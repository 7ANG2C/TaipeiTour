package com.fang.taipeitour.ui.screen.home.webintroduction

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.webkit.GeolocationPermissions
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import com.fang.taipeitour.R
import com.fang.taipeitour.dsl.Invoke
import com.fang.taipeitour.model.language.getLocaleString
import com.fang.taipeitour.ui.component.dsl.BackHandler
import com.fang.taipeitour.ui.component.dsl.LocalDarkMode
import com.fang.taipeitour.ui.component.dsl.LocalLanguage
import com.fang.taipeitour.ui.component.dsl.stateValue
import com.fang.taipeitour.util.LocationUtil
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun UrlIntroductionScreen(
    viewModel: WebIntroductionViewModel = koinViewModel(),
    attractionUrl: String,
    backHandler: Invoke,
) {
    var canGoBack by rememberSaveable {
        mutableStateOf(false)
    }
    var goBackTrigger by rememberSaveable {
        mutableStateOf(false)
    }
    var isResume by rememberSaveable {
        mutableStateOf<Boolean?>(null)
    }
    Column(Modifier.fillMaxSize()) {
        TopBar(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(getControlBarBgColor()),
            urlTitle = viewModel.titleState.stateValue(),
            loadingProgress = viewModel.loadingState.stateValue(),
            backHandler = backHandler,
        )

        var reloadTrigger by rememberSaveable {
            mutableStateOf(false)
        }

        var canGoForward by rememberSaveable {
            mutableStateOf(false)
        }
        var goForwardTrigger by rememberSaveable {
            mutableStateOf(false)
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
        ) {
            val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
            var checkPermission by rememberSaveable {
                mutableStateOf(false)
            }
            val locationPlz = LocalLanguage.getLocaleString(R.string.location_plz)
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        setDownloadListener { url, _, description, mimeType, _ ->
                            val request =
                                DownloadManager.Request(Uri.parse(url))
                                    .setTitle(System.currentTimeMillis().toString())
                                    .setDescription(description)
                                    .setMimeType(mimeType)
                                    .setNotificationVisibility(
                                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED,
                                    )
                                    .setDestinationInExternalPublicDir(
                                        Environment.DIRECTORY_DOWNLOADS,
                                        URLUtil.guessFileName(url, description, mimeType),
                                    )
                            val downloadManager = context.getSystemService<DownloadManager>()
                            downloadManager?.enqueue(request)
                        }
                        settings.apply {
                            javaScriptEnabled = true
                            javaScriptCanOpenWindowsAutomatically = true
                            setGeolocationEnabled(true)
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false
                            mediaPlaybackRequiresUserGesture = true
                            domStorageEnabled = true
                            allowFileAccess = true
                            allowContentAccess = true
                            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                        }
                        webViewClient =
                            object : WebViewClient() {
                                override fun shouldOverrideUrlLoading(
                                    view: WebView,
                                    request: WebResourceRequest,
                                ): Boolean {
                                    val uri = request.url
                                    val urlString = uri?.toString()
                                    val scheme = uri.scheme
                                    return when {
                                        urlString.isNullOrBlank() -> {
                                            true
                                        }
                                        urlString.startsWith("https://www.google.com/maps") -> {
                                            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                                            true
                                        }
                                        !urlString.startsWith("http") -> {
                                            val intent =
                                                if (scheme == "intent") {
                                                    Intent.parseUri(urlString, Intent.URI_INTENT_SCHEME)
                                                } else {
                                                    Intent(Intent.ACTION_VIEW, uri)
                                                }
                                            kotlin.runCatching {
                                                context.startActivity(intent)
                                            }.fold(onSuccess = { true }, onFailure = { false })
                                        }
                                        else -> super.shouldOverrideUrlLoading(view, request)
                                    }
                                }

                                override fun doUpdateVisitedHistory(
                                    view: WebView,
                                    url: String?,
                                    isReload: Boolean,
                                ) {
                                    super.doUpdateVisitedHistory(view, url, isReload)
                                    viewModel.setUrl(url.orEmpty())
                                    canGoBack = view.canGoBack()
                                    canGoForward = view.canGoForward()
                                }
                            }

                        webChromeClient =
                            object : WebChromeClient() {
                                override fun onReceivedTitle(
                                    view: WebView?,
                                    title: String?,
                                ) {
                                    super.onReceivedTitle(view, title)
                                    viewModel.setTitle(title.orEmpty())
                                }

                                override fun onProgressChanged(
                                    view: WebView?,
                                    newProgress: Int,
                                ) {
                                    super.onProgressChanged(view, newProgress)
                                    viewModel.setLoadingProgress(newProgress)
                                }

                                override fun onGeolocationPermissionsShowPrompt(
                                    origin: String?,
                                    callback: GeolocationPermissions.Callback,
                                ) {
                                    when {
                                        !permissionState.hasPermission -> {
                                            checkPermission = true
                                        }
                                        !LocationUtil.isLocationEnabled(context) -> {
                                            Toast.makeText(context, locationPlz, Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                        else -> callback(origin, true, false)
                                    }
                                }
                            }
                        loadUrl(attractionUrl)
                    }
                },
            ) { webView ->
                if (reloadTrigger) {
                    webView.reload()
                    reloadTrigger = false
                }

                if (goBackTrigger) {
                    webView.goBack()
                    goBackTrigger = false
                }

                if (goForwardTrigger) {
                    webView.goForward()
                    goForwardTrigger = false
                }

                if (isResume == true) {
                    webView.resumeTimers()
                    webView.onResume()
                    isResume = null
                } else if (isResume == false) {
                    webView.pauseTimers()
                    webView.onPause()
                    isResume = null
                }
            }
            RequestPermission(permissionState, checkPermission) {
                checkPermission = false
            }
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(getControlBarBgColor()),
        ) {
            val context = LocalContext.current
            val urlState = viewModel.urlState.stateValue()
            IconBtn(res = R.drawable.ic_browse) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlState))
                kotlin.runCatching {
                    context.startActivity(intent)
                }
            }
            val title = viewModel.titleState.stateValue()
            IconBtn(res = R.drawable.ic_share) {
                val intent =
                    Intent.createChooser(
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, urlState)
                        },
                        title,
                    )
                context.startActivity(intent)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconBtn(res = R.drawable.ic_reload) {
                reloadTrigger = true
            }
            IconBtn(
                modifier = Modifier.scale(-1f, -1f),
                res = R.drawable.ic_go,
                enabled = canGoBack,
            ) {
                goBackTrigger = true
            }
            IconBtn(
                res = R.drawable.ic_go,
                enabled = canGoForward,
            ) {
                goForwardTrigger = true
            }
        }
    }

    BackHandler(onEvent = { lifecycleEvent ->
        when (lifecycleEvent) {
            Lifecycle.Event.ON_RESUME -> isResume = true
            Lifecycle.Event.ON_PAUSE -> isResume = false
            else -> {}
        }
    }) {
        if (canGoBack) {
            goBackTrigger = true
        } else {
            backHandler.invoke()
        }
    }
}

