package de.dercodeling.pizzacounter.ui.screens.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.dercodeling.pizzacounter.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfoDialog(appVersion: String?, onDismiss: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current

    AlertDialog(
        title = {
            Column {
                Text(
                    stringResource(R.string.app_name),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )


            }
        },
        text = {
            val sectionPadding = 5.dp

            Column {

                if(appVersion != null) {
                    val versionText = stringResource(R.string.info_dialog_version, appVersion)

                    Row (
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box (
                            Modifier
                                .clip(RoundedCornerShape(20))
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        clipboardManager.setText(AnnotatedString(versionText))
                                    },
                                    onLongClickLabel = stringResource(R.string.info_dialog_version_long_press)
                                ),
                        ) {
                            Text(
                                versionText,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(10.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Column (
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20))
                            .clickable {
                                uriHandler.openUri("https://github.com/dercodeling/PizzaCounter")
                            }
                            .padding(sectionPadding)
                    ) {
                        Column (Modifier.weight(1f)) {
                            Text(stringResource(R.string.info_dialog_developer))
                            Text(stringResource(R.string.info_dialog_github))
                        }

                        Icon(
                            painterResource(R.drawable.github_mark),
                            contentDescription = stringResource(R.string.info_dialog_icon_github),
                            Modifier
                                .size(40.dp),
                        )
                    }

                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20))
                            .clickable {
                                uriHandler.openUri("https://www.gnu.org/licenses/gpl-3.0.html#license-text")
                            }
                            .padding(sectionPadding, 0.dp)
                    ) {
                        Text(
                            stringResource(R.string.info_dialog_license),
                            Modifier.weight(1f)
                        )

                        Icon(
                            painterResource(R.drawable.gpl_v3_logo),
                            contentDescription = stringResource(R.string.info_dialog_icon_gpl),
                            Modifier
                                .size(60.dp),
                        )
                    }
                }
            }
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.dialog_close))
            }
        }
    )
}

@Preview
@Composable
fun InfoDialogPreview() {
    InfoDialog("1.x.x") {}
}