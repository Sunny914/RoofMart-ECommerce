package com.sunny.roofmart

import android.app.Activity
import android.os.Message
import android.widget.Toast
//import com.google.api.Context
import android.content.Context // ✅ Correct import
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.razorpay.Checkout
import org.json.JSONObject
import java.time.temporal.TemporalAmount


object AppUtil {

    fun showToast(context : Context, message : String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    fun addItemToCart(productId : String, context : Context){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener{
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId]?:0
                val updatedQuantity = currentQuantity + 1

                val updatedCart = mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            showToast(context, "Item added to the cart")
                        }
                        else{
                            showToast(context, "Failed adding item to the cart")
                        }
                    }

            }
        }
    }

    fun removeFromCart(productId : String, context : Context, removeAll : Boolean = false){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener{
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId]?:0
                val updatedQuantity = currentQuantity - 1

                val updatedCart =
                    if(updatedQuantity <= 0 || removeAll){
                        mapOf("cartItems.$productId" to FieldValue.delete())
                    }else {
                        mapOf("cartItems.$productId" to updatedQuantity)
                    }

                userDoc.update(updatedCart)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            showToast(context, "Item removed form the cart")
                        }
                        else{
                            showToast(context, "Failed removing item to the cart")
                        }
                    }

            }
        }
    }


    fun getDiscountPercentage() : Float{
        return 10.0f
    }

    fun getTaxPercentage() : Float{
        return 13.0f
    }

    fun razorpayApiKey() : String{
        return "rzp_test_x7MgD8mMRaUPmD"
    }

    fun startPayment(amount: Float){
        val checkout = Checkout()
        checkout.setKeyID(razorpayApiKey())

        val options = JSONObject()
        options.put("name", "RoofMart")
        options.put("description", "")
        options.put("amount", amount *100)
        options.put("currency", "INR")

        checkout.open(GlobalNavigation.navController.context as Activity,options)

    }


}