import UIKit
import Foundation
import Razorpay

/// Swift singleton that owns the Razorpay instance and its delegate.
@objc public class RazorpayHandler: NSObject {

    @objc public static let shared = RazorpayHandler()

    private var razorpay: RazorpayCheckout?
    private var onSuccess: ((String) -> Void)?
    private var onError: ((Int, String) -> Void)?

    private override init() {}

    @objc public func startPayment(
        onSuccess: @escaping (String) -> Void,
        onError: @escaping (Int, String) -> Void
    ) {
        self.onSuccess = onSuccess
        self.onError = onError

        razorpay = RazorpayCheckout.initWithKey(
            "rzp_test_uvBhuJZjsXdyn3",
            andDelegate: self
        )

        let options: [String: Any] = [
            "amount":      500 * 100,
            "currency":    "INR",
            "name":        "My App",
            "description": "Payment",
            "prefill": [
                "email": "test@gmail.com",
                "name":  "User"
            ]
        ]

        guard let vc = topViewController() else {
            onError(-1, "Could not find root view controller")
            return
        }

        razorpay?.open(options, displayController: vc)
    }

    // MARK: - Helper

    private func topViewController() -> UIViewController? {
        let scene = UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .first
        var top = scene?.windows.first(where: { $0.isKeyWindow })?.rootViewController
        while let presented = top?.presentedViewController {
            top = presented
        }
        return top
    }
}

// MARK: - RazorpayPaymentCompletionProtocol

extension RazorpayHandler: RazorpayPaymentCompletionProtocol {

    public func onPaymentSuccess(_ payment_id: String) {
        onSuccess?(payment_id)
        clear()
    }

    public func onPaymentError(_ code: Int32, description str: String) {
        onError?(Int(code), str)
        clear()
    }

    private func clear() {
        onSuccess = nil
        onError   = nil
        razorpay  = nil
    }
}
