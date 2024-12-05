package com.bhuvan.unity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class RazorpayActivity extends Activity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start Razorpay Checkout
        try {
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_7nwIbkoTeDMauq");

            JSONObject options = new JSONObject();
            options.put("name", "Merchant Name");
            options.put("description", "Reference No. #123456");
            options.put("image", "http://example.com/image/rzp.jpg");
            options.put("order_id", getIntent().getStringExtra("order_id")); // Order ID passed from Unity
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", getIntent().getDoubleExtra("amount", 0) * 100); // Amount in subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact", "9988776655");

            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(this, options);
        } catch (Exception e) {
            Log.e("PaymentError", "Error in starting payment: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Payment initialization failed.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Log.i("PaymentSuccess", "Payment successful: " + razorpayPaymentID);
        Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_LONG).show();

        // Notify the Plugin
        MyPlugin.getInstance().notifyPaymentSuccess(razorpayPaymentID);

        finish();
    }

    @Override
    public void onPaymentError(int code, String message) {
        Log.e("PaymentError", "Payment failed: " + message);
        Toast.makeText(this, "Payment Failed: " + message, Toast.LENGTH_LONG).show();

        // Notify the Plugin
        MyPlugin.getInstance().notifyPaymentError(message);

        finish();
    }

}
