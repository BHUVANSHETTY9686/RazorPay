package com.bhuvan.unity;

import android.app.Activity;
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

        try {
            Checkout checkout = new Checkout();
            checkout.setKeyID(getIntent().getStringExtra("api_key")); // Razorpay API Key

            JSONObject options = new JSONObject();
            options.put("name", "Merchant Name");
            options.put("description", getIntent().getStringExtra("description"));
            options.put("image", "http://example.com/image/rzp.jpg");
            options.put("order_id", getIntent().getStringExtra("order_id")); // Order ID passed from Unity
            options.put("theme.color", getIntent().getStringExtra("themeColor"));
            options.put("currency", getIntent().getStringExtra("currency"));
            options.put("amount", getIntent().getDoubleExtra("amount", 0) * 100); // Amount in subunits
            options.put("prefill.email", getIntent().getStringExtra("email"));
            options.put("prefill.contact", getIntent().getStringExtra("contact"));

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
