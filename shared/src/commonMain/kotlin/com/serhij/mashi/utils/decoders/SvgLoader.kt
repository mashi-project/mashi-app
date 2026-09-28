package com.serhij.mashi.utils.decoders

import com.serhij.mashi.data.models.colors.SelectedColors

interface SvgLoader {
    suspend fun fetchOriginalSvgData(url: String): ByteArray?
    suspend fun loadImageAsync(svgData: ByteArray, selectedColors: SelectedColors?): ByteArray?
}