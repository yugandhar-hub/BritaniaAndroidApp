package com.brit.britaniaapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ChartListActivity extends AppCompatActivity {

    private WebView mWebView;

    Report report;

    private boolean isPotrait = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_list);
        ((TextView) findViewById(R.id.tvTitle)).setText("List Report");
        findViewById(R.id.ibBack).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.progress).setVisibility(View.VISIBLE);

        if (getIntent().getExtras() != null) {
            report = getIntent().getExtras().getParcelable("report");
        }


        findViewById(R.id.fullScreen).setOnClickListener(view -> {
            if (isPotrait) {
                isPotrait = false;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                isPotrait = true;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });


//        signinTablue();

        mWebView = findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(false);
        mWebView.getSettings().setDomStorageEnabled(true);
//        String ua = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
//        String ua = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";
        String ua = "Mozilla/5.0 (Linux; Android 4.4; Nexus 4 Build/KRT16H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";
//        String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.104 Safari/537.36";
        mWebView.getSettings().setUserAgentString(ua);


        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null || url.startsWith("http://") || url.startsWith("https://"))
                    return false;

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                    return true;
                } catch (Exception e) {
                    Log.i("TAG", "shouldOverrideUrlLoading Exception:" + e);
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                final String password = "AIBRIT@2020";
//                final String username = "projectai@brtindia.com";


//                final String js = "javascript:" +
//                        "document.getElementsByName('username').value = '" + username + "';" +
//                        "document.getElementsByName('password').value = '" + password + "';" +
//                        "document.getElementsById('button-signin').click()";
//
//                if (Build.VERSION.SDK_INT >= 19) {
//                    view.evaluateJavascript(js, new ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String s) {
//
//                        }
//                    });
//                } else {
//                    view.loadUrl(js);
//                }

                view.loadUrl("javascript:document.getElementsByTbTestId('textbox-username-input').value = 'projectai@brtindia.com'");
//                view.loadUrl("javascript:document.getElementsByName('password').value = 'AIBRIT@2020'");
//                view.loadUrl("javascript:document.forms['loginForm'].submit()");
//
                ((ProgressBar) findViewById(R.id.progress)).setVisibility(View.GONE);
            }
        });


        runOnUiThread(() -> {
            mWebView.loadUrl(report.getWebUrl());
        });
//        setDesktopMode(mWebView,true);


    }

    public void setDesktopMode(WebView webView, boolean enabled) {
        String newUserAgent = webView.getSettings().getUserAgentString();
        if (enabled) {
            try {
                String ua = webView.getSettings().getUserAgentString();
                String androidOSString = webView.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
                newUserAgent = webView.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newUserAgent = null;
        }

        webView.getSettings().setUserAgentString(newUserAgent);
        webView.getSettings().setUseWideViewPort(enabled);
        webView.getSettings().setLoadWithOverviewMode(enabled);
        webView.reload();
    }

    private void signinTablue() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://reports.britindia.com/vizportal/api/web/v1/login";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("encryptedPassword", "2a60a18aca82ca1d946a3bb6b599d27" +
                    "7b563588483af023e823061ccb1eb9cb37f5caa74a894681616588d18e379993fa" +
                    "d293158f296d37f576588bbf811eda3a8c5510fc3088f68045330bd1ad17851780" +
                    "9a48ca7b62ddbe55f7381db1dedfbf90efb84e47565cd75321e54fcde79c1818b89cea4e03b81952dd6a94a7b376a");
            jsonBody.put("keyId", "koegLXXQR1atvrXGWZ57tA|6Qznex3Syu1AAJFEV6MEB4HDRDMGQrs8");
            jsonBody.put("username", "sa");
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                Log.i("VOLLEY", response);
                Toast.makeText(ChartListActivity.this, "Report added successfully!", Toast.LENGTH_SHORT).show();

                onBackPressed();

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    Toast.makeText(ChartListActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}