package com.example.medlife_final

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.google.firebase.firestore.FirebaseFirestore
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_camera.*

var mResultEt: EditText? = null
var mPreviewIv: ImageView? = null

private const val CAMERA_REQUEST_CODE = 200
private const val STORAGE_REQUEST_CODE = 400
private const val IMAGE_PICK_GALLERY_CODE = 1000
private const val IMAGE_PICK_CAMERA_CODE = 1001

var cameraPermission = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

var storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
var image_uri: Uri? = null

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        val actionBar = supportActionBar

        mResultEt = findViewById(R.id.resultEt)
        mPreviewIv = findViewById(R.id.imageIv)
        cameraPermission = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        storagePermission =
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        showImageImportDialog()
    }
    //menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    // item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.addImage) {
            showImageImportDialog()
        }
        if (id == R.id.settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showImageImportDialog() {
        val items = arrayOf("Camera", "Gallery")
        val dialog =
            AlertDialog.Builder(this)
        dialog.setTitle("Select Image")
        dialog.setItems(
            items
        ) { dialogInterface, which ->
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission()
                } else {
                    pickCamera()
                }
            }
            if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                } else {
                    pickGallery()
                }
            }
        }
        dialog.create().show()
    }
    private fun pickGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE)
    }
    private fun pickCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "NewPic")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To text")
        image_uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE)
    }
    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            storagePermission,
            400
        )
    }
    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, 200)
    }
    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        return result && result1
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> if (grantResults.size > 0) {
                val cameraAccepted = grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                val writeStorageAccepted = grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                if (cameraAccepted && writeStorageAccepted) {
                    pickCamera()
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            STORAGE_REQUEST_CODE -> if (grantResults.size > 0) {
                val writeStorageAccepted = grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                if (writeStorageAccepted) {
                    pickGallery()
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data!!.data)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                mPreviewIv!!.setImageURI(resultUri)
                val bitmapDrawable = mPreviewIv!!.drawable as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap
                val recognizer =
                    TextRecognizer.Builder(applicationContext).build()
                if (!recognizer.isOperational) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                } else {
                    val frame =
                        Frame.Builder().setBitmap(bitmap).build()
                    val items = recognizer.detect(frame)
                    val sb = StringBuilder()
                    for (i in 0 until items.size()) {
                        val myItem = items.valueAt(i)
                        sb.append(myItem.value)
                    }
                    mResultEt!!.setText(sb.toString())
                    val medname = sb.toString() as String


                    val datab = FirebaseFirestore.getInstance()
                    datab.collection("MedLife")
                        .get()
                        .addOnCompleteListener {
                            val resultmed:StringBuffer = StringBuffer()
                            if(it.isSuccessful){
                                for(document in it.result!!){
                                    resultmed.append(document.data.getValue("Name"))
                                    //.append(document.data.getValue("Groups")).append("\n\n")
                                    if (document.data.getValue("Name").toString().toLowerCase()==medname.toLowerCase()){
                                        MedName.setText(document.data.getValue("Name").toString())
                                        MedUse.setText(document.data.getValue("Groups").toString())
                                        Suggest.setOnClickListener{
                                            val myIntent = Intent(this, DetailActivity::class.java)
                                            myIntent.putExtra("medicine",document.data.getValue("Name").toString())
                                            startActivity(myIntent)
                                        }
                                }
                                }
                            }    
                        }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}