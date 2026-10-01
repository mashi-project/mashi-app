package com.serhij.mashi.ui.screens.mashup.preview

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.serhij.mashi.data.models.mashup.MashupDetails
import com.serhij.mashi.data.models.screen.ScreenInfo
import com.serhij.mashi.data.models.traits.OptionalTrait
import com.serhij.mashi.data.models.traits.TraitDetails
import com.serhij.mashi.data.models.traits.TraitType
import com.serhij.mashi.data.states.image.ImageIntent
import com.serhij.mashi.ui.grid.TraitHolderGrid
import com.serhij.mashi.ui.theme.BottomSheetShape
import com.serhij.mashi.ui.theme.ContentAccentColor
import com.serhij.mashi.ui.theme.ContentColor
import com.serhij.mashi.ui.theme.MediumPadding
import com.serhij.mashi.ui.theme.Padding
import com.serhij.mashi.ui.theme.SmallPadding
import com.serhij.mashi.ui.theme.Surface
import com.serhij.mashi.utils.helpers.detectScreenType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashupPreview(
    mashupDetails: MashupDetails,
    closeBottomSheet: () -> Unit,
    sheetState: SheetState,
    processImageIntent: (ImageIntent) -> Unit,
    height: Dp,
) {
    val scope = rememberCoroutineScope()

    val optionalTraits = remember(mashupDetails.assets) {
        mashupDetails.assets.map { asset ->
            OptionalTrait(trait = asset, selected = true)
        }.toMutableStateList()
    }

    BoxWithConstraints {
        val screenType = maxWidth.detectScreenType()

        ModalBottomSheet(
            modifier = if (screenType == ScreenInfo.EXPANDED) {
                Modifier
                    .padding(start = 328.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            } else {
                Modifier.fillMaxWidth()
            },
            shape = BottomSheetShape,
            onDismissRequest = closeBottomSheet,
            sheetState = sheetState,
            containerColor = Surface,
            contentColor = ContentColor,
            dragHandle = null,
            sheetGesturesEnabled = false,
        ) {
            Column(
                modifier = Modifier
                    .height(height)
                    .padding(
                        start = Padding,
                        end = Padding,
                        top = Padding,
                    )
                    .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        modifier = Modifier.size(32.dp),
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    closeBottomSheet()
                                }
                            }
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Close",
                            tint = ContentAccentColor,
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(SmallPadding)
                )

                TraitHolderGrid(
                    items = optionalTraits.sortedBy { it.trait.type },
                    spacedByVert = MediumPadding,
                    spacedByHoriz = MediumPadding,
                    columns = screenType.columns,
                    processImageIntent = processImageIntent,
                    onClick = {  }
                )
            }
        }
    }
}