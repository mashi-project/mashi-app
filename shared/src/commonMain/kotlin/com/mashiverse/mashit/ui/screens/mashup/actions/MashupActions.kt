package com.mashiverse.mashit.ui.screens.mashup.actions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.states.image.ImageIntent
import com.mashiverse.mashit.data.states.mashup.ActionsIntent
import com.mashiverse.mashit.ui.screens.buttons.SaveActionButton
import com.mashiverse.mashit.ui.screens.mashup.composite.MashupComposite
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.ui.theme.Surface
import com.mashiverse.mashit.ui.theme.TraitShape

@Composable
fun MashupActions(
    mashupDetails: MashupDetails,
    modifier: Modifier = Modifier,
    holderWidth: Dp,
    processImageIntent: (ImageIntent) -> Unit,
    processActionsIntent: (ActionsIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.width(SmallPadding))

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(TraitShape)
                    .background(Surface),
            ) {
                MashupComposite(
                    modifier = modifier,
                    colors = mashupDetails.colors,
                    assets = mashupDetails.assets,
                    holderWidth = holderWidth,
                    processImageIntent = processImageIntent
                )
            }

            Spacer(Modifier.width(SmallPadding))

            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .width(holderWidth + (40 * 2).dp + SmallPadding * 2),
                horizontalArrangement = Arrangement.End
            ) {
                SaveActionButton(onSave = { processActionsIntent(ActionsIntent.OnSave) })
            }
        }

        Spacer(modifier = Modifier.height(SmallPadding))

        ActionsPanel(processActionsIntent = processActionsIntent)
    }
}