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
import com.marin.catfeina.data.sync.ModuloDto
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
            onFailure = { error ->
                CatLog.e("Falha na sincronização", error)
                emit(UiState.Error(error.message ?: "Erro desconhecido durante a sincronização"))
            }
        )
    }

    override suspend fun syncAll(): Result<Unit> = withContext(ioDispatcher) {
        try {
            CatLog.d("Iniciando syncAll... Obtendo manifesto.")
            val manifest = fetchManifest()

            val modulosParaAtualizar = mutableListOf<ModuloDto>()
            for (modulo in manifest.modulos) {
                if (modulo.versao > prefs.getModuloVersao(modulo.nome).first()) {
                    modulosParaAtualizar.add(modulo)
                }
            }

            val imagensParaAtualizar = manifest.imagens?.let { it.versao > prefs.getModuloVersao("imagens").first() } ?: false

            if (modulosParaAtualizar.isEmpty() && !imagensParaAtualizar) {
                CatLog.i("Todos os módulos estão atualizados. Sincronização não necessária.")
            } else {
                CatLog.i("Atualizações encontradas. Baixando pacote de sincronização...")
                processarPacoteSync(modulosParaAtualizar, imagensParaAtualizar, manifest)
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

    private suspend fun processarPacoteSync(
        modulos: List<ModuloDto>,
        atualizarImagens: Boolean,
        manifest: ManifestDto
    ) {
        val zipFile = File(appContext.cacheDir, "CatfeinaSync.zip")
        val unzipDir = File(appContext.cacheDir, "sync_temp")

        try {
            val url = BuildConfig.SYNC_URL + "CatfeinaSync.zip" + "?t=" + System.currentTimeMillis()
            downloadArquivo(url, zipFile)

            descompactarArquivo(zipFile, unzipDir)

            // Processa módulos de dados
            for (modulo in modulos) {
                CatLog.i("Processando atualização para o módulo '${modulo.nome}' (versão ${modulo.versao}).")
                val arquivoJson = File(unzipDir, modulo.arquivo)
                if (arquivoJson.exists()) {
                    processarModuloLocal(modulo.nome, arquivoJson.readText(), modulo.versao)
                } else {
                    throw IOException("Arquivo '${modulo.arquivo}' não encontrado no pacote de sincronização.")
                }
            }

            // Processa imagens
            if (atualizarImagens && manifest.imagens != null) {
                CatLog.i("Atualizando pacote de imagens para a versão ${manifest.imagens.versao}.")
                val pastaImagensOrigem = File(unzipDir, manifest.imagens.arquivo)
                val pastaImagensDestino = File(appContext.filesDir, "images")

                if (pastaImagensOrigem.exists()) {
                    if (pastaImagensDestino.exists()) pastaImagensDestino.deleteRecursively()
                    pastaImagensOrigem.renameTo(pastaImagensDestino)
                    prefs.updateModuloVersao("imagens", manifest.imagens.versao)
                } else {
                    CatLog.w("Pasta de imagens '${manifest.imagens.arquivo}' não encontrada no pacote de sincronização.")
                }
            }

        } finally {
            if (zipFile.exists()) zipFile.delete()
            if (unzipDir.exists()) unzipDir.deleteRecursively()
            CatLog.d("Limpeza dos arquivos temporários de sincronização concluída.")
        }
    }


    override suspend fun verificarAtualizacoes(): ResultadoVerificacao = withContext(ioDispatcher) {
        try {
            CatLog.d("Verificando atualizações...")
            val manifest = fetchManifest()

            var dadosDisponiveis = false
            for (modulo in manifest.modulos) {
                if (modulo.versao > prefs.getModuloVersao(modulo.nome).first()) {
                    dadosDisponiveis = true
                    break
                }
            }
            if (!dadosDisponiveis) {
                dadosDisponiveis = manifest.imagens?.let { it.versao > prefs.getModuloVersao("imagens").first() } == true
            }

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
        val manifestUrl = BuildConfig.SYNC_URL + BuildConfig.SYNC_MANIFEST_NAME + "?t=" + System.currentTimeMillis()
        val response = httpClient.get(manifestUrl)
        if (!response.status.isSuccess()) {
            throw IOException("Falha ao baixar o manifesto: ${response.status}")
        }
        val manifestJson = response.bodyAsText()
        return json.decodeFromString(manifestJson)
    }

    private suspend fun processarModuloLocal(nome: String, conteudoJson: String, novaVersao: Int) {
        when (nome) {
            "poesias" -> poesiaRepository.upsertPoesias(json.decodeFromString<List<PoesiaSync>>(conteudoJson).map { it.toDomain() })
            "atelier" -> atelierRepository.upsertAteliers(json.decodeFromString<List<AtelierSync>>(conteudoJson).map { it.toDomain() })
            "informativos" -> informativoRepository.upsertInformativos(json.decodeFromString<List<InformativoSync>>(conteudoJson).map { it.toDomain() })
            "personagens" -> personagemRepository.upsertPersonagens(json.decodeFromString<List<PersonagemSync>>(conteudoJson).map { it.toDomain() })
            "meows" -> meowRepository.upsertMeows(json.decodeFromString<List<MeowSync>>(conteudoJson).map { it.toDomain() })
            else -> CatLog.w("Processamento para o módulo de dados '$nome' não implementado.")
        }
        prefs.updateModuloVersao(nome, novaVersao)
        CatLog.d("Módulo '$nome' atualizado para a versão $novaVersao.")
    }

    private suspend fun downloadArquivo(url: String, arquivoDestino: File) {
        CatLog.d("Baixando arquivo de: $url")
        val response = httpClient.get(url)
        if (!response.status.isSuccess()) throw IOException("Falha ao baixar o arquivo $url: ${response.status}")
        response.body<ByteReadChannel>().copyAndClose(arquivoDestino.writeChannel())
        CatLog.d("Arquivo salvo em: ${arquivoDestino.absolutePath}")
    }

    private fun descompactarArquivo(arquivoZip: File, pastaDestino: File) {
        CatLog.d("Descompactando '${arquivoZip.name}' para '${pastaDestino.absolutePath}'")
        if (pastaDestino.exists()) pastaDestino.deleteRecursively()
        pastaDestino.mkdirs()

        ZipInputStream(arquivoZip.inputStream().buffered()).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                val newFile = File(pastaDestino, entry.name)

                // Prevenção contra 'Zip Path Traversal'
                if (!newFile.canonicalPath.startsWith(pastaDestino.canonicalPath + File.separator)) {
                    throw IOException("Zip Path Traversal: ${entry.name}")
                }

                if (entry.isDirectory) {
                    if (!newFile.isDirectory && !newFile.mkdirs()) throw IOException("Falha ao criar diretório ${newFile.absolutePath}")
                } else {
                    // Garante que o diretório pai exista
                    newFile.parentFile?.let {
                        if (!it.isDirectory && !it.mkdirs()) throw IOException("Falha ao criar diretório pai ${it.absolutePath}")
                    }
                    newFile.outputStream().use { fos -> zis.copyTo(fos) }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
        CatLog.d("Descompactação concluída.")
    }
}
