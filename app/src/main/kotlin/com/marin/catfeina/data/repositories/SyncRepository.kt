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
import com.marin.core.sync.SyncState
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
    fun executarSincronizacao(): Flow<SyncState>
    suspend fun syncAll(): Result<Unit>
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

    override fun executarSincronizacao(): Flow<SyncState> = flow {
        emit(SyncState.Executando("Iniciando sincronização..."))
        val result = syncAll()
        if (result.isSuccess) {
            emit(SyncState.Sucesso("Sincronização concluída com sucesso!"))
        } else {
            val error = result.exceptionOrNull()
            CatLog.e("Falha ao executar sincronização para a UI", error)
            emit(SyncState.Falha(error?.message ?: "Erro desconhecido"))
        }
    }

    override suspend fun syncAll(): Result<Unit> = withContext(ioDispatcher) {
        try {
            CatLog.d("Iniciando sincronização... Obtendo manifesto.")
            val manifest = fetchManifest()

            // Processar módulos de dados
            for (modulo in manifest.modulos) {
                val versaoLocal = prefs.getModuloVersao(modulo.nome).first()
                if (modulo.versao > versaoLocal) {
                    CatLog.i("Módulo '${modulo.nome}' precisa ser atualizado (local: $versaoLocal, servidor: ${modulo.versao}).")
                    processarModulo(modulo.nome, modulo.arquivo, modulo.versao)
                } else {
                    CatLog.d("Módulo '${modulo.nome}' está atualizado.")
                }
            }

            // Processar pacote de imagens
            manifest.imagens?.let { imagens ->
                val versaoImagensLocal = prefs.getModuloVersao("imagens").first()
                if (imagens.versao > versaoImagensLocal) {
                    CatLog.i("Pacote de imagens precisa ser atualizado (local: $versaoImagensLocal, servidor: ${imagens.versao}).")
                    processarPacoteImagens(imagens.arquivo, imagens.versao)
                } else {
                    CatLog.d("Pacote de imagens está atualizado.")
                }
            }

            // Lidar com atualização do app
            manifest.appUpdate?.let { appUpdate ->
                val versaoAppLocal = BuildConfig.VERSION_CODE
                if (appUpdate.versionCode > versaoAppLocal) {
                    CatLog.i("Nova versão do app disponível: ${appUpdate.versionName}. Changelog: ${appUpdate.changelog}")
                    prefs.setAppUpdateInfo(appUpdate)
                }
            }

            CatLog.i("Sincronização concluída com sucesso!")
            Result.success(Unit)

        } catch (e: Exception) {
            CatLog.e("Falha catastrófica na sincronização", e)
            Result.failure(e)
        }
    }

    private suspend fun fetchManifest(): ManifestDto {
        val manifestUrl = BuildConfig.SYNC_URL + "manifest.json"
        val response = httpClient.get(manifestUrl)
        if (!response.status.isSuccess()) {
            throw IOException("Falha ao baixar o manifesto: ${response.status}")
        }
        val manifestJson = response.bodyAsText()
        return json.decodeFromString(manifestJson)
    }

    private suspend fun processarModulo(nome: String, arquivo: String, novaVersao: Int) {
        try {
            val url = BuildConfig.SYNC_URL + arquivo
            CatLog.d("Baixando dados do módulo '$nome' de $url")
            val response = httpClient.get(url)
            if (!response.status.isSuccess()) {
                throw IOException("Falha ao baixar o arquivo do módulo '$nome': ${response.status}")
            }
            val conteudoJson = response.bodyAsText()

            when (nome) {
                "poesias" -> {
                    val dtos = json.decodeFromString<List<PoesiaSync>>(conteudoJson)
                    poesiaRepository.upsertPoesias(dtos.map { it.toDomain() })
                }
                "atelier" -> {
                    val dtos = json.decodeFromString<List<AtelierSync>>(conteudoJson)
                    atelierRepository.upsertAteliers(dtos.map { it.toDomain() })
                }
                "informativos" -> {
                    val dtos = json.decodeFromString<List<InformativoSync>>(conteudoJson)
                    informativoRepository.upsertInformativos(dtos.map { it.toDomain() })
                }
                "personagens" -> {
                    val dtos = json.decodeFromString<List<PersonagemSync>>(conteudoJson)
                    personagemRepository.upsertPersonagens(dtos.map { it.toDomain() })
                }
                "meow" -> {
                    val dtos = json.decodeFromString<List<MeowSync>>(conteudoJson)
                    meowRepository.upsertMeows(dtos.map { it.toDomain() })
                }
                else -> CatLog.w("Processamento para o módulo de dados '$nome' não implementado.")
            }
            prefs.updateModuloVersao(nome, novaVersao)
            CatLog.i("Módulo '$nome' atualizado para a versão $novaVersao.")
        } catch (e: Exception) {
            CatLog.e("Falha ao processar o módulo '$nome'", e)
            // Não re-lança para permitir que outros módulos continuem
        }
    }

    private suspend fun processarPacoteImagens(arquivo: String, novaVersao: Int) {
        val arquivoZip = File(appContext.cacheDir, "imagens.zip")
        try {
            val url = BuildConfig.SYNC_URL + arquivo
            CatLog.d("Baixando pacote de imagens de $url")
            downloadArquivo(url, arquivoZip)

            val pastaImagensDestino = File(appContext.filesDir, "images")
            if (!pastaImagensDestino.exists()) {
                pastaImagensDestino.mkdirs()
            }
            descompactarArquivo(arquivoZip, pastaImagensDestino)

            prefs.updateModuloVersao("imagens", novaVersao)
            CatLog.i("Pacote de imagens atualizado para a versão $novaVersao.")
        } catch (e: Exception) {
            CatLog.e("Falha ao processar o pacote de imagens", e)
        } finally {
            if (arquivoZip.exists()) arquivoZip.delete()
        }
    }

    private suspend fun downloadArquivo(url: String, arquivoDestino: File) {
        val response = httpClient.get(url)
        if (!response.status.isSuccess()) {
            throw IOException("Falha ao baixar o arquivo $url: ${response.status}")
        }
        response.body<ByteReadChannel>().copyAndClose(arquivoDestino.writeChannel())
    }

    private fun descompactarArquivo(arquivoZip: File, pastaDestino: File) {
        if (pastaDestino.exists()) {
            pastaDestino.deleteRecursively()
        }
        pastaDestino.mkdirs()

        ZipInputStream(arquivoZip.inputStream()).use { zipInputStream ->
            var entry = zipInputStream.nextEntry
            while (entry != null) {
                val newFile = File(pastaDestino, entry.name)
                if (!newFile.canonicalPath.startsWith(pastaDestino.canonicalPath + File.separator)) {
                    throw IOException("Tentativa de Zip Path Traversal: ${entry.name}")
                }
                if (entry.isDirectory) {
                    if (!newFile.isDirectory && !newFile.mkdirs()) {
                        throw IOException("Falha ao criar diretório ${newFile.absolutePath}")
                    }
                } else {
                    val parent = newFile.parentFile
                    if (parent != null && !parent.isDirectory && !parent.mkdirs()) {
                        throw IOException("Falha ao criar diretório pai ${parent.absolutePath}")
                    }
                    newFile.outputStream().use { fos -> zipInputStream.copyTo(fos) }
                }
                zipInputStream.closeEntry()
                entry = zipInputStream.nextEntry
            }
        }
    }
}
