package com.mindorks.myapplication.db


import android.net.Uri
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Product(

        var name: String? = "",
        var description: String? = "",
        var imagePathProduct:String? = ""

)