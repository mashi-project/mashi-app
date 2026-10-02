package com.mashiverse.mashit.ui.screens.mashup.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.ui.theme.ContentAccentColor

@Composable
fun GenerateDialog(
    onGenerate: (Boolean, ImageType) -> Unit,
    onDismiss: () -> Unit,
    isDiscord: Boolean
) {
    var checked by remember { mutableStateOf(isDiscord) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = Color.DarkGray,
                contentColor = ContentAccentColor
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        modifier = Modifier.size(24.dp),
                        checked = checked,
                        colors = CheckboxDefaults.colors().copy(
                            checkedBorderColor = Color.White,
                            uncheckedBorderColor = Color.White,
                            checkedCheckmarkColor = Color.White,
                        ),
                        onCheckedChange = { isChecked -> checked = isChecked }
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text("Share on Discord", fontSize = 10.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    onGenerate.invoke(checked, ImageType.PNG)
                    onDismiss.invoke()
                }) {
                    Text("Save and generate PNG")
                }

                Button(onClick = {
                    onGenerate.invoke(checked, ImageType.GIF)
                    onDismiss.invoke()
                }) {
                    Text("Save and generate GIF")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("You will be notified once ready", fontSize = 10.sp)
            }
        }
    }
}