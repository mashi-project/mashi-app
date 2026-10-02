package com.mashiverse.mashit.utils.decoders

import com.mashiverse.mashit.data.models.colors.SelectedColors

interface SvgLoader {
    suspend fun fetchOriginalSvgData(url: String): ByteArray?
    suspend fun loadImageAsync(svgData: ByteArray, selectedColors: SelectedColors?): ByteArray?
}