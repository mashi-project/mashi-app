package com.mashiverse.mashit.ui.screens.mashup.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo
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

    val dir = LocalLayoutDirection.current
    val insets = WindowInsets.safeDrawing.asPaddingValues()
    val endInset = insets.calculateEndPadding(dir)
    val windowWidth = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }

    val verticalInsets = WindowInsets.systemBars.only(WindowInsetsSides.Vertical)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .windowInsetsPadding(verticalInsets),
        contentAlignment = Alignment.CenterEnd
    ) {
        // maxWidth is now the full window width, so subtract the control bar first.
        // Right pane of the unfolded device = half of what is left.
        val sheetWidth = if (isHalfWidth) maxWidth / 2 else maxWidth
        val screenType = sheetWidth.detectScreenType()

        // The sheet is centered in a window as wide as the whole screen.
        // Move its right edge to the right edge of the content area.
        val offsetX = if (isHalfWidth) (windowWidth - sheetWidth) / 2 - endInset else 0.dp - endInset / 2

        ModalBottomSheet(
            modifier = Modifier
                .offset(x = offsetX)
                .width(sheetWidth),
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
                    .windowInsetsPadding(verticalInsets),
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