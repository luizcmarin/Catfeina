/*
*  Projeto: Catfeina/Catverso
*  Arquivo: app/src/main/kotlin/com/marin/catfeina/ui/componentes/UpdateDialog.kt
*
*  Direitos autorais (c) 2025 Marin. Todos os direitos reservados.
*
*  Autores: Luiz Carlos Marin / Ivete Gielow Marin / Caroline Gielow Marin
*
*  Este arquivo faz parte do projeto Catfeina.
*  A reprodução ou distribuição não autorizada deste arquivo, ou de qualquer parte
*  dele, é estritamente proibida.
*
*  Nota: Composable reutilizável para o diálogo de atualização de versão do app.
*
*/
package com.marin.catfeina.ui.componentes

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.marin.catfeina.R
import com.marin.catfeina.data.sync.AppUpdateDto
import com.marin.core.ui.Icones

@Composable
fun UpdateDialog(
    updateInfo: AppUpdateDto,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icones.Info, contentDescription = null) },
        title = { Text(text = stringResource(R.string.dialogo_atualizacao_titulo)) },
        text = {
            Column {
                Text(stringResource(R.string.dialogo_atualizacao_mensagem, updateInfo.versionName))
                Text(updateInfo.changelog, modifier = Modifier.padding(top = 8.dp))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateInfo.url))
                    context.startActivity(intent)
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.dialogo_atualizacao_confirmar))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialogo_atualizacao_cancelar))
            }
        }
    )
}
