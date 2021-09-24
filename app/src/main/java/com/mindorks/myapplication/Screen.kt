package com.mindorks.myapplication

import android.util.Log

sealed class Screen(val router:String){
    object Register:Screen("Register")
    object DetailScreen:Screen("detail_screen")
    fun withArgs(vararg  args:String):String{
        return buildString {
            Log.d("kuso",router)

            append(router)
            args.forEach { arg->
                append("/$arg")
                Log.d("kuso",arg)

            }
        }
    }

}
