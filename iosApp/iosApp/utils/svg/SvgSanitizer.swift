import Foundation

enum SvgSanitizer {
    static func sanitize(_ rawString: String) -> String {
        let trimmed = rawString.trimmingCharacters(in: .whitespacesAndNewlines)

        if let xmlRange = trimmed.range(of: "<?xml"), xmlRange.lowerBound != trimmed.startIndex {
            return String(trimmed[xmlRange.lowerBound...])
        }

        if let svgRange = trimmed.range(of: "<svg", options: .caseInsensitive), svgRange.lowerBound != trimmed.startIndex {
            return String(trimmed[svgRange.lowerBound...])
        }

        return trimmed
    }
}