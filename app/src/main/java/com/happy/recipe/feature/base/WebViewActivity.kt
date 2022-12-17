package com.happy.recipe.feature.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.happy.recipe.databinding.ActivityMenuWebviewBinding
import com.happy.recipe.utility.gone
import com.happy.recipe.utility.visible

class WebViewActivity: AppCompatActivity() {

    companion object {
        const val MENU_URL_KEY = "MENU_URL_KEY"

        fun start(context: Context, urlString: String) {
            context.startActivity(Intent(context, WebViewActivity::class.java).apply {
                putExtra(MENU_URL_KEY, urlString)
            })
        }
    }

    private lateinit var binding: ActivityMenuWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val url = intent.getStringExtra(MENU_URL_KEY) ?: ""
        setWebContent(url)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebContent(urlString: String) {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = object: WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBar.visible()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBar.gone()
                }
            }
            loadUrl(urlString)
        }
    }
}