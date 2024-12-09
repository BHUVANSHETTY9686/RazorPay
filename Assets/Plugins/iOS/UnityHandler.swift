import Foundation

public struct PaymentDetails: Decodable {
    let apiKey: String
    let orderId: String
    let amount: Double
    let currency: String
    let description: String
    let contact: String
    let email: String
    let themeColor: String
}

import Foundation

/// Typealias for Unity callbacks
public typealias UnityCallback = @convention(c) (UnsafePointer<CChar>?) -> Void

/// Bridge to communicate between Unity and iOS
class UnityBridge {
    static var successCallback: UnityCallback?
    static var failureCallback: UnityCallback?

    /// Register Unity success callback
    @_silgen_name("SetUnitySuccessCallback")
    public static func setUnitySuccessCallback(callback: @escaping UnityCallback) {
        successCallback = callback
    }

    /// Register Unity failure callback
    @_silgen_name("SetUnityFailureCallback")
    public static func setUnityFailureCallback(callback: @escaping UnityCallback) {
        failureCallback = callback
    }

    /// Start Razorpay payment
    @_silgen_name("SwiftUnity")
    public static func startRazorpayPayment(input: UnsafePointer<CChar>?) {
        guard let input = input, let inputString = String(validatingUTF8: input) else {
            print("Invalid or null input received from Unity.")
            return
        }

        // Decode JSON input to PaymentDetails
        if let jsonData = inputString.data(using: .utf8) {
            do {
                let paymentDetails = try JSONDecoder().decode(PaymentDetails.self, from: jsonData)
                RazorpayHandler.shared.startPayment(with: paymentDetails)
            } catch {
                print("Failed to decode JSON: \(error)")
            }
        }
    }

    /// Notify Unity about payment success
    static func notifySuccess(paymentId: String) {
        let message = "Payment Success: \(paymentId)"
        successCallback?(message.cString(using: .utf8))
    }

    /// Notify Unity about payment failure
    static func notifyFailure(errorCode: Int32, errorMessage: String) {
        let message = "Payment Error \(errorCode): \(errorMessage)"
        failureCallback?(message.cString(using: .utf8))
    }
}

