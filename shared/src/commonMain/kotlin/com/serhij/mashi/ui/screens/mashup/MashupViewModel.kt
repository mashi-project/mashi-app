package com.serhij.mashi.ui.screens.mashup

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhij.mashi.data.local.db.entities.ImageTypeEntity
import com.serhij.mashi.data.models.colors.ColorType
import com.serhij.mashi.data.models.image.ImageType
import com.serhij.mashi.data.models.mashi.mappers.fromEntities
import com.serhij.mashi.data.models.mashup.MashupDetails
import com.serhij.mashi.data.models.mashup.MashupTrait
import com.serhij.mashi.data.models.traits.SortType
import com.serhij.mashi.data.models.traits.TraitType
import com.serhij.mashi.data.remote.MashiApi
import com.serhij.mashi.data.repos.CollectionRepo
import com.serhij.mashi.data.repos.DatastoreRepo
import com.serhij.mashi.data.repos.ImageTypeRepo
import com.serhij.mashi.data.repos.MashupRepo
import com.serhij.mashi.data.states.image.ImageIntent
import com.serhij.mashi.data.states.mashup.ActionsIntent
import com.serhij.mashi.data.states.mashup.MashupIntent
import com.serhij.mashi.data.states.mashup.MashupState
import com.serhij.mashi.data.states.mashup.MashupUiState
import com.serhij.mashi.utils.helpers.getRandomTraits
import com.serhij.mashi.utils.helpers.toHexString
import com.serhij.mashi.utils.stack.StackManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MashupViewModel(
    val collectionRepo: CollectionRepo,
    datastoreRepo: DatastoreRepo,
    private val mashitRepo: MashupRepo,
    private val imageTypeRepo: ImageTypeRepo,
    private val mashiApi: MashiApi
) : ViewModel() {
    val isLoading = mutableStateOf(false)

    var mashupUiState = mutableStateOf(MashupUiState())
        private set

    var mashupState = mutableStateOf(MashupState())
        private set

    private val stackManager = StackManager<MashupDetails>()

    private val walletFlow = datastoreRepo.walletFlow
    private val collectionFlow = collectionRepo.collectionFlow

    var isSync = mutableStateOf(false)
        private set

    init {
        observeWallet()
        observeCollection()
    }

    private fun observeWallet() {
        viewModelScope.launch(Dispatchers.IO) {
            walletFlow.distinctUntilChanged().collect { wallet ->
                if (wallet.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        mashupState.value = mashupState.value.copy(wallet = wallet)
                    }
                    val initialMashup =
                        collectionRepo.getCachedMashup(wallet) ?: MashupDetails()

                    withContext(Dispatchers.Main) {
                        mashupState.value = mashupState.value.copy(
                            mashupDetails = initialMashup,
                            colors = initialMashup.colors
                        )
                    }
                    stackManager.clear()

                    try {
                        isSync.value = true

                        val syncedMashup = collectionRepo.getMashup(wallet)
                        mashupState.value = mashupState.value.copy(
                            mashupDetails = syncedMashup,
                            colors = syncedMashup.colors
                        )
                        collectionRepo.cacheMashup(wallet = wallet, mashupDetails = syncedMashup)

                        collectionRepo.updateOwnedData("0xac73aca1205aff5dfc5e31223c33a970854057ae")
                    } catch (e: Exception) {
                        print(e.message)
                    } finally {
                        isSync.value = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        mashupState.value = mashupState.value.copy(wallet = null)
                    }
                    collectionRepo.clearOwned()
                }
            }
        }
    }

    private fun observeCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            collectionFlow.distinctUntilChanged().collect { collection ->
                withContext(Dispatchers.Main) {
                    println(collection.size)
                    mashupUiState.value = mashupUiState.value.copy(isCollectionReady = true)
                    mashupState.value = mashupState.value.copy(nfts = collection.fromEntities())
                }
            }
        }
    }

    // State
    private fun recordState() {
        stackManager.record(mashupState.value.mashupDetails)
    }

    fun onUndo() {
        stackManager.undo(mashupState.value.mashupDetails)?.let { previous ->
            mashupState.value = mashupState.value.copy(
                mashupDetails = previous,
                colors = previous.colors
            )
        }
    }

    fun onRedo() {
        stackManager.redo(mashupState.value.mashupDetails)?.let { next ->
            mashupState.value = mashupState.value.copy(
                mashupDetails = next,
                colors = next.colors
            )
        }
    }

    // Collection
    fun changeSortType(
        scope: CoroutineScope,
        vState: LazyListState,
        gState: LazyGridState,
        type: SortType
    ) {
        mashupState.value = mashupState.value.copy(sortType = type)
        scope.launch {
            vState.animateScrollToItem(0)
            gState.animateScrollToItem(0)
        }
    }

    // Mashup
    fun onReset() {
        recordState()
        mashupState.value = mashupState.value.copy(mashupDetails = MashupDetails())
    }

    fun onMashupUpdate(mashupTrait: MashupTrait) {
        val uiState = mashupState.value
        val mashupDetails = uiState.mashupDetails

        recordState()

        val trait = mashupTrait.trait
        val assets = mashupDetails.assets.toMutableList()

        val assetIndex = assets.indexOfFirst { it.type == trait.type }
        if (assetIndex != -1) {
            if (assets[assetIndex].url != trait.url) {
                assets[assetIndex] = trait
            } else {
                assets[assetIndex] = assets[assetIndex].copy(url = null)
            }
        }

        mashupState.value = uiState.copy(
            mashupDetails = mashupDetails.copy(
                assets = assets,
            )
        )
    }

    fun onRandom() {
        val uiState = mashupState.value
        if (uiState.nfts.isEmpty()) return

        recordState()

        val randomAssets = getRandomTraits(uiState.nfts)
        mashupState.value = uiState.copy(
            mashupDetails = uiState.mashupDetails.copy(
                assets = randomAssets.map { it.trait },
            )
        )
    }

    fun onSave() {
        viewModelScope.launch(Dispatchers.IO) {
            val uiState = mashupState.value
            if (uiState.wallet.isNullOrEmpty()) return@launch

            isLoading.value = true
            val res = mashitRepo.saveMashup(uiState.mashupDetails, uiState.wallet)
            if (res?.success == true) {
                val syncedMashup = collectionRepo.getMashup(uiState.wallet)
                mashupState.value = mashupState.value.copy(
                    mashupDetails = syncedMashup,
                    colors = syncedMashup.colors
                )
                collectionRepo.cacheMashup(wallet = uiState.wallet, mashupDetails = syncedMashup)
            }
            isLoading.value = false
        }
    }

    // Colors
    fun onColorsSave() {
        recordState()
        val uiState = mashupState.value

        mashupState.value = uiState.copy(
            mashupDetails = uiState.mashupDetails.copy(
                colors = uiState.colors
            )
        )
    }

    fun onColorsReset() {
        mashupState.value = mashupState.value.copy(
            colors = mashupState.value.mashupDetails.colors
        )
    }

    fun onColorTypeSelect(colorType: ColorType) {
        mashupState.value = mashupState.value.copy(selectedColorType = colorType)
    }

    fun onColorChange(color: Color) {
        val uiState = mashupState.value

        val hex = "#" + color.toHexString()
        val currentColors = uiState.colors

        mashupState.value = uiState.copy(
            colors = when (uiState.selectedColorType) {
                ColorType.BASE -> currentColors.copy(base = hex)
                ColorType.EYES -> currentColors.copy(eyes = hex)
                ColorType.HAIR -> currentColors.copy(hair = hex)
            }
        )
    }

    // Category
    fun onCategorySelect(scope: CoroutineScope, state: LazyGridState, selectedCategory: TraitType) {
        mashupState.value = mashupState.value.copy(
            selectedCategory = selectedCategory,
        )
        mashupUiState.value = mashupUiState.value.copy(
            isCollectibles = false
        )
        scope.launch { state.scrollToItem(0) }
    }

    // Images
    fun getImageType(url: String, onResult: (ImageType?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var result = imageTypeRepo.getImageType(url)?.type
                if (result == null) {
                    val type = mashiApi.getImageType(url.split("/").last())
                    imageTypeRepo.insertImageType(imageTypeEntity = ImageTypeEntity(url, type))
                    result = type
                }
                withContext(Dispatchers.Main) {
                    onResult(result)
                }
            } catch (e: Exception) {
                println("⚠ Failed to fetch image type for $url: ${e.message}")
                withContext(Dispatchers.Main) {
                    onResult(null) // Prevent crash and notify caller of failure
                }
            }
        }
    }

    // Intents
    fun processActionsIntent(intent: ActionsIntent) {
        val uiState = mashupUiState.value

        when (intent) {
            is ActionsIntent.OnColor -> mashupUiState.value =
                uiState.copy(isColorChange = true)

            is ActionsIntent.OnColorDismiss -> mashupUiState.value =
                uiState.copy(isColorChange = false)

            is ActionsIntent.OnPreview -> mashupUiState.value =
                uiState.copy(isPreview = true)

            is ActionsIntent.OnPreviewDismiss -> mashupUiState.value =
                uiState.copy(isPreview = false)

            is ActionsIntent.OnRandom -> onRandom()
            is ActionsIntent.OnSave -> onSave()
            is ActionsIntent.OnReset -> onReset()
            is ActionsIntent.OnRedo -> onRedo()
            is ActionsIntent.OnUndo -> onUndo()
        }
    }

    fun onCollectiblesSelect() {
        mashupUiState.value = mashupUiState.value.copy(
            isCollectibles = true
        )
    }

    fun onCollectibleExpand(
        position: Int,
        scope: CoroutineScope,
        state: LazyListState
    ) {
        scope.launch { state.animateScrollToItem(position) }
    }

    fun processMashupIntent(intent: MashupIntent) {
        when (intent) {
            is MashupIntent.OnCategorySelect -> onCategorySelect(
                intent.scope,
                intent.state,
                intent.selected
            )

            is MashupIntent.OnCollectibleExpand -> onCollectibleExpand(
                intent.position,
                intent.scope,
                intent.state
            )

            is MashupIntent.OnCollectiblesSelect -> onCollectiblesSelect()
            is MashupIntent.OnColorChange -> onColorChange(intent.color)
            is MashupIntent.OnColorsReset -> onColorsReset()
            is MashupIntent.OnMashupUpdate -> onMashupUpdate(intent.trait)
            is MashupIntent.OnColorsSave -> onColorsSave()
            is MashupIntent.OnColorTypeSelect -> onColorTypeSelect(intent.colorType)
        }
    }

    fun processImageIntent(intent: ImageIntent) {
        when (intent) {
            is ImageIntent.OnTypeGet -> getImageType(intent.url, intent.onResult)
        }
    }
}