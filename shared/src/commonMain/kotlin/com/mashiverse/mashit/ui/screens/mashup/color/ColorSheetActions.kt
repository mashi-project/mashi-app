package com.mashiverse.mashit.ui.screens.mashup.color

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ButtonBackground
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.Primary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSheetActions(
    scope: CoroutineScope,
    sheetState: SheetState,
    closeBottomSheet: () -> Unit,
    saveColors: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier
                .weight(1F)
                .height(40.dp),
            onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        closeBottomSheet.invoke()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = ButtonBackground,
                contentColor = ContentAccentColor
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Cancel",
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(Padding))

        Button(
            modifier = Modifier
                .weight(1F)
                .height(40.dp),
            onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        saveColors.invoke()
                        closeBottomSheet.invoke()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Primary,
                contentColor = ContentAccentColor
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Save",
                fontSize = 14.sp
            )
        }
    }
}