//package com.bhuvan.unity;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//
//import com.razorpay.Checkout;
//import com.razorpay.PaymentResultListener;
//
//import org.json.JSONObject;
//
//public class MyPlugin extends Activity implements PaymentResultListener {
//
//    private static MyPlugin instance;
//    private Activity activityContext;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    // Private Constructor
//    private MyPlugin() {
//        // Private to prevent direct instantiation
//    }
//
//    // Singleton Getter
//    public static MyPlugin getInstance() {
//        if (instance == null) {
//            synchronized (MyPlugin.class) {
//                if (instance == null) {
//                    instance = new MyPlugin();
//                }
//            }
//        }
//        return instance;
//    }
//
//    // Initialize the Plugin with Activity Context
//    public void initialize(Activity activity) {
//        this.activityContext = activity;
//    }
//
//    // Start Payment Method
//    public void startPayment(double amount) {
//        if (activityContext == null) {
//            Log.e("MyPlugin", "Activity context is not initialized!");
//            return;
//        }
//
//        Checkout checkout = new Checkout();
//        checkout.setKeyID("rzp_test_fVLBUKtRL0lxp3");
//
//        try {
//            // Configure Payment Options
//            JSONObject options = new JSONObject();
//            options.put("name", "Merchant Name");
//            options.put("description", "Reference No. #123456");
//            options.put("image", "http://example.com/image/rzp.jpg");
//            options.put("order_id", "cust_PT7iz3AKLL1lDu"); // Example Order ID
//            options.put("theme.color", "#3399cc");
//            options.put("currency", "INR");
//            options.put("amount", amount * 100); // Amount in subunits (e.g., 500.00 becomes 50000)
//            options.put("prefill.email", "gaurav.kumar@example.com");
//            options.put("prefill.contact", "9988776655");
//
//            JSONObject retryObj = new JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);
//
//            // Open Razorpay Checkout
//            checkout.open(activityContext, options);
//
//        } catch (Exception e) {
//            Log.e("PaymentError", "Error in starting payment: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    // Handle Payment Success
//    @Override
//    public void onPaymentSuccess(String razorpayPaymentID) {
//        Log.i("PaymentSuccess", "Payment successful: " + razorpayPaymentID);
//        showToast("Payment Successful: " + razorpayPaymentID);
//    }
//
//    // Handle Payment Error
//    @Override
//    public void onPaymentError(int code, String message) {
//        Log.e("PaymentError", "Payment failed: " + message);
//        showToast("Payment Failed: " + message);
//    }
//
//    // Show Toast Message
//    private void showToast(final String message) {
//        if (activityContext != null) {
//            activityContext.runOnUiThread(() -> Toast.makeText(activityContext, message, Toast.LENGTH_LONG).show());
//        } else {
//            Log.e("MyPlugin", "Activity context is not initialized for showing toast!");
//        }
//    }
//}
package com.bhuvan.unity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyPlugin {

    // Singleton Instance
    private static MyPlugin instance;
    private Activity activityContext;

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

    // Initialize Plugin with Activity Context
    public void initialize(Activity activity) {
        this.activityContext = activity;
    }

    // Start Payment
    public void startPayment(double amount) {
        if (activityContext == null) {
            Log.e("MyPlugin", "Activity context is not initialized!");
            // Avoid calling Toast with null context
            if (activityContext != null) {
                Toast.makeText(activityContext, "Plugin is not initialized with context.", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // Proceed with payment logic if the context is valid
        Intent intent = new Intent(activityContext, RazorpayActivity.class);
        intent.putExtra("amount", amount);
        intent.putExtra("order_id", "order_PTQ7hSJacF6AnK"); // Example Order ID
        activityContext.startActivity(intent);
    }

    // Callback for Payment Success
    public void onPaymentSuccess(String razorpayPaymentID) {
        Log.i("MyPlugin", "Payment successful: " + razorpayPaymentID);
        // Handle success (e.g., log or notify Unity, if required)
        Toast.makeText(activityContext, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_LONG).show();
    }

    // Callback for Payment Error
    public void onPaymentError(int code, String message) {
        Log.e("MyPlugin", "Payment failed: " + message);
        // Handle error (e.g., log or notify Unity, if required)
        Toast.makeText(activityContext, "Payment Failed: " + message, Toast.LENGTH_LONG).show();
    }
}

