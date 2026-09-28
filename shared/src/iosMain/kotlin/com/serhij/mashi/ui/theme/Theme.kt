package com.serhij.mashi.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.setStatusBarStyle

@Composable
actual fun PlatformSystemBarEffect() {
    SideEffect {
        UIApplication.sharedApplication.setStatusBarStyle(
            UIStatusBarStyleDarkContent,
            animated = true
        )
    }
}