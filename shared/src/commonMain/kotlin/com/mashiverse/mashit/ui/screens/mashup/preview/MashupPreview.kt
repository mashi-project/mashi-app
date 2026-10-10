package com.mashiverse.mashit.ui.screens.mashup.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.traits.OptionalTrait
import com.mashiverse.mashit.data.states.image.ImageIntent
import com.mashiverse.mashit.ui.grid.TraitHolderGrid
import com.mashiverse.mashit.ui.theme.BottomSheetShape
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.MediumPadding
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.Surface
import com.mashiverse.mashit.utils.helpers.detectScreenType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashupPreview(
    modifier: Modifier = Modifier,
    mashupDetails: MashupDetails,
    closeBottomSheet: () -> Unit,
    isHalfWidth: Boolean,
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

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .systemBarsPadding(),
        contentAlignment = Alignment.CenterEnd
    ) {
        val sheetWidth = if (isHalfWidth) maxWidth / 2 else maxWidth
        val screenType = sheetWidth.detectScreenType()

        ModalBottomSheet(
            modifier = Modifier
                .width(sheetWidth)
                // ModalBottomSheet is centered in its own window, so shift
                // it by half of the leftover space to align it to the end.
                .offset(x = (maxWidth - sheetWidth) / 2),
            // Without this, Material's default 640dp max width would cap the sheet
            sheetMaxWidth = sheetWidth,
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
                    .fillMaxWidth()
                    .height(height)
                    .padding(
                        start = Padding,
                        end = Padding,
                        top = Padding,
                    )
                    .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))

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

                Spacer(modifier = Modifier.height(Padding))

                TraitHolderGrid(
                    items = optionalTraits.sortedBy { it.trait.type },
                    spacedByVert = MediumPadding,
                    spacedByHoriz = MediumPadding,
                    columns = screenType.columns,
                    processImageIntent = processImageIntent,
                    onClick = { }
                )
            }
        }
    }
}