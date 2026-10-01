package com.serhij.mashi.app.swift

import com.serhij.mashi.utils.decoders.SvgLoader

object SharedCode {
    lateinit var swiftSvgLoader: SvgLoader
    fun init(swiftSvgLoader: SvgLoader) {
        this.swiftSvgLoader = swiftSvgLoader
    }
}