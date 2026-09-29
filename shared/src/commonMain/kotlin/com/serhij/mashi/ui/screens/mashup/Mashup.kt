package com.serhij.mashi.ui.screens.mashup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.serhij.mashi.data.models.colors.ColorType
import com.serhij.mashi.data.models.mashi.Mashi
import com.serhij.mashi.data.models.traits.TraitType
import com.serhij.mashi.data.states.mashup.ActionsIntent
import com.serhij.mashi.ui.grid.MashupTraitHolderGrid
import com.serhij.mashi.ui.indicators.SyncIndicator
import com.serhij.mashi.ui.screens.mashup.actions.MashupActions
import com.serhij.mashi.ui.screens.mashup.categories.CategorySelector
import com.serhij.mashi.ui.screens.mashup.categories.CollectiblesCategory
import com.serhij.mashi.ui.screens.mashup.color.ColorSheet
import com.serhij.mashi.ui.screens.mashup.preview.MashupPreview
import com.serhij.mashi.ui.screens.mashup.sorting.Sorting
import com.serhij.mashi.ui.theme.MediumPadding
import com.serhij.mashi.ui.theme.Padding
import com.serhij.mashi.ui.theme.SmallPadding
import com.serhij.mashi.ui.theme.XLHolderHeight
import com.serhij.mashi.ui.theme.XLHolderWidth
import com.serhij.mashi.utils.helpers.detectScreenType
import com.serhij.mashi.utils.helpers.getTraitsByType
import com.serhij.mashi.utils.helpers.sortNfts
import com.serhij.mashi.utils.helpers.toHexColor
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mashup(searchQuery: String) {
    val viewModel = koinViewModel<MashupViewModel>()

    val searchQueryValue by remember(searchQuery) { mutableStateOf(searchQuery) }

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val colorChangingState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val previewState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val traitsGridState = rememberLazyGridState()
    val collectiblesVState = rememberLazyListState()

    var height by remember { mutableStateOf(0.dp) }
    val mashupUiState by remember { viewModel.mashupUiState }
    val mashupState by remember { viewModel.mashupState }

    val selectedColorType by remember(mashupState.selectedColorType) {
        mutableStateOf(
            mashupState.selectedColorType
        )
    }

    val currentColor = remember(
        selectedColorType,
        mashupState.colors,
    ) {
        when (selectedColorType) {
            ColorType.BASE -> mashupState.colors.base
            ColorType.EYES -> mashupState.colors.eyes
            ColorType.HAIR -> mashupState.colors.hair
        }
    }

    val previousColor = remember(
        mashupState.mashupDetails,
        selectedColorType,
    ) {
        when (selectedColorType) {
            ColorType.BASE ->
                mashupState.mashupDetails.colors.base

            ColorType.EYES ->
                mashupState.mashupDetails.colors.eyes

            ColorType.HAIR ->
                mashupState.mashupDetails.colors.hair
        }
    }

    var nfts by remember {
        mutableStateOf<List<Mashi>>(emptyList())
    }

    LaunchedEffect(mashupState.mashupDetails) {
        val selectedBackground =
            mashupState.mashupDetails.assets
                .first {
                    it.type == TraitType.BACKGROUND
                }
                .url

        val selectedNft =
            nfts.firstOrNull { nft ->
                nft.traits?.any {
                    it.url == selectedBackground
                } == true
            }
    }

    LaunchedEffect(
        mashupState.nfts,
        searchQueryValue,
    ) {
        val temp = mashupState.nfts.toList()

        nfts = if (searchQueryValue.isEmpty()) {
            temp
        } else {
            temp.filter {
                it.name.lowercase().contains(
                    searchQueryValue.lowercase()
                ) ||
                        it.author.lowercase().contains(
                            searchQueryValue.lowercase()
                        )
            }
        }
    }

    val selectedTraitUrl by remember(
        mashupState.mashupDetails,
        mashupState.selectedCategory,
    ) {
        derivedStateOf {
            mashupState.mashupDetails.assets
                .first {
                    it.type == mashupState.selectedCategory
                }
                .url
                ?: ""
        }
    }

    val sortedNfts = remember(
        mashupState.sortType,
        nfts,
    ) {
        sortNfts(
            mashupState.sortType,
            nfts,
        )
    }

    val traits by remember(
        mashupState.selectedCategory,
        sortedNfts,
    ) {
        derivedStateOf {
            val traits =
                getTraitsByType(sortedNfts)[mashupState.selectedCategory]
                    ?: emptyList()

            if (
                mashupState.selectedCategory !=
                TraitType.BACKGROUND
            ) {
                traits.distinctBy {
                    it.avatarName
                }
            } else {
                traits
            }
        }
    }

    LaunchedEffect(
        mashupUiState.isCollectibles,
    ) {
        if (mashupUiState.isCollectibles) {
            collectiblesVState.animateScrollToItem(0)
        }
    }

    val isSync by remember {
        viewModel.isSync
    }

    BoxWithConstraints {
        val screenType = maxWidth.detectScreenType()

        Column {
            if (mashupState.wallet != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Padding),
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (isSync) {
                            SyncIndicator(
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.TopStart),
                            )
                        }

                        MashupActions(
                            mashupDetails = mashupState
                                .mashupDetails
                                .copy(
                                    colors = mashupState.colors
                                ),
                            modifier = Modifier
                                .height(XLHolderHeight)
                                .width(XLHolderWidth)
                                .clickable {
                                    viewModel.processActionsIntent(
                                        ActionsIntent.OnPreview
                                    )
                                },
                            holderWidth = XLHolderWidth,
                            processImageIntent = {
                                viewModel.processImageIntent(it)
                            },
                            processActionsIntent = {
                                viewModel.processActionsIntent(it)
                            }
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(SmallPadding)
                    )

                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .onSizeChanged { size ->
                                height = with(density) {
                                    size.height.toDp()
                                } + 64.dp
                            },
                    ) {
                        if (mashupUiState.isCollectionReady) {
                            if (mashupUiState.isCollectibles) {
                                CollectiblesCategory(
                                    modifier = Modifier.weight(1f),
                                    nfts = sortedNfts,
                                    mashupDetails =
                                        mashupState.mashupDetails,
                                    state = collectiblesVState,
                                    scope = scope,
                                    processMashupIntent = {
                                        viewModel.processMashupIntent(it)
                                    },
                                    processImageIntent = {
                                        viewModel.processImageIntent(it)
                                    },
                                )
                            } else {
                                MashupTraitHolderGrid(
                                    modifier = Modifier.weight(1f),
                                    items = traits,
                                    selectedTraitUrl = selectedTraitUrl,
                                    state = traitsGridState,
                                    spacedByHoriz = MediumPadding,
                                    spacedByVert = MediumPadding,
                                    columns = screenType.columns,
                                    processImageIntent = {
                                        viewModel.processImageIntent(it)
                                    },
                                    processMashupIntent = {
                                        viewModel.processMashupIntent(it)
                                    },
                                )
                            }

                            Row(
                                modifier = Modifier.padding(
                                    vertical = SmallPadding
                                ),
                                verticalAlignment =
                                    Alignment.CenterVertically,
                            ) {
                                Sorting { type ->
                                    viewModel.changeSortType(
                                        scope = scope,
                                        vState = collectiblesVState,
                                        gState = traitsGridState,
                                        type = type,
                                    )
                                }

                                CategorySelector(
                                    mashupState = mashupState,
                                    mashupUiState = mashupUiState,
                                    processMashupIntent = {
                                        viewModel.processMashupIntent(it)
                                    },
                                    gridState = traitsGridState,
                                    scope = scope,
                                )
                            }
                        } else {

                        }
                    }
                }
            } else {
            }

            if (mashupUiState.isColorChange) {
                ColorSheet(
                    sheetState = colorChangingState,
                    initialColor = previousColor.toHexColor(),
                    color = currentColor.toHexColor(),
                    scope = scope,
                    selectedColorType = selectedColorType,
                    processMashupIntent = {
                        viewModel.processMashupIntent(it)
                    },
                    processActionsIntent = {
                        viewModel.processActionsIntent(it)
                    },
                    height = height,
                )
            }

            if (mashupUiState.isPreview) {
                MashupPreview(
                    closeBottomSheet = {
                        viewModel.processActionsIntent(
                            ActionsIntent.OnPreviewDismiss
                        )
                    },
                    sheetState = previewState,
                    mashupDetails = mashupState
                        .mashupDetails
                        .copy(
                            colors = mashupState.colors
                        ),
                    processImageIntent = {
                        viewModel.processImageIntent(it)
                    },
                    height = height,
                )
            }
        }
    }
}
