package com.sunny.roofmart.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.sunny.roofmart.AppUtil
import com.sunny.roofmart.model.ProductModel
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun ProductDetailsPage(modifier: Modifier = Modifier, productId : String){

    var product by remember {
        mutableStateOf(ProductModel())
    }

    var context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("products")
            .document(productId).get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    var result = it.result.toObject(ProductModel::class.java)
                    if(result != null){
                        product = result
                    }
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = product.title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
            )

        Spacer(modifier = Modifier.height(10.dp))

        Column() {
            val pagerState = rememberPagerState(0) {
                product.image.size
            }
            HorizontalPager(state = pagerState, pageSpacing = 28.dp) {
                AsyncImage(
                    model = product.image.get(it),
                    contentDescription = "Product images",
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .height(220.dp)

                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            DotsIndicator(
                dotCount = product.image.size ,
                type =  ShiftIndicatorType(
                    DotGraphic(
                    color = MaterialTheme.colorScheme.primary,
                    size = 6.dp
                )
                ),
                pagerState = pagerState
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$" + product.price,
                fontSize = 18.sp,
                style = TextStyle(textDecoration = TextDecoration.LineThrough)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$" + product.actualPrice,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Add to Favourite"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                 AppUtil.addItemToCart(productId, context)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(text = "Add to Cart", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Product description : ",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = product.description, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Other Product description : ",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        product.otherDetails.forEach{(key, value)->
            Row(
                modifier = Modifier.fillMaxWidth()
                           .padding(4.dp)
            ){
                Text(text = "$key : ", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(text = value, fontSize = 16.sp)
            }
        }

    }

}