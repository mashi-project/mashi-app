package com.serhij.mashi.ui.availability

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.serhij.mashi.ui.theme.ContentAccentColor

@Composable
fun NotFound(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text("No items found", color = ContentAccentColor)
    }
}