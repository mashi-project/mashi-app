import UIKit
import SwiftUI
import Shared

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let vc = MainViewControllerKt.MainViewController()
        if #available(iOS 27.1, *) {
            let interaction = UIHingeInteraction { _, update in
                guard let hinge = update.hinge else {
                    FoldBridge.shared.update(status: -1)
                    return
                }
                switch hinge.status {
                case .closed: FoldBridge.shared.update(status: 0)
                case .partiallyOpen: FoldBridge.shared.update(status: 1)
                case .fullyOpen: FoldBridge.shared.update(status: 2)
                @unknown default: FoldBridge.shared.update(status: -1)
                }
            }
            vc.view.addInteraction(interaction)
        }
        return vc
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Self.Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}