package com.bhuvan.unity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class MyPlugin {

    private static MyPlugin instance;
    private Activity activityContext;
    private PaymentResultCallback callback;

    private MyPlugin() {}

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

    public void startPayment(String jsonPaymentDetails) {
        if (activityContext == null || callback == null) {
            Log.e("MyPlugin", "Plugin is not properly initialized!");
            return;
        }

        try {
            // Parse the received JSON
            JSONObject paymentDetails = new JSONObject(jsonPaymentDetails);
            String orderId = paymentDetails.getString("orderId");
            double amount = paymentDetails.getDouble("amount");
            String apiKey = paymentDetails.getString("apiKey");
            String currency = paymentDetails.getString("currency");
            String description = paymentDetails.getString("description");
            String contact = paymentDetails.getString("contact");
            String email = paymentDetails.getString("email");
            String themeColor = paymentDetails.getString("themeColor");

            // Proceed with Razorpay payment
            Intent intent = new Intent(activityContext, RazorpayActivity.class);
            intent.putExtra("amount", amount);
            intent.putExtra("order_id", orderId);
            intent.putExtra("api_key", apiKey);
            intent.putExtra("currency", currency);
            intent.putExtra("description", description);
            intent.putExtra("contact", contact);
            intent.putExtra("email", email);
            intent.putExtra("themeColor", themeColor);
            activityContext.startActivity(intent);
        } catch (JSONException e) {
            Log.e("MyPlugin", "Failed to parse payment details: " + e.getMessage());
        }
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
