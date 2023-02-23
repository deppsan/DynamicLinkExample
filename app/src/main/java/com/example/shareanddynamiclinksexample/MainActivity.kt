package com.example.shareanddynamiclinksexample

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.applyCanvas
import androidx.core.view.ViewCompat
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

        btnButton.setOnClickListener {

            val baseUrl = "https://www.mioxxo.com"
            val PATH_SEGMENT = "/cupones_segment"
            val LINKS_URL_PREFIX = "https://shareanddynamiclinksexample.page.link"

            generateShortDynamicLink(baseUrl, PATH_SEGMENT, LINKS_URL_PREFIX, tvDynamicLink)
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
                title = ""
                description = ""
                imageUrl = Uri.parse("") //Imagen metadata al compartir
            }
        }.addOnSuccessListener {
            tvDynamicLink.text = it.shortLink.toString()
        }.addOnFailureListener {
            tvDynamicLink.text = it.message
        }
    }
}