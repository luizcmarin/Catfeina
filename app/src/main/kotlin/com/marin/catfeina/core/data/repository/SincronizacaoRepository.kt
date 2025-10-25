/*
 *  Projeto: Catfeina
 *  Arquivo: SincronizacaoRepository.kt
 *
 *  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 *
 *  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 *
 *  Este arquivo faz parte do projeto Catfeina.
 *  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 *  dele, é estritamente proibida.
 *
 *  Nota:
 *
 */

package com.marin.catfeina.core.data.repository

import android.content.Context
import com.marin.catfeina.BuildConfig
import com.marin.catfeina.core.sync.AtelierSync
import com.marin.catfeina.core.sync.InformativoSync
import com.marin.catfeina.core.sync.PersonagemSync
import com.marin.catfeina.core.sync.PoesiaSync
import com.marin.catfeina.core.sync.SyncManifest
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.copyAndClose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.zip.ZipInputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SincronizacaoRepository @Inject constructor(
    @param:ApplicationContext private val appContext: Context,
    private val httpClient: HttpClient,
    private val json: Json,
    private val preferenciasRepository: PreferenciasUsuarioRepository,
    private val poesiaRepository: PoesiaRepository,
    private val atelierRepository: AtelierRepository,
    private val informativoRepository: InformativoRepository,
    private val personagemRepository: PersonagemRepository
) {

    /**
     * Executa a sincronização e emite mensagens de status para a UI.
     * @return Um Flow de Strings com as mensagens de progresso.
     */
    fun executarSincronizacao(): Flow<String> = flow {
        val zipFile = File(appContext.cacheDir, BuildConfig.SYNC_ZIP_FILE_NAME)
        val pastaDestino = File(appContext.cacheDir, "sync_temp")

        try {
            emit("Iniciando sincronização...")

            emit("Baixando novos dados...")
            downloadArquivo(BuildConfig.SYNC_URL, zipFile)

            emit("Preparando arquivos...")
            descompactarArquivo(zipFile, pastaDestino)

            emit("Verificando atualizações...")
            val manifesto = lerManifesto(pastaDestino)

            val ultimaSincronizacaoLocal = preferenciasRepository.ultimaSincronizacao.first()
            if (manifesto.versao <= ultimaSincronizacaoLocal) {
                emit("Seus dados já estão atualizados.")
                // Encerra o fluxo se não houver nada a fazer.
                return@flow
            }

            emit("Nova versão encontrada. Atualizando...")
            processarDados(pastaDestino, manifesto)

            emit("Atualizando imagens...")
            processarImagens(pastaDestino)

            emit("Finalizando...")
            preferenciasRepository.salvarUltimaSincronizacao(manifesto.versao)

            emit("Sincronização concluída!")

        } catch (e: Exception) {
            Timber.e(e, "Falha na sincronização")
            // Emite uma mensagem de erro para ser exibida ao usuário.
            emit("Erro na sincronização: ${e.message ?: "Verifique sua conexão."}")
        } finally {
            Timber.d("Iniciando limpeza de arquivos temporários...")
            if (zipFile.exists()) zipFile.delete()
            if (pastaDestino.exists()) pastaDestino.deleteRecursively()
            Timber.d("Arquivos temporários de sincronização limpos.")
        }
    }

    private suspend fun downloadArquivo(url: String, arquivoDestino: File) {
        withContext(Dispatchers.IO) {
            Timber.d("Baixando arquivo de: $url")
            val response: HttpResponse = httpClient.get(url)

            if (!response.status.isSuccess()) {
                throw IOException("Falha no download. Status: ${response.status.value}")
            }

            val channel: ByteReadChannel = response.body()
            val fileChannel = arquivoDestino.writeChannel()

            channel.copyAndClose(fileChannel)

            Timber.d("Arquivo salvo em: ${arquivoDestino.absolutePath}")
        }
    }

    private suspend fun descompactarArquivo(
        arquivoZip: File,
        pastaDestino: File
    ) {
        withContext(Dispatchers.IO) {
            Timber.d("Descompactando ${arquivoZip.name} para ${pastaDestino.absolutePath}")
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
                        newFile.outputStream().use { fos ->
                            zipInputStream.copyTo(fos)
                        }
                    }
                    zipInputStream.closeEntry()
                    entry = zipInputStream.nextEntry
                }
            }
            Timber.d("Descompactação concluída.")
        }
    }

    private suspend fun lerManifesto(pastaManifesto: File): SyncManifest {
        return withContext(Dispatchers.IO) {
            val arquivoManifesto =
                File(pastaManifesto, BuildConfig.SYNC_MANIFEST_NAME)
            if (!arquivoManifesto.exists()) {
                throw IOException("Arquivo de manifesto não encontrado na pasta de sincronização.")
            }
            val textoManifesto = arquivoManifesto.readText()
            json.decodeFromString(textoManifesto)
        }
    }

    private suspend fun processarDados(
        pastaRaizSync: File,
        manifesto: SyncManifest
    ) {
        val pastaDados = File(pastaRaizSync, BuildConfig.SYNC_DATA_FOLDER_NAME)
        if (!pastaDados.exists() || !pastaDados.isDirectory) {
            Timber.w("Pasta de dados não encontrada. Pulando etapa de processamento de dados.")
            return
        }

        manifesto.arquivosDados.forEach { nomeArquivo ->
            val arquivoJson = File(pastaDados, nomeArquivo)
            if (!arquivoJson.exists()) {
                Timber.w("Arquivo de dados '${nomeArquivo}' listado no manifesto, mas não encontrado. Pulando.")
                return@forEach
            }

            Timber.d("Processando arquivo de dados: $nomeArquivo")
            val conteudoJson =
                withContext(Dispatchers.IO) { arquivoJson.readText() }

            when (nomeArquivo) {
                "poesias.json" -> {
                    val poesias =
                        json.decodeFromString<List<PoesiaSync>>(conteudoJson)
                    poesiaRepository.upsertPoesias(poesias)
                    Timber.i("${poesias.size} poesias foram inseridas/atualizadas.")
                }

                "atelier.json" -> {
                    val ateliers =
                        json.decodeFromString<List<AtelierSync>>(conteudoJson)
                    atelierRepository.upsertAteliers(ateliers)
                    Timber.i("${ateliers.size} notas do atelier foram inseridas/atualizadas.")
                }

                "informativos.json" -> {
                    val informativos =
                        json.decodeFromString<List<InformativoSync>>(
                            conteudoJson
                        )
                    informativoRepository.upsertInformativos(informativos)
                    Timber.i("${informativos.size} informativos foram inseridos/atualizados.")
                }

                "personagens.json" -> {
                    val personagens =
                        json.decodeFromString<List<PersonagemSync>>(conteudoJson)
                    personagemRepository.upsertPersonagens(personagens)
                    Timber.i("${personagens.size} personagens foram inseridos/atualizados.")
                }

                else -> {
                    Timber.w("Processamento para o arquivo '${nomeArquivo}' não implementado.")
                }
            }
        }
    }

    private suspend fun processarImagens(pastaRaizSync: File) {
        withContext(Dispatchers.IO) {
            val pastaImagensOrigem =
                File(pastaRaizSync, BuildConfig.SYNC_IMAGE_FOLDER_NAME)
            if (!pastaImagensOrigem.exists() || !pastaImagensOrigem.isDirectory) {
                Timber.w("Pasta de imagens não encontrada no pacote de sincronização. Pulando etapa.")
                return@withContext
            }

            val pastaImagensDestino =
                File(appContext.filesDir, BuildConfig.SYNC_IMAGE_FOLDER_NAME)
            if (!pastaImagensDestino.exists()) {
                pastaImagensDestino.mkdirs()
            }

            val arquivosDeImagem = pastaImagensOrigem.listFiles()
            if (arquivosDeImagem.isNullOrEmpty()) {
                Timber.i("Nenhuma imagem encontrada na pasta de sincronização para processar.")
                return@withContext
            }

            var contador = 0
            arquivosDeImagem.forEach { arquivo ->
                if (arquivo.isFile) {
                    val arquivoDestino = File(pastaImagensDestino, arquivo.name)
                    arquivo.copyTo(arquivoDestino, overwrite = true)
                    contador++
                }
            }
            Timber.i("$contador imagens foram copiadas/atualizadas para o armazenamento interno.")
        }
    }
}
