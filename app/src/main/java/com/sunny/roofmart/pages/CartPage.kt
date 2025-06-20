package com.sunny.roofmart.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.sunny.roofmart.AppUtil
import com.sunny.roofmart.GlobalNavigation
import com.sunny.roofmart.components.CartItemView
import com.sunny.roofmart.model.UserModel

@Composable
fun CartPage(modifier: Modifier = Modifier){

    val userModel = remember {
        mutableStateOf(UserModel())
    }

     DisposableEffect(key1 = Unit) {
         var listner = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addSnapshotListener{it, _ ->
                if(it != null){
                    val result = it.toObject(UserModel::class.java)
                    if(result != null){
                        userModel.value = result
                    }
                }
            }

         onDispose {
             listner.remove()
         }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your Cart", style = TextStyle(
              fontSize = 29.sp,
              fontWeight = FontWeight.SemiBold
            )
        )

    LazyColumn(
        modifier = Modifier.weight(1f)
    ) {
        items(userModel.value.cartItems.toList(), key = {it.first}){(productId, qty)->
            CartItemView(productId = productId, qty = qty)
        }

    }

        Button(
            onClick = {
                GlobalNavigation.navController.navigate("checkout")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height( 50.dp)


        ) {
            Text(text = "Checkout")
        }

        Spacer(modifier = Modifier.height(80.dp))

        Text(text = ".....")
    }





}