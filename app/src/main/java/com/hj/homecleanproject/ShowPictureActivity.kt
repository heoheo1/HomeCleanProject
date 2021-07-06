package com.hj.homecleanproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView

class ShowPictureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_picture)

        val imageView = findViewById<ImageView>(R.id.galleryImage)
        val backButton: ImageButton = findViewById(R.id.backButton)

        backButton.setOnClickListener(View.OnClickListener {
            finish()
        })

        var intent: Intent = intent
        var bytes : ByteArray? = intent.getByteArrayExtra("byte")
        var bitmap: Bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes?.size?:0)
        imageView.setImageBitmap(bitmap)
    }
}