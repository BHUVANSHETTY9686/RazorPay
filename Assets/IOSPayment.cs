#if UNITY_IOS
using UnityEngine;
using Newtonsoft.Json;
using System.Runtime.InteropServices;

public class IOSPayment
{
    [DllImport("__Internal")]
    private static extern void SetUnitySuccessCallback(OnPaymentSuccess callback);

    [DllImport("__Internal")]
    private static extern void SetUnityFailureCallback(OnPaymentFailure callback);

    [DllImport("__Internal")]
    private static extern void SwiftUnity(string input);

    public delegate void OnPaymentSuccess(string paymentId);
    public delegate void OnPaymentFailure(string errorMessage);

    public static void InitializeIOSCallbacks(OnPaymentSuccess successCallback, OnPaymentFailure failureCallback)
    {
        SetUnitySuccessCallback(successCallback);
        SetUnityFailureCallback(failureCallback);
    }

    public static void StartPayment(string jsonPaymentDetails)
    {
        SwiftUnity(jsonPaymentDetails);
    }
}
#endif