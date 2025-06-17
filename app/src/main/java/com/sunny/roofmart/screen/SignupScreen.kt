package com.sunny.roofmart.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sunny.roofmart.AppUtil
import com.sunny.roofmart.R
import com.sunny.roofmart.viewmodel.AuthViewModel

@Composable
fun SignupScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel = viewModel()){

    var email by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }

    var context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()
        .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello Jaaneman!",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 25.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                //textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Create an Account",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 22.sp,

            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(painter = painterResource(id = R.drawable.login),
            contentDescription = "Banner",
            modifier = modifier.fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(value = email, onValueChange = {
            email = it
        },
            label = {
                Text(text = "Email address")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(value = name, onValueChange = {
            name = it
        },
            label = {
                Text(text = "full name")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(value = password, onValueChange = {
            password = it
        },
            label = {
                Text(text = "Create Password")
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))


        Button(onClick = {
            isLoading = true
           authViewModel.signup(email, name, password){success, errorMessage->
               if(success){
                   isLoading = false
                    navController.navigate("home"){
                        popUpTo("auth") {inclusive = true}
                    }
               }else{
                   isLoading = false
                    AppUtil.showToast(context, errorMessage?:"Something went wrong")
               }
           }
        },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(text = if(isLoading)"Creating Account " else "Signup", fontSize = 22.sp)
        }



    }
}