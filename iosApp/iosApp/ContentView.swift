import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        VStack {
            ComposeView()
                .ignoresSafeArea()
            Button("Launch Payment SDK") {
                LaunchPaymentSDK_iosKt.launchPaymentSDK(
                    onSuccess: { paymentId in
                        print("Payment Success: \(paymentId)")
                    },
                    onError: { code, message in
                        print("Payment Error: \(code) - \(message)")
                    }
                )
            }
        }
    }
}



