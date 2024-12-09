import Foundation
import Razorpay

/// A Singleton class to handle Razorpay payment flows
class RazorpayHandler: NSObject, RazorpayPaymentCompletionProtocol {
    
    // Singleton instance
    static let shared = RazorpayHandler()
    private var razorpay: RazorpayCheckout?
    
    private override init() {
        super.init()
    }

    /// Start the Razorpay payment flow
    /// - Parameter details: A `PaymentDetails` object containing all the necessary payment data
    func startPayment(with details: PaymentDetails) {
        // Initialize Razorpay with the API key and delegate
        razorpay = RazorpayCheckout.initWithKey(details.apiKey, andDelegate: self)

        // Prepare Razorpay payment options
        let options: [String: Any] = [
            "key": details.apiKey,
            "amount": Int(details.amount * 100),
            "currency": details.currency,
            "description": details.description,
            "order_id": details.orderId,
            "prefill": [
                "contact": details.contact,
                "email": details.email
            ],
            "theme": [
                "color": details.themeColor
            ]
        ]

        // Open the Razorpay checkout
        razorpay?.open(options)
    }

    /// Called when payment succeeds
    func onPaymentSuccess(_ payment_id: String) {
        print("Payment Success: \(payment_id)")
        UnityBridge.notifySuccess(paymentId: payment_id)
    }

    /// Called when payment fails
    func onPaymentError(_ code: Int32, description str: String) {
        print("Payment Error \(code): \(str)")
        UnityBridge.notifyFailure(errorCode: code, errorMessage: str)
    }
}
