package com.sunny.roofmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.razorpay.PaymentResultListener
import com.sunny.roofmart.ui.theme.RoofMartTheme

class MainActivity : ComponentActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoofMartTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  AppNavigation(Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        AppUtil.showToast(this, "Payment Success !!")

    }

    override fun onPaymentError(p0: Int, p1: String?) {
       AppUtil.showToast(this, "Payment failed !!!")
    }
}
