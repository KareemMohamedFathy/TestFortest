package com.mindorks.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mindorks.myapplication.db.User

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(requireActivity()).apply {
            setContent {
                MaterialTheme {
                    Register()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    @Preview
    @Composable
    fun Register() {
        var email by remember { mutableStateOf(TextFieldValue("")) }

        var name by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
            ,horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top



        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value =email,
                onValueChange= {email=it},
                label ={Text("Plz enter your email")
                },
                modifier = Modifier.fillMaxWidth()



            )
            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(value =name,
                onValueChange= {name=it},


                label ={Text("Enter your name",color = Color.DarkGray)},
                modifier = Modifier.fillMaxWidth(),


                )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value =password,
                onValueChange= {password=it},
                label ={Text("Enter your password")},
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                          handleAuth(name.text,password.text,email.text)
               val intent= Intent(requireActivity(),AddShopActivity::class.java)
                    startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                border = BorderStroke(width = 1.dp, brush = SolidColor(Color.Green)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
            ) {
                Text(text = "CLick Me!",  textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight= FontWeight.Bold
                )
            }
        }
    }

    private fun handleAuth(name: String, password: String, email: String) {
           val auth= FirebaseAuth.getInstance()
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                                if(task.isSuccessful){



                                    val firebaseDatabase= FirebaseDatabase.getInstance()


                                    val  dbReference=firebaseDatabase.getReference("User")
                                    val id=auth.currentUser!!.uid

                                    val user= User(id,name,email)
                                    dbReference.child(id).setValue(user)



                                }
                                else{
                                    Log.d("kuso","kuso")
                                    Log.d("kuso",task.result.toString())

                                }


                            })


                      //  navController.navigate(Screen.DetailScreen.withArgs(name.text,email.text))

    }

}
