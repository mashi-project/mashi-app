import Foundation
import SDWebImage
import SDWebImageSVGCoder
import Shared
import UIKit

final class SvgImageLoader: NSObject, SvgLoader {
    
    private static let workQueue = DispatchQueue(
        label: "com.mashit.svgRecolorLoader",
        qos: .userInitiated,
        attributes: .concurrent
    )

    // MARK: - SDWebImage Cache

    private let imageCache: SDImageCache

    override init() {
        let config = SDImageCacheConfig()
        
        // 32 MB memory cache
        config.maxMemoryCost = 32 * 1024 * 1024
        
        // 32 MB disk cache
        config.maxDiskSize = 32 * 1024 * 1024

        self.imageCache = SDImageCache(
            namespace: "svg_cache",
            diskCacheDirectory: nil,
            config: config
        )

        super.init()
    }

    // MARK: - Original SVG

    func fetchOriginalSvgData(
        url: String,
        completionHandler: @escaping @Sendable (KotlinByteArray?, (any Error)?) -> Void
    ) {
        Task {
            do {
                guard let nsUrl = URL(string: url) else {
                    let error = NSError(
                        domain: "SvgImageLoader",
                        code: 400,
                        userInfo: [
                            NSLocalizedDescriptionKey: "Invalid URL string: \(url)"
                        ]
                    )
                    completionHandler(nil, error)
                    return
                }

                let cacheKey = SDWebImageManager.shared.cacheKey(for: nsUrl)

                // Check disk cache
                let cachedData: Data? = await withCheckedContinuation { continuation in
                    SvgImageLoader.workQueue.async {
                        let data = self.imageCache.diskImageData(forKey: cacheKey)
                        continuation.resume(returning: (data?.isEmpty == false) ? data : nil)
                    }
                }

                if let cachedData {
                    completionHandler(cachedData.toKotlinByteArray(), nil)
                    return
                }

                // Download
                let (fetchedData, _) = try await URLSession.shared.data(from: nsUrl)

                // Store on disk
                SvgImageLoader.workQueue.async {
                    self.imageCache.storeImageData(toDisk: fetchedData, forKey: cacheKey)
                }

                completionHandler(fetchedData.toKotlinByteArray(), nil)

            } catch {
                completionHandler(nil, error)
            }
        }
    }

    // MARK: - SVG Processing

    func loadImageAsync(
        svgData: KotlinByteArray,
        selectedColors: SelectedColors?,
        completionHandler: @escaping @Sendable (KotlinByteArray?, (any Error)?) -> Void
    ) {
        SvgImageLoader.workQueue.async {
            let resultByteArray = autoreleasepool { () -> KotlinByteArray? in
                let rawData = svgData.toData()

                guard let rawString = String(data: rawData, encoding: .utf8), !rawString.isEmpty else {
                    print("❌ SVG is not valid UTF8")
                    return nil
                }

                let sanitized = SvgSanitizer.sanitize(rawString)

                let colors = selectedColors ?? SelectedColors(
                    base: "#00FF00",
                    eyes: "#FFFF00",
                    hair: "#0000FF"
                )

                let recolored = ColorReplacer.replaceColors(
                    svgSrc: sanitized,
                    bodyColor: colors.base,
                    eyesColor: colors.eyes,
                    hairColor: colors.hair
                )

                let svgText = recolored.contains("<svg") ? recolored : sanitized

                if svgText != recolored {
                    print("⚠️ Invalid recolored SVG, falling back to sanitized original")
                }

                guard
                    let finalData = svgText.data(using: .utf8),
                    let vectorImage = SDImageSVGCoder.shared.decodedImage(with: finalData, options: nil)
                else {
                    print("❌ SVG vector decode failed")
                    return nil
                }

                var drawSize = vectorImage.size
                if drawSize.width <= 0 || drawSize.height <= 0 {
                    drawSize = CGSize(width: 512, height: 512)
                }

                let format = UIGraphicsImageRendererFormat.preferred()
                format.scale = 1.0
                format.preferredRange = .standard
                format.opaque = false

                let renderer = UIGraphicsImageRenderer(size: drawSize, format: format)
                let rasterizedImage = renderer.image { _ in
                    vectorImage.draw(in: CGRect(origin: .zero, size: drawSize))
                }

                guard let pngData = rasterizedImage.pngData() else {
                    print("❌ Failed to encode rasterized image to PNG")
                    return nil
                }

                return pngData.toKotlinByteArray()
            }

            completionHandler(resultByteArray, nil)
        }
    }
}

// MARK: - Required Type Conversion Helpers

extension Data {
    func toKotlinByteArray() -> KotlinByteArray {
        let byteArray = KotlinByteArray(size: Int32(self.count))

        self.withUnsafeBytes { bufferPointer in
            if let address = bufferPointer.baseAddress {
                let bytes = address.assumingMemoryBound(to: Int8.self)
                for i in 0..<self.count {
                    byteArray.set(index: Int32(i), value: bytes[i])
                }
            }
        }

        return byteArray
    }
}

extension KotlinByteArray {
    func toData() -> Data {
        var data = Data(count: Int(self.size))

        data.withUnsafeMutableBytes { bufferPointer in
            if let address = bufferPointer.baseAddress {
                let bytes = address.assumingMemoryBound(to: Int8.self)
                for i in 0..<self.size {
                    bytes[Int(i)] = self.get(index: i)
                }
            }
        }

        return data
    }
}
