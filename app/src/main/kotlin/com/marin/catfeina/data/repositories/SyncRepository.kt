/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/data/repositories/SyncRepository.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Repositório que gerencia a sincronização de dados da rede.
*
*/
package com.marin.catfeina.data.repositories

import android.content.Context
import com.marin.catfeina.BuildConfig
import com.marin.catfeina.data.sync.AtelierSync
import com.marin.catfeina.data.sync.InformativoSync
import com.marin.catfeina.data.sync.ManifestDto
import com.marin.catfeina.data.sync.MeowSync
import com.marin.catfeina.data.sync.PersonagemSync
import com.marin.catfeina.data.sync.PoesiaSync
import com.marin.catfeina.data.sync.toDomain
import com.marin.catfeina.usecases.ResultadoVerificacao
import com.marin.core.ui.UiState
import com.marin.core.util.CatLog
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.copyAndClose
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import java.util.zip.ZipInputStream
import javax.inject.Inject
import javax.inject.Singleton

interface SyncRepository {
    fun executarSincronizacao(): Flow<UiState<Unit>>
    suspend fun syncAll(): Result<Unit>
    suspend fun verificarAtualizacoes(): ResultadoVerificacao
}

@Singleton
class SyncRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val appContext: Context,
    private val httpClient: HttpClient,
    private val json: Json,
    private val prefs: UserPreferencesRepository,
    private val poesiaRepository: PoesiaRepository,
    private val atelierRepository: AtelierRepository,
    private val informativoRepository: InformativoRepository,
    private val personagemRepository: PersonagemRepository,
    private val meowRepository: MeowRepository,
    private val ioDispatcher: CoroutineDispatcher
) : SyncRepository {

    override fun executarSincronizacao(): Flow<UiState<Unit>> = flow {
        emit(UiState.Loading("Iniciando sincronização..."))
        syncAll().fold(
            onSuccess = { emit(UiState.Success(Unit)) },
            onFailure = { emit(UiState.Error(it.message ?: "Erro desconhecido")) }
        )
    }

    override suspend fun syncAll(): Result<Unit> = withContext(ioDispatcher) {
        try {
            CatLog.d("Iniciando syncAll... Obtendo manifesto.")
            val manifest = fetchManifest()

            for (modulo in manifest.modulos) {
                val versaoLocal = prefs.getModuloVersao(modulo.nome).first()
                if (modulo.versao > versaoLocal) {
                    CatLog.i("Módulo '${modulo.nome}' precisa ser atualizado (local: $versaoLocal, servidor: ${modulo.versao}).")
                    // O caminho do arquivo no manifesto pode conter um diretório (ex: "data/"). Usamos apenas o nome do arquivo.
                    val nomeArquivo = File(modulo.arquivo).name
                    val url = BuildConfig.SYNC_URL + nomeArquivo
                    processarModulo(modulo.nome, url, modulo.versao)
                } else {
                    CatLog.d("Módulo '${modulo.nome}' está atualizado.")
                }
            }

            manifest.imagens?.let { imagens ->
                val versaoImagensLocal = prefs.getModuloVersao("imagens").first()
                if (imagens.versao > versaoImagensLocal) {
                    CatLog.i("Pacote de imagens precisa ser atualizado (local: $versaoImagensLocal, servidor: ${imagens.versao}).")
                    val url = BuildConfig.SYNC_URL + "images.zip"
                    processarPacoteImagens(url, imagens.versao)
                } else {
                    CatLog.d("Pacote de imagens está atualizado.")
                }
            }

            manifest.appUpdate?.let {
                if (it.versionCode > BuildConfig.VERSION_CODE) prefs.setAppUpdateInfo(it) else prefs.setAppUpdateInfo(null)
            }

            CatLog.i("syncAll concluído com sucesso!")
            Result.success(Unit)

        } catch (e: Exception) {
            CatLog.e("Falha catastrófica no syncAll", e)
            Result.failure(e)
        }
    }

   override suspend fun verificarAtualizacoes(): ResultadoVerificacao = withContext(ioDispatcher) {
        try {
            CatLog.d("Verificando atualizações...")
            val manifest = fetchManifest()

            val dadosDisponiveis = manifest.modulos.any { modulo ->
                modulo.versao > prefs.getModuloVersao(modulo.nome).first()
            } || manifest.imagens?.let { it.versao > prefs.getModuloVersao("imagens").first() } == true

            val appDisponivel = manifest.appUpdate?.let { appUpdate ->
                appUpdate.versionCode > BuildConfig.VERSION_CODE
            } ?: false

            CatLog.i("Verificação de atualizações concluída. Dados: $dadosDisponiveis, App: $appDisponivel")
            ResultadoVerificacao(
                atualizacaoDeDadosDisponivel = dadosDisponiveis,
                atualizacaoDeAppDisponivel = appDisponivel
            )
        } catch (e: Exception) {
            CatLog.e("Falha ao verificar atualizações. Presumindo que não há novidades.", e)
            ResultadoVerificacao(atualizacaoDeDadosDisponivel = false, atualizacaoDeAppDisponivel = false)
        }
    }

    private suspend fun fetchManifest(): ManifestDto {
        val manifestUrl = BuildConfig.SYNC_URL + BuildConfig.SYNC_MANIFEST_NAME
        val response = httpClient.get(manifestUrl)
        if (!response.status.isSuccess()) {
            throw IOException("Falha ao baixar o manifesto: ${response.status}")
        }
        val manifestJson = response.bodyAsText()
        return json.decodeFromString(manifestJson)
    }

    private suspend fun processarModulo(nome: String, url: String, novaVersao: Int) {
        val response = httpClient.get(url)
        if (!response.status.isSuccess()) throw IOException("Falha ao baixar o arquivo do módulo '$nome': ${response.status}")
        val conteudoJson = response.bodyAsText()

        when (nome) {
            "poesias" -> poesiaRepository.upsertPoesias(json.decodeFromString<List<PoesiaSync>>(conteudoJson).map { it.toDomain() })
            "atelier" -> atelierRepository.upsertAteliers(json.decodeFromString<List<AtelierSync>>(conteudoJson).map { it.toDomain() })
            "informativos" -> informativoRepository.upsertInformativos(json.decodeFromString<List<InformativoSync>>(conteudoJson).map { it.toDomain() })
            "personagens" -> personagemRepository.upsertPersonagens(json.decodeFromString<List<PersonagemSync>>(conteudoJson).map { it.toDomain() })
            "meows" -> meowRepository.upsertMeows(json.decodeFromString<List<MeowSync>>(conteudoJson).map { it.toDomain() })
            else -> CatLog.w("Processamento para o módulo de dados '$nome' não implementado.")
        }
        prefs.updateModuloVersao(nome, novaVersao)
    }

    private suspend fun processarPacoteImagens(url: String, novaVersao: Int) {
        val arquivoZip = File(appContext.cacheDir, "imagens.zip")
        try {
            downloadArquivo(url, arquivoZip)
            descompactarArquivo(arquivoZip, File(appContext.filesDir, "images"))
            prefs.updateModuloVersao("imagens", novaVersao)
        } finally {
            if (arquivoZip.exists()) arquivoZip.delete()
        }
    }

    private suspend fun downloadArquivo(url: String, arquivoDestino: File) {
        val response = httpClient.get(url)
        if (!response.status.isSuccess()) throw IOException("Falha ao baixar o arquivo $url: ${response.status}")
        response.body<ByteReadChannel>().copyAndClose(arquivoDestino.writeChannel())
    }

    private fun descompactarArquivo(arquivoZip: File, pastaDestino: File) {
        if (pastaDestino.exists()) pastaDestino.deleteRecursively()
        pastaDestino.mkdirs()
        ZipInputStream(arquivoZip.inputStream()).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                val newFile = File(pastaDestino, entry.name)
                if (!newFile.canonicalPath.startsWith(pastaDestino.canonicalPath + File.separator)) throw IOException("Zip Path Traversal: ${entry.name}")
                if (entry.isDirectory) {
                    if (!newFile.mkdirs()) throw IOException("Falha ao criar diretório ${newFile.absolutePath}")
                } else {
                    newFile.parentFile?.mkdirs()
                    newFile.outputStream().use { fos -> zis.copyTo(fos) }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
    }
}
