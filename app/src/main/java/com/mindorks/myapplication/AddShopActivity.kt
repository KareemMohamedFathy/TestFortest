package com.mindorks.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mindorks.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoilApi
class AddShopActivity : AppCompatActivity() {

    companion object {
        private const val MY_PERMISSION_CODE = 124
        private const val REQUEST_CODE_CAMERA = 1
    }


    private  var picturePath: String=""
    private lateinit var imageLocation: File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
           DetailScreen()
            }
        }
    }

    private lateinit var dialog: FileSelectorFragment;

    private fun openPicker() {
        dialog = FileSelectorFragment.newInstance()
        dialog.show(supportFragmentManager, "hayo")
    }


    private fun checkAndRequestPermissions(): Boolean {
        val permissionCAMERA =
            ContextCompat.checkSelfPermission(this@AddShopActivity, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(
            this@AddShopActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val listPermissionsNeeded = ArrayList<String>()
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this@AddShopActivity,
                listPermissionsNeeded.toTypedArray<String>(),
                MY_PERMISSION_CODE
            )
            return false
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permReqLuncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    when (requestCode) {
                        MY_PERMISSION_CODE -> {
                            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                openPicker()
                            }
                        }
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
    }
     fun onCameraClick() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    this@AddShopActivity,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile
                )
                imageLocation = photoFile
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                val intent = Intent(
                    Intent.ACTION_OPEN_DOCUMENT,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(intent, REQUEST_CODE_CAMERA)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }

      fun  onGalleryClick() {
        val intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                if (::imageLocation.isInitialized) {
                    picturePath = imageLocation.path.toString()
                } else {
                    val selectedImage = result.data!!.data
                    picturePath = selectedImage.toString()
              //      Log.d("kuso1",picturePath)
                   HelloViewModel().setPickedImage(picturePath)

                }
            }
        }
    class HelloViewModel : ViewModel() {

        private val _name:MutableLiveData<String?> = MutableLiveData(null)
        val name: LiveData<String?> = _name

    // onNameChange is an event we're defining that the UI can invoke
    // (events flow up from UI)
    fun setPickedImage(newName: String?) {
        Log.d("kuso3",newName+"hayo")
        _name.value = newName
    }
        }

    @Composable
    fun HelloScreen(helloViewModel: HelloViewModel = viewModel()) {
        // by default, viewModel() follows the Lifecycle as the Activity or Fragment
        // that calls HelloScreen(). This lifecycle can be modified by callers of HelloScreen.

        // name is the current value of [helloViewModel.name]
        // with an initial value of ""
        val pickedImage  by  helloViewModel.name.observeAsState(null)

       // DetailScreen(pickedImage = pickedImage, onNameChange = { helloViewModel.setPickedImage(pickedImage)},helloViewModel)
    }

    @ExperimentalCoilApi
    @Composable
    fun DetailScreen(
    ) {
        var pickedImage: MutableState<String?> =remember { mutableStateOf(null)}
        val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }
        var description by remember { mutableStateOf(TextFieldValue("")) }
        var name by remember { mutableStateOf(TextFieldValue("")) }

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,

            ) {
            Spacer(modifier = Modifier.padding(16.dp))
            //Log.d("kusoo",pickedImage.toString()+"s")
            Image(
                painter = pickedImage.value?.let {
                    rememberImagePainter(it)
                } ?: painterResource(id = R.drawable.ic_photo),
                "...",
                modifier = Modifier
                    .clickable(onClick = {
                        if (checkAndRequestPermissions()) {
                            //   openPicker()
                            setShowDialog(true)

                        }
                    })
                    .height(100.dp)
                    .width(100.dp)
            )
            OutlinedTextField(value =description,
                onValueChange= {description=it},
                label ={
                    Text("Plz enter your description")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter your name", color = Color.DarkGray) },
                modifier = Modifier.fillMaxWidth(),
            )
            Log.d("kuso4","${pickedImage.value} s")
            Log.d("kuso5","$picturePath s")

//            helloViewModel.setPickedImage("kuso")

            AlertDialogComponent(showDialog, setShowDialog,pickedImage)
        }
    }
@Composable
    fun AlertDialogComponent(
    openDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    pickedImage: MutableState<String?>,
) {
        if (openDialog) {


            AlertDialog(

                    onDismissRequest = { setShowDialog(false)
                if(picturePath!="") {
                //    helloViewModel.setPickedImage(picturePath)
                    pickedImage.value=picturePath
                }
                                   },
                title = { Text(text = "Alert Dialog") },
                text = { Text("Hello! I am an Alert Dialog") },

                confirmButton = {

                    TextButton(
                               onClick = {
                            val pp=picturePath
                            val value = GlobalScope.async { // creates worker thread
                                val res = withContext(Dispatchers.Default) {


                                    onGalleryClick()
                                    while(picturePath==pp){
                                        Log.d("kusog","lol")
                                        delay(500)
                                    }

                                }


                                pickedImage.value=picturePath
                               //   setPickedImage(picturePath)
                                Log.d("kuso2", picturePath + "sda")
                                setShowDialog(false)
                                }
                        }
                    ) {
                        Text(text = "Choose Gallery")


                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            setShowDialog(false)
                        }
                    ) {
                        Text("Choose Camera")

                    }
                },


                backgroundColor = Color.Black,
                contentColor = Color.White
                      ,  modifier = Modifier.size(1000.dp,2050.dp)

                    ,



            )

        }
    }



}


interface OnOptionClickListener {
    fun onCameraClick()
    fun onGalleryClick()
}
