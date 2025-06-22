package com.sunny.roofmart

import android.app.Activity
import android.os.Message
import android.widget.Toast
//import com.google.api.Context
import android.content.Context // ✅ Correct import
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firestore.admin.v1.Index.IndexField.Order
import com.razorpay.Checkout
import com.sunny.roofmart.model.OrderModel
import org.json.JSONObject
import java.time.temporal.TemporalAmount
import java.util.UUID


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


    fun clearCartAndAddToOrders(){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener{
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()

                val order = OrderModel(
                    id = "ORD_" + UUID.randomUUID().toString().replace("-", "").take(10).uppercase(),
                    userID = FirebaseAuth.getInstance().currentUser?.uid!!,
                    date = Timestamp.now(),
                    items = currentCart,
                    status = "ORDERED",
                    address = it.result.get("address") as String
                )

                Firebase.firestore.collection("orders")
                    .document(order.id).set(order)
                    .addOnCompleteListener{
                        if(it.isSuccessful){

                            userDoc.update("cartItems", FieldValue.delete())
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