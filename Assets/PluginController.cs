using UnityEngine;
using Newtonsoft.Json; // Make sure Newtonsoft.Json is added to your project.

#if UNITY_ANDROID
public class PluginController : MonoBehaviour
{
    const string pluginName = "com.bhuvan.unity.MyPlugin";
    static AndroidJavaClass _pluginClass;
    static AndroidJavaObject _pluginInstance;

    public static AndroidJavaClass PluginClass
    {
        get
        {
            if (_pluginClass == null)
            {
                _pluginClass = new AndroidJavaClass(pluginName);
            }
            return _pluginClass;
        }
    }

    public static AndroidJavaObject PluginInstance
    {
        get
        {
            if (_pluginInstance == null)
            {
                _pluginInstance = PluginClass.CallStatic<AndroidJavaObject>("getInstance");
            }
            return _pluginInstance;
        }
    }

    void Start()
    {
        using (AndroidJavaObject activity = GetUnityActivity())
        {
            PaymentResultHandler handler = new PaymentResultHandler(OnPaymentSuccess, OnPaymentError);
            PluginInstance.Call("initialize", activity, handler);
        }
    }

    public void StartPayment(
        string apiKey, 
        string orderId, 
        double amount, 
        string currency, 
        string description, 
        string contact, 
        string email, 
        string themeColor)
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            // Create a payment details object
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

            // Serialize payment details to JSON using Newtonsoft.Json
            string jsonPaymentDetails = JsonConvert.SerializeObject(paymentDetails);

            // Pass the JSON string to the plugin
            PluginInstance.Call("startPayment", jsonPaymentDetails);
        }
        else
        {
            Debug.LogWarning("Payment is supported only on Android.");
        }
    }

    public void Init()
    {
        StartPayment(
            apiKey: "rzp_test_pkGw5CRkJuxMwn",
            orderId: "order_PV4ViTyreJmcWH",
            amount: 500.0, // Amount in INR
            currency: "INR",
            description: "Purchase of Product X",
            contact: "9876543210",
            email: "user@example.com",
            themeColor: "#F37254"
        );
    }

    private void OnPaymentSuccess(string paymentId)
    {
        Debug.Log($"Unity Payment Successful: {paymentId}");
    }

    private void OnPaymentError(string errorMessage)
    {
        Debug.Log($"Unity Payment Failed: {errorMessage}");
    }

    private AndroidJavaObject GetUnityActivity()
    {
        using (AndroidJavaClass unityPlayerClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer"))
        {
            return unityPlayerClass.GetStatic<AndroidJavaObject>("currentActivity");
        }
    }
}
#endif