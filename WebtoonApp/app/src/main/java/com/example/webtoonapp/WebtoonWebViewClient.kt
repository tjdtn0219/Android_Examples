package com.example.webtoonapp

import android.graphics.Bitmap
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

class WebtoonWebViewClient(
    private val progressBar: ProgressBar,
    private val saveData: (String) -> Unit,
): WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        //페이지 로딩하기 전에 실행

        if(request != null && request.url.toString().contains("comic.naver.com/webtoon/detail")) {
            saveData(request.url.toString())
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        progressBar.visibility = GONE
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)

        progressBar.visibility = VISIBLE
    }
}