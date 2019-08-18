package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.share

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        webView.apply {
            settings.javaScriptEnabled = true

            //webViewClient = WebViewClient()

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String) {
                    urlEditText.setText(url)
                }
            }

        }
        webView.loadUrl("https://www.google.com")

        urlEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                webView.loadUrl(url_path(urlEditText.text.toString()))
                true
            }else{
                false
            }
        }

        //컨텍스트 메뉴를 표시할 뷰 등록
        registerForContextMenu(webView)

    }//onCreate end

    //컨텍스트 메뉴 작성
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }

    //컨텍스트 아이템 조건별 선택
    override fun onContextItemSelected(item: MenuItem): Boolean {

        when(item?.itemId){

            R.id.action_share -> {
                //페이지 공유
                share(webView.url)
                return  true
            }

            R.id.action_browser -> {
                //기본 웹 브라우저에서 열기
                browse(webView.url)
                return true
            }

        }


        return super.onContextItemSelected(item)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack()
        }else{
            super.onBackPressed()
        }


    }//onBackPressed End

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item?.itemId){
            R.id.action_google, R.id.action_home -> {
                webView.loadUrl("https://www.google.com")
                return true
            }

            R.id.action_naver -> {
                webView.loadUrl("https://www.naver.com")
                return true
            }

            R.id.action_daum -> {
                webView.loadUrl("https://www.daum.net")
                return true
            }

            R.id.action_call -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:031-132-4567")
                if (intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }
                return true
            }

            R.id.action_send_text -> {
                //문자보내기
                val intent = Intent(Intent.ACTION_SEND)
                intent.apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "보낼 문자열")
                    var chooser = Intent.createChooser(intent, null)
                    if (intent.resolveActivity(packageManager) != null){
                        startActivity(chooser)
                    }

                    //anko query
                    //sendSMS("031-123-4567", webView.url)
                }
                return true
            }

            R.id.action_email -> {
                //이메일 보내기
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("http://www.example.com")
                if (intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }

                //anko query
                //email("http://www.example.com","좋은 사이트", webView.url)

                return  true
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun url_path(address : String) : String
    {
        var temp = address

        if (!address.contains("https://", ignoreCase = true)) {
            temp = "https://$temp"
        } else temp

        Log.d("url_path",temp.toString())
        return temp



    }//url_path end

}
