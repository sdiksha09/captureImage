package com.example.shipra.captureimage_kotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Picture
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.appcompat.R.id.image
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue
import android.R.attr.data
import android.widget.ImageView


class MainActivity : AppCompatActivity() {

    lateinit var photoPath: String                 //variable which store photo path
    val REQUEST_TAKE_PHOTO = 1
    private val GALLERY = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_capture.setOnClickListener {

            takePicture()                                //function call
        }

        btn_gallary.setOnClickListener {

            takePictureFromGallery()


        }

    }

    private fun takePictureFromGallery() {

        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)



    }







    private fun takePicture() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {

                photoFile = createImagefile()           //function call


            } catch (e: IOException) {
            }

            if (photoFile != null) {
                val photoUri = FileProvider.getUriForFile(this, "com.example.shipra.captureimage_kotlin.fileprovider", photoFile)

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)


            }

        }

    }







    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {


            picture.setImageBitmap(setScaledBitmap())


        }


    }


    @Throws(IOException::class)
    private fun createImagefile(): File? {
        val fileNmae = "MyPicture"
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "JPEG_" + timeStamp + "_"

        val storagedir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!storagedir.exists()) storagedir.mkdir()
        val image = File.createTempFile(imageFileName, ".png", storagedir)

        photoPath = image.absolutePath
        return image

    }

    private fun setScaledBitmap(): Bitmap {
        val imageViewWidth = picture.width
        val imageViewHeight = picture.height

        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(photoPath, bmOptions)
        val bitmapWidth = bmOptions.outWidth
        val bitmapHeight = bmOptions.outHeight

        val scaleFactor = Math.min(bitmapWidth / imageViewWidth, bitmapHeight / imageViewHeight)

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        return BitmapFactory.decodeFile(photoPath, bmOptions)


    }


}
