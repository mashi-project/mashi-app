package com.mashiverse.mashit.utils.decoders

import coil3.PlatformContext
import com.mashiverse.mashit.app.swift.SharedCode
import com.mashiverse.mashit.data.models.colors.SelectedColors

actual suspend fun fetchOriginalSvgData(
    url: String,
    context: PlatformContext
): ByteArray? {
    return SharedCode.swiftSvgLoader.fetchOriginalSvgData(url)
}

actual suspend fun loadImageAsync(
    svgData: ByteArray,
    selectedColors: SelectedColors?,
    context: PlatformContext
): ByteArray? {
    return SharedCode.swiftSvgLoader.loadImageAsync(svgData, selectedColors)
}