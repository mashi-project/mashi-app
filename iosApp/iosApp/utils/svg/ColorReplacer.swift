import Foundation

struct ColorReplacer {

    static func replaceColors(
        svgSrc: String,
        bodyColor: String,
        eyesColor: String,
        hairColor: String
    ) -> String {

        // Pattern for Green (#00FF00, #0F0, lime, rgb(0,255,0))
        let bodyPattern = "#00ff00|#0f0\\b|\\blime\\b|rgb\\s*\\(\\s*0\\s*,\\s*255\\s*,\\s*0\\s*\\)"

        // Pattern for Yellow (#FFFF00, #FF0, yellow, rgb(255,255,0))
        let eyesPattern = "#ffff00|#ff0\\b|\\byellow\\b|rgb\\s*\\(\\s*255\\s*,\\s*255\\s*,\\s*0\\s*\\)"

        // Pattern for Blue (#0000FF, #00F, blue, rgb(0,0,255))
        let hairPattern = "#0000ff|#00f\\b|\\bblue\\b|rgb\\s*\\(\\s*0\\s*,\\s*0\\s*,\\s*255\\s*\\)"

        var svgStr = svgSrc

        svgStr = svgStr.replacingOccurrences(
            of: bodyPattern,
            with: bodyColor,
            options: [.regularExpression, .caseInsensitive]
        )

        svgStr = svgStr.replacingOccurrences(
            of: eyesPattern,
            with: eyesColor,
            options: [.regularExpression, .caseInsensitive]
        )

        svgStr = svgStr.replacingOccurrences(
            of: hairPattern,
            with: hairColor,
            options: [.regularExpression, .caseInsensitive]
        )

        return svgStr
    }
}