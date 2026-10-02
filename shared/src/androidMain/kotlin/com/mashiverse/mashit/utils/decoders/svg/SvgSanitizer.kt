package com.mashiverse.mashit.utils.decoders.svg

object SvgSanitizer {

    fun sanitize(rawString: String): String {
        val trimmed = rawString.trim()

        val xmlIndex = trimmed.indexOf("<?xml")
        if (xmlIndex > 0) {
            return trimmed.substring(xmlIndex)
        }

        val svgIndex = trimmed.indexOf("<svg", ignoreCase = true)
        if (svgIndex > 0) {
            return trimmed.substring(svgIndex)
        }

        return trimmed
    }
}