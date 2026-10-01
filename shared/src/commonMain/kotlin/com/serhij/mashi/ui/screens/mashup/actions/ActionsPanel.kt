package com.serhij.mashi.ui.screens.mashup.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.serhij.mashi.data.states.mashup.ActionsIntent
import com.serhij.mashi.ui.screens.buttons.ActionButton
import com.serhij.mashi.ui.screens.buttons.ColorSelectActionButton
import com.serhij.mashi.ui.theme.SmallPadding

@Composable
fun ActionsPanel(
    modifier: Modifier = Modifier,
    processActionsIntent: (ActionsIntent) -> Unit,
    isLurking: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(SmallPadding)
        ) {
            ColorSelectActionButton(onColor = { processActionsIntent(ActionsIntent.OnColor) })

            if (!isLurking) {
                ActionButton(
                    icon = Icons.Default.Delete,
                    onClick = { processActionsIntent(ActionsIntent.OnReset) },
                )

                ActionButton(
                    icon = Icons.Default.Refresh,
                    onClick = { processActionsIntent(ActionsIntent.OnRandom) }
                )

                ActionButton(
                    icon = Icons.AutoMirrored.Filled.Undo,
                    onClick = { processActionsIntent(ActionsIntent.OnUndo) },
                )

                ActionButton(
                    icon = Icons.AutoMirrored.Filled.Redo,
                    onClick = { processActionsIntent(ActionsIntent.OnRedo) }
                )

                ActionButton(
                    icon = Icons.Default.Download,
                    onClick = { processActionsIntent(ActionsIntent.OnGenerate) },
                )
            }
        }
    }
}