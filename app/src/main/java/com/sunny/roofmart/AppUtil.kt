package com.sunny.roofmart

import android.os.Message
import android.widget.Toast
//import com.google.api.Context
import android.content.Context // ✅ Correct import


object AppUtil {

    fun showToast(context : Context, message : String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}