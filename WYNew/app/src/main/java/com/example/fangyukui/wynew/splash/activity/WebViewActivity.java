package com.example.fangyukui.wynew.splash.activity;

import android.os.Bundle;

import com.example.fangyukui.wynew.splash.bean.Action;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.fangyukui.wynew.R;

public class WebViewActivity extends AppCompatActivity {

    public static final String ACTION_NAME = "action";

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Action action = (Action) getIntent().getSerializableExtra(ACTION_NAME);

        webview = (WebView) findViewById(R.id.webview);

        //启用javaScript
        webview.getSettings().setJavaScriptEnabled(true);


        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);

        webview.loadUrl(action.getLink_url());

        //处理url重定向不要抛到系统浏览器
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.i("it520","onReceivedError");
                super.onReceivedError(view, request, error);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.i("it520","onReceivedError2 ");

            }
        });


    }

    @Override
    public void onBackPressed() {
        //如果webviwe能够回退到上一个页面
        if(webview.canGoBack()){
            webview.goBack();
            return;
        }
        super.onBackPressed();
    }

}
