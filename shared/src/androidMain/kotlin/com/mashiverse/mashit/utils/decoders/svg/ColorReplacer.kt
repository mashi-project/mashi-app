package com.mashiverse.mashit.utils.decoders.svg

object ColorReplacer {

    fun replaceColors(
        svgSrc: String,
        bodyColor: String,
        eyesColor: String,
        hairColor: String
    ): String {
        // Pattern for Green (#00FF00, #0F0, lime, rgb(0,255,0))
        val bodyPattern =
            "(?i)#00ff00|#0f0\\b|\\blime\\b|rgb\\s*\\(\\s*0\\s*,\\s*255\\s*,\\s*0\\s*\\)"

        // Pattern for Yellow (#FFFF00, #FF0, yellow, rgb(255,255,0))
        val eyesPattern =
            "(?i)#ffff00|#ff0\\b|\\byellow\\b|rgb\\s*\\(\\s*255\\s*,\\s*255\\s*,\\s*0\\s*\\)"

        // Pattern for Blue (#0000FF, #00F, blue, rgb(0,0,255))
        val hairPattern =
            "(?i)#0000ff|#00f\\b|\\bblue\\b|rgb\\s*\\(\\s*0\\s*,\\s*0\\s*,\\s*255\\s*\\)"

        return svgSrc
            .replace(Regex(bodyPattern), bodyColor)
            .replace(Regex(eyesPattern), eyesColor)
            .replace(Regex(hairPattern), hairColor)
    }
}