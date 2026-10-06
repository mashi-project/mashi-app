package com.mashiverse.mashit.ui.screens.mashup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.colors.ColorType
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.screen.ScreenInfo
import com.mashiverse.mashit.data.models.traits.TraitType
import com.mashiverse.mashit.data.states.mashup.ActionsIntent
import com.mashiverse.mashit.ui.availability.NotFound
import com.mashiverse.mashit.ui.grid.MashupTraitHolderGrid
import com.mashiverse.mashit.ui.indicators.SyncIndicator
import com.mashiverse.mashit.ui.screens.mashup.actions.MashupActions
import com.mashiverse.mashit.ui.screens.mashup.categories.CategorySelector
import com.mashiverse.mashit.ui.screens.mashup.categories.CollectiblesCategory
import com.mashiverse.mashit.ui.screens.mashup.color.ColorSheet
import com.mashiverse.mashit.ui.screens.mashup.dialog.GenerateDialog
import com.mashiverse.mashit.ui.screens.mashup.preview.MashupPreview
import com.mashiverse.mashit.ui.screens.mashup.sorting.Sorting
import com.mashiverse.mashit.ui.theme.MediumPadding
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.ui.theme.XLHolderHeight
import com.mashiverse.mashit.ui.theme.XLHolderWidth
import com.mashiverse.mashit.utils.decoders.AnimationGate
import com.mashiverse.mashit.utils.helpers.detectScreenType
import com.mashiverse.mashit.utils.helpers.getTraitsByType
import com.mashiverse.mashit.utils.helpers.rememberIsOpen
import com.mashiverse.mashit.utils.helpers.sortNfts
import com.mashiverse.mashit.utils.helpers.toHexColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mashup(searchQuery: String, isApprovalTeam: Boolean = false) {
    val viewModel = koinViewModel<MashupViewModel>()
    val isLoading by remember { viewModel.isLoading }
    val isGenerate by remember { viewModel.isGenerateDialog }
    val isDiscord by viewModel.isDiscord.collectAsState(false)
    val isOpen by rememberIsOpen()

    if (isApprovalTeam) {
        viewModel.loadForApprovalTeam()
    }

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val colorChangingState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val previewState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val traitsGridState = rememberLazyGridState()
    val collectiblesVState = rememberLazyListState()

    var height by remember { mutableStateOf(0.dp) }
    val mashupUiState by remember { viewModel.mashupUiState }
    val mashupState by remember { viewModel.mashupState }

    val selectedColorType = mashupState.selectedColorType

    // Drive the animation gate from a snapshotFlow so scrolling start/stop
    // does NOT recompose this whole screen.
    val isCollectibles = mashupUiState.isCollectibles
    LaunchedEffect(isCollectibles) {
        snapshotFlow {
            if (isCollectibles) collectiblesVState.isScrollInProgress
            else traitsGridState.isScrollInProgress
        }
            .distinctUntilChanged()
            .collectLatest { scrolling ->
                if (scrolling) {
                    AnimationGate.paused = true
                } else {
                    delay(120.milliseconds) // avoids flicker between fling segments
                    AnimationGate.paused = false
                }
            }
    }

    // Don't leave animations frozen if the screen is left mid-scroll
    DisposableEffect(Unit) {
        onDispose { AnimationGate.paused = false }
    }

    val currentColor = remember(selectedColorType, mashupState.colors) {
        when (selectedColorType) {
            ColorType.BASE -> mashupState.colors.base
            ColorType.EYES -> mashupState.colors.eyes
            ColorType.HAIR -> mashupState.colors.hair
        }
    }

    val previousColor = remember(mashupState.mashupDetails, selectedColorType) {
        when (selectedColorType) {
            ColorType.BASE -> mashupState.mashupDetails.colors.base
            ColorType.EYES -> mashupState.mashupDetails.colors.eyes
            ColorType.HAIR -> mashupState.mashupDetails.colors.hair
        }
    }

    // Created once per change instead of on every recomposition.
    val mashupDetailsWithColors = remember(mashupState.mashupDetails, mashupState.colors) {
        mashupState.mashupDetails.copy(colors = mashupState.colors)
    }

    val nfts by remember(mashupState.nfts, searchQuery) {
        derivedStateOf {
            val q = searchQuery.trim().lowercase()
            if (q.isEmpty()) {
                mashupState.nfts
            } else {
                mashupState.nfts.filter {
                    it.name.contains(q, ignoreCase = true) ||
                            it.author.contains(q, ignoreCase = true)
                }
            }
        }
    }

    val selectedTraitUrl by remember(
        mashupState.mashupDetails,
        mashupState.selectedCategory,
    ) {
        derivedStateOf {
            mashupState.mashupDetails.assets
                .first { it.type == mashupState.selectedCategory }
                .url
                ?: ""
        }
    }

    val sortedNfts = remember(mashupState.sortType, nfts) {
        sortNfts(mashupState.sortType, nfts)
    }

    // Group once per list change, not on every category switch.
    val traitsByType = remember(sortedNfts) { getTraitsByType(sortedNfts) }

    val traits = remember(traitsByType, mashupState.selectedCategory) {
        val list = traitsByType[mashupState.selectedCategory] ?: emptyList()
        if (mashupState.selectedCategory != TraitType.BACKGROUND) {
            list.distinctBy { it.avatarName }
        } else {
            list
        }
    }

    LaunchedEffect(mashupUiState.isCollectibles) {
        if (mashupUiState.isCollectibles) {
            collectiblesVState.animateScrollToItem(0)
        }
    }

    val isSync by remember { viewModel.isSync }

    BoxWithConstraints {
        val screenType = maxWidth.detectScreenType()

        Column {
            if (mashupState.wallet != null) {
                if (!isOpen) {
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
                                mashupDetails = mashupDetailsWithColors,
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
                                    } + 80.dp
                                },
                        ) {
                            if (mashupUiState.isCollectionReady) {
                                if (sortedNfts.isEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        NotFound(modifier = Modifier.fillMaxSize())
                                    }
                                } else {
                                    if (mashupUiState.isCollectibles) {
                                        CollectiblesCategory(
                                            modifier = Modifier.weight(1f),
                                            nfts = sortedNfts,
                                            mashupDetails = mashupState.mashupDetails,
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
                                }

                                Row(
                                    modifier = Modifier.padding(vertical = SmallPadding),
                                    verticalAlignment = Alignment.CenterVertically,
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
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = Padding)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1F)
                                .fillMaxHeight()
                        ) {
                            if (isSync) {
                                SyncIndicator(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .align(Alignment.TopStart),
                                )
                            }

                            Box(modifier = Modifier.align(Alignment.Center)) {
                                MashupActions(
                                    mashupDetails = mashupDetailsWithColors,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
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
                        }

                        Column(
                            modifier = Modifier
                                .weight(1F)
                                .wrapContentHeight()
                                .onSizeChanged { size ->
                                    height = with(density) {
                                        size.height.toDp()
                                    } + 80.dp
                                },
                        ) {
                            if (mashupUiState.isCollectionReady) {
                                if (sortedNfts.isEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        NotFound(modifier = Modifier.fillMaxSize())
                                    }
                                } else {
                                    if (mashupUiState.isCollectibles) {
                                        CollectiblesCategory(
                                            modifier = Modifier.weight(1f),
                                            nfts = sortedNfts,
                                            mashupDetails = mashupState.mashupDetails,
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
                                            columns = ScreenInfo.COMPACT.columns,
                                            processImageIntent = {
                                                viewModel.processImageIntent(it)
                                            },
                                            processMashupIntent = {
                                                viewModel.processMashupIntent(it)
                                            },
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.padding(vertical = SmallPadding),
                                    verticalAlignment = Alignment.CenterVertically,
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
                            }
                        }
                    }
                }
            } else {
                // Wallet not connected state placeholder if needed
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
                    mashupDetails = mashupDetailsWithColors,
                    processImageIntent = {
                        viewModel.processImageIntent(it)
                    },
                    height = height,
                )
            }
        }
    }

    if (isGenerate) {
        GenerateDialog(
            isDiscord = isDiscord,
            onDismiss = { viewModel.closeDialog() },
            onGenerate = { discord: Boolean, type: ImageType ->
                viewModel.sendGenerationRequest(discord = discord, imageType = type)
                viewModel.closeDialog()
            }
        )
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3F)),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
    }
}