using UnityEngine;
using System.Runtime.InteropServices;
using Newtonsoft.Json;

public class RazorpayManager : MonoBehaviour
{
#if UNITY_IOS
    [DllImport("__Internal")]
    private static extern void SetUnitySuccessCallback(OnPaymentSuccess callback);

    [DllImport("__Internal")]
    private static extern void SetUnityFailureCallback(OnPaymentFailure callback);

    [DllImport("__Internal")]
    private static extern void SwiftUnity(string input);
#endif

    // Delegate types
    public delegate void OnPaymentSuccess(string paymentId);
    public delegate void OnPaymentFailure(string errorMessage);

    private void Start()
    {
#if UNITY_IOS
        // Register separate callbacks for success and failure
        SetUnitySuccessCallback(OnPaymentSuccessHandler);
        SetUnityFailureCallback(OnPaymentFailureHandler);
#endif
    }
#if UNITY_IOS
    public void Init()
    {
        
        StartPayment(
            apiKey: "rzp_test_pkGw5CRkJuxMwn",
            orderId: "order_PV3QmlXOx0MoGx",
            amount: "10000", // 100.00 INR (amount is in paise)
            currency: "INR",
            description: "Purchase of product X",
            contact: "9876543210",
            email: "user@example.com",
            themeColor: "#F37254"
        );
    }
#endif

    public void StartPayment(
        string apiKey,
        string orderId,
        string amount,
        string currency,
        string description,
        string contact,
        string email,
        string themeColor = "#F37254")
    {
#if UNITY_IOS
        // Prepare payment details as JSON
        var paymentDetails = new
        {
            apiKey = apiKey,
            orderId = orderId,
            amount = amount,
            currency = currency,
            description = description,
            contact = contact,
            email = email,
            themeColor = themeColor
        };

        // Serialize payment details to JSON
        string jsonString = JsonConvert.SerializeObject(paymentDetails);

        // Pass JSON data to iOS
        SwiftUnity(jsonString);
#endif
    }

    // Success callback
    [AOT.MonoPInvokeCallback(typeof(OnPaymentSuccess))]
    private static void OnPaymentSuccessHandler(string paymentId)
    {
        Debug.Log("Payment Successful! Payment ID: " + paymentId);
        // Handle success (e.g., unlock features, update UI)
    }

    // Failure callback
    [AOT.MonoPInvokeCallback(typeof(OnPaymentFailure))]
    private static void OnPaymentFailureHandler(string errorMessage)
    {
        Debug.LogError("Payment Failed! Error: " + errorMessage);
        // Handle failure (e.g., show error message, retry option)
    }
}
