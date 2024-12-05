using UnityEngine;

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
        // Initialize MyPlugin with the Activity context before using it
        using (AndroidJavaObject activity = GetUnityActivity())
        {
            PluginInstance.Call("initialize", activity);
        }
    }

    public void StartPayment(double amount)
    {
        if (Application.platform == RuntimePlatform.Android)
        {
            PluginInstance.Call("startPayment", amount);
        }
        else
        {
            Debug.LogWarning("Payment is supported only on Android.");
        }
    }

    // Helper method to get the Unity activity
    private AndroidJavaObject GetUnityActivity()
    {
        using (AndroidJavaClass unityPlayerClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer"))
        {
            return unityPlayerClass.GetStatic<AndroidJavaObject>("currentActivity");
        }
    }
    public void Init()
    {
        PluginController pluginController = FindObjectOfType<PluginController>();
        pluginController.StartPayment(500.0); // Amount in INR
    }

}