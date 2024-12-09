#if UNITY_ANDROID
using UnityEngine;
using Newtonsoft.Json;

public class AndroidPayment
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

    public static void Initialize(AndroidJavaObject activity, PaymentResultHandler handler)
    {
        PluginInstance.Call("initialize", activity, handler);
    }

    public static void StartPayment(string jsonPaymentDetails)
    {
        PluginInstance.Call("startPayment", jsonPaymentDetails);
    }
}
#endif