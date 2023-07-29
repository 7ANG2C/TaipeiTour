package com.fang.taipeitour.ui.screen.attraction.guide

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import com.fang.taipeitour.model.attraction.Attraction2
import com.fang.taipeitour.ui.theme.TaipeiTourTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * 景點導覽
 */
class AttractionGuideFragment : Fragment() {

    enum class Page {
        FIRST, SECOND, THIRD
    }

    companion object {
        private const val ARG_ID = "id"
        fun createIntent(id: Attraction2): Fragment {
            return AttractionGuideFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ID, id)
                }
            }
        }
    }

    private val viewModel by viewModel<AttractionGuideViewModel> {
        parametersOf(requireArguments().getParcelable<Attraction2>(ARG_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {

            // Dispose of the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val isShow = remember {
                    mutableStateOf(false)
                }

                CollapsingToolbarParallaxEffect(
                    Modifier
                        .fillMaxSize()
                        .background(Color(0xff252525))
//                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    var current by remember {
                        mutableStateOf(Page.FIRST)
                    }
                    val state = rememberLazyListState()
                    TabRow(indicator = {}, selectedTabIndex = current.ordinal) {
                        Page.values().forEach {
                            Tab(selected = current == it, onClick = {
                                current = it
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Create,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                    Row {
                        Spacer(Modifier.weight(1f))
                        Divider(Modifier.weight(1f))
                        Spacer(Modifier.weight(1f))
                    }
//                    LazyColumn(
//                        modifier = Modifier.fillMaxWidth(),
//                        state = state
//                    ) {
////                                ImageSlider(
////                                    modifier = Modifier,
////                                    images = viewModel.state.collectAsState().value?.images?.map { it.src }
////                                        .orEmpty()
////                                )
//
//                        item {
//                            Text(text = viewModel.state.collectAsState().value?.name ?: "")
//                            Text(text = "官方網站",
//                                modifier = Modifier.clickable {
//                                    isShow.value = true
//                                })
//                            Text(text = "URL",
//                                modifier = Modifier.clickable {
//                                    isShow.value = true
//                                })
//                        }
//                        item {
//                            repeat(6) {
//                                Text(text = "CONTENT2")
//                            }
//                        }
//                        item {
//                            repeat(6) {
//                                Text(text = "CONTENT3")
//                            }
//                        }
//                    }
                }


                val mUrl = viewModel.state.collectAsState().value?.url ?: ""
                if (isShow.value) {
                    CustomWebView(
                        modifier = Modifier.fillMaxSize(),
                        url = mUrl,
                        onProgressChange = { progress ->
//                            rememberWebViewProgress = progress
                        },
                        initSettings = { settings ->
                            settings?.apply {
                                // 允許 JS 交互
                                javaScriptEnabled = true
                                //将图片调整到适合webView的大小
                                useWideViewPort = true
                                // 縮放至螢幕大小
                                loadWithOverviewMode = true
                                // 縮放操作
                                setSupportZoom(true)
                                builtInZoomControls = true
                                displayZoomControls = true
                                // 是否支援通過 JS 開啟新窗口
                                javaScriptCanOpenWindowsAutomatically = true
                                // 不加載緩存內容
                                cacheMode = WebSettings.LOAD_NO_CACHE
                            }
                        }, onBack = { webView ->
                            if (webView?.canGoBack() == true) {
                                webView.goBack()
                            } else {
//                                isShow.value = true
                                // finish()
                            }
                        }, onReceivedError = {

                        }
                    )
                }
                if (isShow.value) {
                    BackHandler {
                        isShow.value = false
                    }
                } else {

                }

            }
        }
    }

    @Composable
    private fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    private fun GreetingPreview() {
        TaipeiTourTheme {
            Greeting("Android")
        }
    }

    @Composable
    fun CustomWebView(
        modifier: Modifier = Modifier,
        url: String,
        onBack: (webView: WebView?) -> Unit,
        onProgressChange: (progress: Int) -> Unit = {},
        initSettings: (webSettings: WebSettings?) -> Unit = {},
        onReceivedError: (error: WebResourceError?) -> Unit = {}
    ) {
        val webViewChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                // 回調網頁內容加載進度
                onProgressChange(newProgress)
                super.onProgressChanged(view, newProgress)
            }
        }
        val webViewClient = object : WebViewClient() {
            override fun onPageStarted(
                view: WebView?, url: String?,
                favicon: Bitmap?
            ) {
                super.onPageStarted(view, url, favicon)
                onProgressChange(-1)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                onProgressChange(100)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (null == request?.url) return false
                val showOverrideUrl = request.url.toString()
                try {
                    if (!showOverrideUrl.startsWith("")
                        && !showOverrideUrl.startsWith("")
                    ) {
                        // 處理非 http https 開頭
                        Intent(Intent.ACTION_VIEW, Uri.parse(showOverrideUrl)).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            view?.context?.applicationContext?.startActivity(this)
                        }
                        return true
                    }
                } catch (e: Exception) {
                    // 沒有安裝和找到能打開(「xxxx://openlink.cc....」等)協議的應用
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                onReceivedError(error)
            }
        }
        var webView: WebView? = null
        val coroutineScope = rememberCoroutineScope()
        AndroidView(modifier = modifier, factory = { ctx ->
            WebView(ctx).apply {
                this.webViewClient = webViewClient
                this.webChromeClient = webViewChromeClient
                initSettings(this.settings)
                webView = this
                loadUrl(url)
            }
        })
        BackHandler {
            coroutineScope.launch {
                onBack(webView)
            }
        }
    }
}
