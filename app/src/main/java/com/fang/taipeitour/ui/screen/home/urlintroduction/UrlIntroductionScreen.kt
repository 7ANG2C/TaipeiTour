package com.fang.taipeitour.ui.screen.home.urlintroduction

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.webkit.GeolocationPermissions
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.fang.taipeitour.R
import com.fang.taipeitour.dsl.Invoke
import com.fang.taipeitour.ui.component.Loading
import com.fang.taipeitour.ui.component.dsl.BackHandler
import com.fang.taipeitour.ui.component.dsl.LocalDarkMode
import com.fang.taipeitour.util.logD
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun UrlIntroductionScreen(attractionUrl: String, backHandler: Invoke) {
    var urlTitle by rememberSaveable {
        mutableStateOf("1234")
    }
    var loading by rememberSaveable {
        mutableStateOf("")
    }
    var canGoBack by rememberSaveable {
        mutableStateOf(false)
    }
    var goBackTrigger by rememberSaveable {
        mutableStateOf(0)
    }

    Column(Modifier.fillMaxSize()) {
        var canGoForward by rememberSaveable {
            mutableStateOf(false)
        }
        val chromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                urlTitle = title.takeIf { it?.isNotBlank() == true } ?: "-"
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                loading = if (newProgress >= 100) {
                    ""
                } else "$newProgress%"
            }

            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback
            ) {
                // 如果有權限，檢查定位有沒有開
                callback.invoke(origin, true, false)
            }
        }
        val client = object : WebViewClient() {
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                loading = ""
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val url = request.url
                return if (url != null && url.toString()
                        .startsWith("https://www.google.com/maps")
                ) {
                    view.context.startActivity(Intent(Intent.ACTION_VIEW, url))
                    true
                } else if (url.scheme == "intent") {
                    try {
                        val intent = Intent.parseUri(url.toString(), Intent.URI_INTENT_SCHEME)
                        view.context.startActivity(intent)
                        true
                    } catch (expected: ActivityNotFoundException) {
                        false
                    }
                } else if (url.scheme == "tel") {
                    val intent = Intent(Intent.ACTION_DIAL, url)
                    view.context.startActivity(intent)
                    true
                } else {
                    super.shouldOverrideUrlLoading(view, request)
                }
            }

            override fun doUpdateVisitedHistory(view: WebView, url: String?, isReload: Boolean) {
                super.doUpdateVisitedHistory(view, url, isReload)
                canGoBack = view.canGoBack()
                canGoForward = view.canGoForward()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
//                    view.pauseTimers()
//                    view.onPause()
                super.onReceivedError(view, request, error)
                logD("onProgressChanged_", "onReceivedError")
//                    onReceivedError(error)
            }
        }

        var reloadTrigger by rememberSaveable {
            mutableStateOf(0)
        }

        var goForwardTrigger by rememberSaveable {
            mutableStateOf(0)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .background(controlBarColor())
        ) {
            IconButton(
                onClick = {
                    backHandler()
                },
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    tint = mainColor()
                )
            }
            Text(
                text = urlTitle,
                color = mainColor(),
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            Spacer(modifier = Modifier.width(24.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        setDownloadListener { url, _, description, mimeType, _ ->
                            // 2: userAgent
                            // 3: contentLength
                            val request = DownloadManager.Request(Uri.parse(url))
                                .setTitle(System.currentTimeMillis().toString())
                                .setDescription(description)
                                .setMimeType(mimeType)
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(
                                    Environment.DIRECTORY_DOWNLOADS,
                                    URLUtil.guessFileName(url, null, null)
                                )

                            val downloadManager =
                                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            downloadManager.enqueue(request)
                        }

                        settings.apply {
                            javaScriptEnabled = true
                            setGeolocationEnabled(true)
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false

                            databaseEnabled = true
                            domStorageEnabled = true
                            allowFileAccess = true
                            allowContentAccess = true
                            mediaPlaybackRequiresUserGesture = false
                            // 将图片调整到适合webView的大小
                            useWideViewPort = true
                            // 縮放至螢幕大小
                            loadWithOverviewMode = true
                            // 是否支援通過 JS 開啟新窗口
                            javaScriptCanOpenWindowsAutomatically = true
                            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                        }
                        webViewClient = client
                        webChromeClient = chromeClient
                        loadUrl(attractionUrl)
                    }
                }
            ) { webView ->
                if (reloadTrigger != 0) {
                    if (reloadTrigger == 1) {
                        webView.reload()
                        reloadTrigger = 0
                    }
                }
                if (goBackTrigger != 0) {
                    if (goBackTrigger == 1) {
                        webView.goBack()
                        goBackTrigger = 0
                    }
                }
                if (goForwardTrigger != 0) {
                    if (goForwardTrigger == 1) {
                        webView.goForward()
                        goForwardTrigger = 0
                    }
                }
            }
            if (loading != "") {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Loading(isFancy = false)
                    Text(text = loading, color = controlBarColor())
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .background(controlBarColor())
        ) {
            val context = LocalContext.current
            IconButton(
                onClick = {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(attractionUrl)
                    context.startActivity(i)
                },
                modifier = Modifier

            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_browse),
                    contentDescription = null,
                    tint = mainColor()
                )
            }
            // 分享
            IconButton(
                onClick = {
                    context.startActivity(
                        Intent.createChooser(
                            Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                val shareText = "快來看!有人發表了評論"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            },
                            "分享文章"
                        )
                    )
                },
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = null,
                    tint = mainColor()
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            // 複製網址
            IconButton(
                onClick = {
                    reloadTrigger = 1
                },
                modifier = Modifier

            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_reload),
                    contentDescription = null,
                    tint = mainColor()
                )
            }
            // 向前
            IconButton(
                onClick = {
                    goBackTrigger = 1
                },
                modifier = Modifier,
                enabled = canGoBack
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_go),
                    contentDescription = null,
                    modifier = Modifier.scale(-1f, -1f),
                    tint = mainColor().copy(
                        alpha = if (canGoBack) 1f else 0.5f
                    )
                )
            }
            // 向後
            IconButton(
                onClick = {
                    goForwardTrigger = 1
                },
                modifier = Modifier,
                enabled = canGoForward
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_go),
                    contentDescription = null,
                    tint = mainColor().copy(
                        alpha = if (canGoForward) 1f else 0.5f
                    )
                )
            }
        }
    }
    BackHandler {
        if (canGoBack) {
            goBackTrigger = 1
        } else {
            backHandler()
        }
    }
}

@Composable
private fun controlBarColor() = if (LocalDarkMode) {
    MaterialTheme.colorScheme.onSecondary
} else {
    MaterialTheme.colorScheme.inverseSurface
}

@Composable
private fun mainColor() = if (LocalDarkMode) {
    MaterialTheme.colorScheme.onPrimaryContainer
} else {
    MaterialTheme.colorScheme.secondaryContainer
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermission() {
    val permission = Manifest.permission.READ_EXTERNAL_STORAGE
    val permissionState = rememberPermissionState(permission)
    if (!permissionState.hasPermission) {
        PermissionRequired(
            permissionState = permissionState,
            permissionNotGrantedContent = {
                Button(onClick = { permissionState.launchPermissionRequest() }) {
                    Text("Request permission.")
                }
            },
            permissionNotAvailableContent = {
                Text("Permission Denied.")
            },
            content = {
                Text("Permission Granted.")
            }
        )
    }
}
