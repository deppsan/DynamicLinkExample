package com.example.shareanddynamiclinksexample

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnButton = findViewById<Button>(R.id.btnCreateDynamicLink)
        val tvDynamicLink = findViewById<TextView>(R.id.tvDynamicLink)
        val btnShareScreenshot = findViewById<Button>(R.id.btnShareScreenshot)


        btnButton.setOnClickListener {

            val baseUrl = "https://www.mioxxo.com"
            val PATH_SEGMENT = "/cupones_segment"
            val LINKS_URL_PREFIX = "https://shareanddynamiclinksexample.page.link"

            generateShortDynamicLink(baseUrl, PATH_SEGMENT, LINKS_URL_PREFIX, tvDynamicLink)
        }

        btnShareScreenshot.setOnClickListener {
            storeBitmap(screenShot())
        }


    }

    private fun generateShortDynamicLink(
        baseUrl: String,
        PATH_SEGMENT: String,
        LINKS_URL_PREFIX: String,
        tvDynamicLink: TextView
    ) {
        FirebaseApp.initializeApp(applicationContext)
        Firebase.dynamicLinks.shortLinkAsync {

            link = Uri.parse("${baseUrl + PATH_SEGMENT}")
            domainUriPrefix = LINKS_URL_PREFIX
            androidParameters { }
            socialMetaTagParameters {
                title = "Esto es una liga dynamica"
                description = "Como Wolverine en los 60's latino"
                imageUrl = Uri.parse("https://i.etsystatic.com/22360457/r/il/447352/2199635638/il_570xN.2199635638_svz8.jpg") //Imagen metadata al compartir
            }
        }.addOnSuccessListener {
            tvDynamicLink.text = it.shortLink.toString()
            copyToClipboard(it.shortLink.toString())
        }.addOnFailureListener {
            tvDynamicLink.text = it.message
        }
    }

    private fun copyToClipboard(textToClip : String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToClip)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this,"Copiado", Toast.LENGTH_LONG).show()
    }

    private fun screenShot() : Bitmap {
        val rootView = window.decorView.findViewById<View>(android.R.id.content)

        rootView.isDrawingCacheEnabled = true

        val bitmap = Bitmap.createBitmap(rootView.drawingCache)
        rootView.isDrawingCacheEnabled = false
        return bitmap
    }

    private fun storeBitmap(bitmap : Bitmap){
        val pathOfBmp = MediaStore.Images.Media.insertImage(this.contentResolver, bitmap, "title", null)
        val uri = Uri.parse(pathOfBmp)

        shareImage(uri)
    }

    private fun shareImage (uri : Uri){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "")
        shareIntent.putExtra(Intent.EXTRA_TITLE, "")
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)

        this.startActivity(Intent.createChooser(shareIntent,null))
    }
}