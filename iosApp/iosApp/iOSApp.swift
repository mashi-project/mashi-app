import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        KoinInitIosKt.doInitKoinIos()
        SharedCode.shared.doInit(swiftSvgLoader: SvgImageLoader())
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    SupabaseHandlerIosKt.handleDeeplinks(url: url)
                }
        }
    }
}
