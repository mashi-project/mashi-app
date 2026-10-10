package com.mashiverse.mashit.utils.helpers

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
actual fun rememberDiscordInviteOpener(): (String) -> Unit {
    val context = LocalContext.current
    return remember(context) {
        { url: String ->
            val uri = url.toUri()
            val discordIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.discord")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(discordIntent)
            } catch (e: ActivityNotFoundException) {
                // Discord not installed, open in browser
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }
}