@Composable
private fun TopBar(
    modifier: Modifier,
    urlTitle: String,
    loadingProgress: Int,
    backHandler: Invoke,
) {
    Box(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconBtn(
                res = R.drawable.ic_back,
                onClick = backHandler,
            )
            Text(
                text = urlTitle.takeIf { it.isNotBlank() } ?: "-",
                modifier = Modifier.weight(1f),
                color = getPrimaryColor(),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.width(24.dp))
        }

        if (loadingProgress in 1..99) {
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(loadingProgress.toFloat()),
                    thickness = 3.dp,
                    color = getPrimaryColor().copy(alpha = 0.9f),
                )
                HorizontalDivider(
                    modifier = Modifier.weight((100 - loadingProgress).toFloat()),
                    thickness = 3.dp,
                    color = Color.Transparent,
                )
            }
        }
    }
}

@Composable
private fun IconBtn(
    modifier: Modifier = Modifier,
    @DrawableRes res: Int,
    enabled: Boolean = true,
    onClick: Invoke,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        val color = getPrimaryColor()
        Icon(
            painter = painterResource(res),
            contentDescription = null,
            tint = if (enabled) color else color.copy(alpha = 0.35f),
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermission(
    permissionState: PermissionState,
    checkPermission: Boolean,
    closeCheckPermission: Invoke,
) {
    if (checkPermission) {
        var showDialog by rememberSaveable {
            mutableStateOf(false)
        }

        PermissionRequired(
            permissionState = permissionState,
            content = {
                // hasPermission
                closeCheckPermission()
            },
            permissionNotGrantedContent = {
                showDialog = true
            },
            permissionNotAvailableContent = {
                closeCheckPermission()
            },
        )
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            closeCheckPermission()
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                    ) {
                        Text(LocalLanguage.getLocaleString(R.string.cancel))
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            permissionState.launchPermissionRequest()
                            showDialog = false
                            closeCheckPermission()
                        },
                    ) {
                        Text(LocalLanguage.getLocaleString(R.string.confirm))
                    }
                },
                title = {
                    Text(LocalLanguage.getLocaleString(R.string.location_title))
                },
                text = {
                    Text(LocalLanguage.getLocaleString(R.string.location_msg))
                },
                properties =
                    DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false,
                    ),
            )
        }
    }
}

@Composable
private fun getPrimaryColor() =
    if (LocalDarkMode) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

@Composable
private fun getControlBarBgColor() =
    if (LocalDarkMode) {
        MaterialTheme.colorScheme.onSecondary
    } else {
        MaterialTheme.colorScheme.inverseSurface
    }

@Preview(showBackground = true)
@Composable
private fun Preview() {
    UrlIntroductionScreen(attractionUrl = "https://www.google.com/") {}
}
