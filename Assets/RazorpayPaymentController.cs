using UnityEngine;
using Newtonsoft.Json;

public class RazorpayPaymentController : MonoBehaviour
{
    // Delegate types for success and failure callbacks
    public delegate void OnPaymentSuccess(string paymentId);
    public delegate void OnPaymentFailure(string errorMessage);

    private void Start()
    {
        // Initialize the payment system based on the platform
        InitializePaymentSystem();
    }

    public void Init()
    {
        StartPayment(
            apiKey: "rzp_test_pkGw5CRkJuxMwn",
            orderId: "order_PV6zY2wSADVaMR",
            amount: 500.0, // Amount in INR
            currency: "INR",
            description: "Purchase of Product X",
            contact: "9876543210",
            email: "user@example.com",
            themeColor: "#F37254"
        );
    }
    private void InitializePaymentSystem()
    {
#if UNITY_ANDROID
        InitializeAndroidPayment();
#elif UNITY_IOS
        InitializeIOSPayment();
#else
        Debug.LogWarning("Payment is supported only on Android and iOS.");
#endif
    }
#if UNITY_ANDROID
    private void InitializeAndroidPayment()
    {
        using (AndroidJavaObject activity = GetUnityActivity())
        {
            PaymentResultHandler handler = new PaymentResultHandler(OnPaymentSuccessHandler, OnPaymentFailureHandler);
            AndroidPayment.Initialize(activity, handler);
        }
    }
#endif
  
#if UNITY_IOS
    private void InitializeIOSPayment()
    {
        IOSPayment.InitializeIOSCallbacks(OnPaymentSuccessHandler, OnPaymentFailureHandler);
    }
#endif
    

    public void StartPayment(string apiKey, string orderId, double amount, string currency, string description, string contact, string email, string themeColor = "#F37254")
    {
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

        string jsonPaymentDetails = JsonConvert.SerializeObject(paymentDetails);

#if UNITY_ANDROID
        AndroidPayment.StartPayment(jsonPaymentDetails);
#elif UNITY_IOS
        IOSPayment.StartPayment(jsonPaymentDetails);
#else
        Debug.LogWarning("Payment is supported only on Android and iOS.");
#endif
    }
// These should be static to avoid IL2CPP marshaling issues
    [AOT.MonoPInvokeCallback(typeof(OnPaymentSuccess))]
    private static void OnPaymentSuccessHandler(string paymentId)
    {
        Debug.Log($"Payment Successful! Payment ID: {paymentId}");
    }

    [AOT.MonoPInvokeCallback(typeof(OnPaymentFailure))]
    private static void OnPaymentFailureHandler(string errorMessage)
    {
        Debug.LogError($"Payment Failed! Error: {errorMessage}");
    }
#if UNITY_ANDROID
    private AndroidJavaObject GetUnityActivity()
    {
        using (AndroidJavaClass unityPlayerClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer"))
        {
            return unityPlayerClass.GetStatic<AndroidJavaObject>("currentActivity");
        }
    }
#endif
   
}
