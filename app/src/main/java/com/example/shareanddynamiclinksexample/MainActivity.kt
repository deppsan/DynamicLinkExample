package com.example.shareanddynamiclinksexample

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnButton = findViewById<Button>(R.id.btnCreateDynamicLink)
        val tvDynamicLink = findViewById<TextView>(R.id.tvDynamicLink)

        btnButton.setOnClickListener {

            FirebaseApp.initializeApp(applicationContext)
            Firebase.dynamicLinks.shortLinkAsync {
                val baseUrl = "mioxxo.com"
                val PATH_SEGMENT = "/cupones_segment"
                val LINKS_URL_PREFIX = "https://shareanddynamiclinksexample.page.link"

                link = Uri.parse("${baseUrl + PATH_SEGMENT}")
                domainUriPrefix = LINKS_URL_PREFIX
                androidParameters { }
                socialMetaTagParameters {
                    title = ""
                    description = ""
                    imageUrl = Uri.parse("") //Imagen metadata al compartir
                }
            }.addOnSuccessListener {
                tvDynamicLink.text = it?.shortLink.toString()
            }
            .addOnFailureListener {
                tvDynamicLink.text = it?.message
            }
        }
    }
}