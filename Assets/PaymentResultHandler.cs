using UnityEngine;

#if UNITY_ANDROID
public class PaymentResultHandler : AndroidJavaProxy
{
    private System.Action<string> onSuccess;
    private System.Action<string> onError;

    public PaymentResultHandler(System.Action<string> successCallback, System.Action<string> errorCallback)
        : base("com.bhuvan.unity.PaymentResultCallback")
    {
        this.onSuccess = successCallback;
        this.onError = errorCallback;
    }

    void onPaymentSuccess(string paymentId)
    {
        Debug.Log($"Unity Payment Successful: {paymentId}");
        onSuccess?.Invoke(paymentId);
    }

    void onPaymentError(string errorMessage)
    {
        Debug.Log($"Unity Payment Failed: {errorMessage}");
        onError?.Invoke(errorMessage);
    }
}
#endif