import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        PaymentBridge_iosKt.setPaymentHandler { onSuccess, onError in
            RazorpayHandler.shared.startPayment(
                onSuccess: { paymentId in onSuccess(paymentId) },
                onError: { code, message in onError(KotlinInt(integerLiteral: code), message) }
            )
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
