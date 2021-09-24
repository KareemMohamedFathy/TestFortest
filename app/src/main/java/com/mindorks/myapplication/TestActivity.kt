package com.mindorks.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.android.volley.Header
import com.mindorks.myapplication.ui.theme.MyApplicationTheme

class TestActivity : AppCompatActivity() {
private var  arr: List<Blog> = ArrayList()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        val book1 = Blog("hi",null)
        val book2 = Blog("","https://firebasestorage.googleapis.com/v0/b/freshproducts-cb620.appspot.com/o/images%2F00b2a608-fe88-4e04-a306-8e758b9ec41b.jpg?alt=media&token=02f6d6fe-7c39-4c4a-8692-6f14a70ac1e4")

        arr= listOf<Blog>(book1,book2)

        var b=Blog("hi","ny")
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Lelouch(arr)
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @ExperimentalFoundationApi
    @Composable
    private fun Lelouch(arr:List<Blog>) {
      LazyColumn(

          verticalArrangement = Arrangement.spacedBy(8.dp),
          horizontalAlignment = Alignment.CenterHorizontally
          ) {
          stickyHeader(key="55") {
              Header("zs","ss")
          }
          itemsIndexed(items=arr) {index,item->
              if(item.mes!=""){
                  Text(text = "Eren Yeager is the best %item.mes",
                  modifier = Modifier.fillMaxWidth()
                  ,
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold,
                      textAlign = TextAlign.Center
                  )

              }
              else {
                  Image(
                          rememberImagePainter(item.image,
                                  builder = {
                              placeholder(R.drawable.ic_photo) // or bi
                                  crossfade(1000)

                                  // tmap
                          }
                          )
                              ,"...",
                              modifier = Modifier
                     .width(100.dp)
                                  .height(100.dp)
                                  .defaultMinSize(100.dp,100.dp)

                      ,

                      contentScale = ContentScale.FillBounds
                      ,


                      alignment = Alignment.Center,

                  )
              }
          }


      }
    }

}
data class Blog(
     var mes:String="",
     var image:String?
)