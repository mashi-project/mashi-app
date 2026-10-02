package com.mashiverse.mashit.data.states.mashup

sealed class ActionsIntent {

    object OnColor : ActionsIntent()

    object OnColorDismiss : ActionsIntent()

    object OnRandom : ActionsIntent()

    object OnSave : ActionsIntent()

    object OnReset : ActionsIntent()

    object OnUndo : ActionsIntent()

    object OnRedo : ActionsIntent()

    object OnPreview : ActionsIntent()

    object OnPreviewDismiss : ActionsIntent()

    object OnGenerate : ActionsIntent()
}