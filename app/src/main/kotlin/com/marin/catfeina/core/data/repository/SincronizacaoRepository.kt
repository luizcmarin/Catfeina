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

/*
 *
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
 *
 */

/*
 * // ===================================================================================
 * //  Projeto: Catfeina
 * //  Arquivo: SincronizacaoRepository.kt
 * //
 * //  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
 * //
 * //  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
 * //
 * //  Este arquivo faz parte do projeto Catfeina.
 * //  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
 * //  dele, é estritamente proibida.
 * // ===================================================================================
 * //  Nota:
 * //
 * //
 * // ===================================================================================
 *
 */

package com.marin.catfeina.core.data.repository

import android.content.Context
import com.marin.catfeina.BuildConfig
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
import kotlinx.coroutines.flow.first
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
    private val poesiaRepository: PoesiaRepository
) {

    suspend fun executarSincronizacao() {
        Timber.i("Iniciando tarefa de sincronização de dados...")

        val zipFile = File(appContext.cacheDir, BuildConfig.SYNC_ZIP_FILE_NAME)
        val pastaDestino = File(appContext.cacheDir, "sync_temp")

        try {
            Timber.d("Passo 1: Iniciando download...")
            downloadArquivo(BuildConfig.SYNC_URL, zipFile)
            Timber.d("Passo 1: Download concluído.")

            Timber.d("Passo 2: Iniciando descompactação...")
            descompactarArquivo(zipFile, pastaDestino)
            Timber.d("Passo 2: Descompactação concluída.")

            Timber.d("Passo 3: Iniciando leitura do manifesto...")
            val manifesto = lerManifesto(pastaDestino)
            Timber.i("Manifesto lido com sucesso: Versão=${manifesto.versao}, Nota='${manifesto.nota}'")
            Timber.d("Passo 3: Leitura do manifesto concluída.")

            Timber.d("Passo 4: Iniciando verificação de versão...")
            val ultimaSincronizacaoLocal = preferenciasRepository.ultimaSincronizacao.first()
//            if (manifesto.versao <= ultimaSincronizacaoLocal) {
//                Timber.i("Dados locais já estão atualizados. Versão local: $ultimaSincronizacaoLocal, Versão remota: ${manifesto.versao}. Sincronização não necessária.")
//                return
//            }
            Timber.i("Nova versão de dados encontrada. Local: $ultimaSincronizacaoLocal, Remota: ${manifesto.versao}. Prosseguindo...")
            Timber.d("Passo 4: Verificação de versão concluída.")

            Timber.d("Passo 5: Iniciando processamento dos dados...")
            processarDados(pastaDestino, manifesto)
            Timber.d("Passo 5: Processamento dos dados concluído.")

            Timber.d("Passo 6: Iniciando processamento das imagens...")
            processarImagens(pastaDestino)
            Timber.d("Passo 6: Processamento das imagens concluído.")
            
            Timber.d("Passo 8: Iniciando salvamento do timestamp...")
            preferenciasRepository.salvarUltimaSincronizacao(manifesto.versao)
            Timber.i("Timestamp da nova versão (${manifesto.versao}) salvo com sucesso.")
            Timber.d("Passo 8: Salvamento do timestamp concluído.")

            Timber.i("Sincronização concluída com sucesso!")
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

    private suspend fun descompactarArquivo(arquivoZip: File, pastaDestino: File) {
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
            val arquivoManifesto = File(pastaManifesto, BuildConfig.SYNC_MANIFEST_NAME)
            if (!arquivoManifesto.exists()) {
                throw IOException("Arquivo de manifesto não encontrado na pasta de sincronização.")
            }
            val textoManifesto = arquivoManifesto.readText()
            json.decodeFromString(textoManifesto)
        }
    }

    private suspend fun processarDados(pastaRaizSync: File, manifesto: SyncManifest) {
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
            val conteudoJson = withContext(Dispatchers.IO) { arquivoJson.readText() }

            when (nomeArquivo) {
                "poesias.json" -> {
                    val poesias = json.decodeFromString<List<PoesiaSync>>(conteudoJson)
                    poesiaRepository.upsertPoesias(poesias)
                    Timber.i("${poesias.size} poesias foram inseridas/atualizadas.")
                }
                // TODO: Adicionar cases para outros arquivos de dados (personagens.json, etc.)
                else -> {
                    Timber.w("Processamento para o arquivo '${nomeArquivo}' não implementado.")
                }
            }
        }
    }

    private suspend fun processarImagens(pastaRaizSync: File) {
        withContext(Dispatchers.IO) {
            val pastaImagensOrigem = File(pastaRaizSync, BuildConfig.SYNC_IMAGE_FOLDER_NAME)
            if (!pastaImagensOrigem.exists() || !pastaImagensOrigem.isDirectory) {
                Timber.w("Pasta de imagens não encontrada no pacote de sincronização. Pulando etapa.")
                return@withContext
            }

            val pastaImagensDestino = File(appContext.filesDir, BuildConfig.SYNC_IMAGE_FOLDER_NAME)
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
