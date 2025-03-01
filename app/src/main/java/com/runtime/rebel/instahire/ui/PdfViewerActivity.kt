package com.runtime.rebel.instahire.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.R
import com.runtime.rebel.instahire.databinding.ActivityPdfViewerBinding
import com.runtime.rebel.instahire.vm.pdf.PdfViewerViewModel
import com.runtime.rebel.instahire.vm.pdf.PdfViewerViewModelFactory
import java.net.URLEncoder
import javax.inject.Inject

class PdfViewerActivity : AppCompatActivity() {

    @Inject
    lateinit var pdfViewerViewModelFactory: PdfViewerViewModelFactory
    private lateinit var viewModel: PdfViewerViewModel
    private lateinit var binding: ActivityPdfViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (application as App).appComponent.inject(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this, pdfViewerViewModelFactory)[PdfViewerViewModel::class.java]
        intent.getStringExtra("recent_boost")?.let {
            binding.btnUse.visibility = View.VISIBLE
        }

        binding.btnUse.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("useClicked", intent.getStringExtra("recent_boost"))
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }


        // Enable JavaScript for PDF.js
        with(binding.webView){
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            settings.cacheMode = WebSettings.LOAD_NO_CACHE

            webViewClient = WebViewClient()

            // Your Firebase Storage PDF URL
            val pdfUrl = intent.getStringExtra("pdfUrl") ?: "https://www.google.com"
            // Encode the URL to prevent issues with special characters
            val encodedUrl = URLEncoder.encode(pdfUrl, "UTF-8")
            // Load PDF using PDF.js viewer
            val pdfJsViewer = "https://docs.google.com/gview?embedded=true&url=$encodedUrl"
            loadUrl(pdfJsViewer)
        }

    }
}