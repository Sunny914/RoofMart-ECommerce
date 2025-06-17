package com.sunny.roofmart.pages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.sunny.roofmart.components.ProductItemView
import com.sunny.roofmart.model.CategoryModel
import com.sunny.roofmart.model.ProductModel


@Composable
fun CategoryProductsPage(modifier: Modifier = Modifier, categoryId : String){

    val productslist = remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("products")
            .whereEqualTo("category", categoryId)
            .get().addOnCompleteListener() {
                if(it.isSuccessful){
                    val resultList = it.result.documents.mapNotNull { doc->
                        doc.toObject(ProductModel::class.java)
                    }
                    productslist.value = resultList
                }
            }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
                  .padding(16.dp)

    ) {
        items(productslist.value.chunked(2)){rowItems->
            Row {
                rowItems.forEach{
                    ProductItemView(product = it, modifier = Modifier.weight(1f))
                }
                if(rowItems.size == 1){
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

}