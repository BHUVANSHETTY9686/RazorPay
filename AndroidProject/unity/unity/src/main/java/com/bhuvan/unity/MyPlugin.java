package com.bhuvan.unity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyPlugin {

    // Singleton Instance
    private static MyPlugin instance;
    private Activity activityContext;
    private PaymentResultCallback callback;


    // Private Constructor
    private MyPlugin() {}

    // Singleton Getter
    public static MyPlugin getInstance() {
        if (instance == null) {
            synchronized (MyPlugin.class) {
                if (instance == null) {
                    instance = new MyPlugin();
                }
            }
        }
        return instance;
    }

    public void initialize(Activity activity, PaymentResultCallback callback) {
        this.activityContext = activity;
        this.callback = callback;
    }

    // Start Payment
    public void startPayment(double amount) {
        if (activityContext == null || callback == null) {
            Log.e("MyPlugin", "Plugin is not properly initialized!");
            return;
        }

        // Proceed with payment logic if the context is valid
        Intent intent = new Intent(activityContext, RazorpayActivity.class);
        intent.putExtra("amount", amount);
        intent.putExtra("order_id", "order_PTQ7hSJacF6AnK"); // Example Order ID
        activityContext.startActivity(intent);
    }
    public void notifyPaymentSuccess(String paymentId) {
        if (callback != null) {
            callback.onPaymentSuccess(paymentId);
        }
    }

    public void notifyPaymentError(String errorMessage) {
        if (callback != null) {
            callback.onPaymentError(errorMessage);
        }
    }

}