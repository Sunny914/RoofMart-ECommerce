package com.sunny.roofmart.model

import com.google.firebase.Timestamp

data class OrderModel(
    val id : String = "",
    val date : Timestamp = Timestamp.now(),
    val userID : String = "",
    val items : Map<String, Long> = mapOf(),
    val status : String = "",
    val address : String = ""
)
