package com.mashiverse.mashit.app.swift

import com.mashiverse.mashit.utils.decoders.SvgLoader

object SharedCode {
    lateinit var swiftSvgLoader: SvgLoader
    fun init(swiftSvgLoader: SvgLoader) {
        this.swiftSvgLoader = swiftSvgLoader
    }
}