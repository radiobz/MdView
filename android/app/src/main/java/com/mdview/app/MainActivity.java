package com.mdview.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.ValueCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {

    private WebView webView;
    private ValueCallback<Uri[]> filePathCallback;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView view,
                                             ValueCallback<Uri[]> callback,
                                             FileChooserParams fileChooserParams) {
                filePathCallback = callback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    filePathCallback = null;
                    return false;
                }
                return true;
            }
        });

        // JS 桥接：给网页调用，扫描本地 .md 文件
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public String listMdFiles() {
                JSONArray arr = new JSONArray();
                File[] dirs = new File[]{
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                };
                FilenameFilter filter = (dir, name) ->
                        name.toLowerCase().endsWith(".md") || name.toLowerCase().endsWith(".markdown");
                for (File dir : dirs) {
                    if (dir == null || !dir.exists()) continue;
                    File[] files = dir.listFiles(filter);
                    if (files == null) continue;
                    for (File f : files) {
                        try {
                            JSONObject o = new JSONObject();
                            o.put("name", f.getName());
                            o.put("path", f.getAbsolutePath());
                            o.put("size", f.length());
                            arr.put(o);
                        } catch (Exception ignored) {}
                    }
                }
                return arr.toString();
            }

            @JavascriptInterface
            public String readFile(String path) {
                try {
                    File f = new File(path);
                    FileInputStream fis = new FileInputStream(f);
                    byte[] buf = new byte[(int) f.length()];
                    fis.read(buf);
                    fis.close();
                    return new String(buf, StandardCharsets.UTF_8);
                } catch (Exception e) {
                    return "";
                }
            }

            @JavascriptInterface
            public boolean hasPermission() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    return Environment.isExternalStorageManager();
                }
                return true;
            }

            @JavascriptInterface
            public void requestPermission() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    try {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    } catch (Exception e) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivity(intent);
                    }
                }
            }
        }, "AndroidBridge");

        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && filePathCallback != null) {
            Uri[] results = null;
            if (resultCode == RESULT_OK && data != null) {
                results = new Uri[]{ data.getData() };
            }
            filePathCallback.onReceiveValue(results);
            filePathCallback = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
