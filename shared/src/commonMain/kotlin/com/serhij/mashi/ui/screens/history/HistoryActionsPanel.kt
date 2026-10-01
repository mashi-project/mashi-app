package com.serhij.mashi.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.serhij.mashi.ui.screens.buttons.ActionButton
import com.serhij.mashi.ui.theme.SmallPadding

@Composable
fun HistoryActionsPanel(
    modifier: Modifier = Modifier,
    onShare: () -> Unit,
    onDelete: () -> Unit,
    onDownload: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(SmallPadding)
        ) {
            ActionButton(
                icon = Icons.Default.Delete,
                isRed = true,
                onClick = onDelete,
            )

            ActionButton(
                icon = Icons.Default.Share,
                onClick = onShare
            )

            ActionButton(
                icon = Icons.Default.Save,
                onClick = onDownload
            )
        }
    }
}