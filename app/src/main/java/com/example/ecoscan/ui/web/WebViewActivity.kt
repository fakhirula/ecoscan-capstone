package com.example.ecoscan.ui.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.ecoscan.R

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val url = intent.getStringExtra("url")

        if(url != null){
            val webView = findViewById<WebView>(R.id.webView)
            webView.loadUrl(url)
            webView.settings.javaScriptEnabled = true
        }
    }
}