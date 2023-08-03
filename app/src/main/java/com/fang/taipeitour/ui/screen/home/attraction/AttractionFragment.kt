package com.fang.taipeitour.ui.screen.home.attraction

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.GeolocationPermissions
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import com.fang.taipeitour.R
import com.fang.taipeitour.model.Invoke
import com.fang.taipeitour.model.attraction.Attraction
import com.fang.taipeitour.ui.component.AutoSizeText
import com.fang.taipeitour.ui.component.ImageSlider
import com.fang.taipeitour.ui.theme.TaipeiTourTheme
import com.fang.taipeitour.util.logD

interface A : java.io.Serializable {
    val sfsdf: Invoke
}

/**
 * 景點導覽
 */
class AttractionFragment : Fragment() {

    companion object {
        private const val ARG_ATTRACTION = "id"
        private const val ARG_ATTRACTIfON = "id2"
        fun createIntent(attraction: Attraction): Fragment {
            return AttractionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ATTRACTION, attraction)

                }
            }
        }
    }

//    private val viewModel by viewModel<AttractionViewModel> {
//        parametersOf(requireArguments().getParcelable<Attraction>(ARG_ATTRACTION))
//    }

    var close: Invoke = {}

    private val attraction by lazy {
        mutableStateOf(requireArguments().getParcelable<Attraction>(ARG_ATTRACTION)!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val aaa = (requireArguments().getSerializable(ARG_ATTRACTIfON) as A)

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CollapsingToolbar(
                    Modifier

                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                )
            }
        }
    }

    private val headerHeight = 250.dp
    private val toolbarHeight = 56.dp

    private val paddingMedium = 16.dp

    private val titlePaddingStart = 16.dp
    private val titlePaddingEnd = 72.dp

    private val titleFontScaleStart = 1f
    private val titleFontScaleEnd = 0.66f

    @Composable
    fun CollapsingToolbar(modifier: Modifier = Modifier) {
        val scroll = rememberScrollState(0)

        val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
        val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }

        Box(modifier = modifier) {
            val isShow = rememberSaveable {
                mutableStateOf(false)
            }
            Body(
                scroll = scroll,
                modifier = Modifier.fillMaxSize()
            ) {
                isShow.value = true
            }
            Header(
                scroll = scroll,
                headerHeightPx = headerHeightPx,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
            )
            IconButton(
                onClick = {
//                    aaa.sfsdf()
                    close()
                },
                modifier = Modifier
                    .padding(20.dp)
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.White
                )
            }
            TopBar(
                scroll = scroll,
                headerHeightPx = headerHeightPx,
//                toolbarHeightPx = toolbarHeightPx
            )
            Title(scroll = scroll)
            Crossfade(targetState = isShow.value) {
                Web(it) {
                    isShow.value = false
                }
            }
        }
    }

    @Composable
    private fun Header(
        scroll: ScrollState,
        headerHeightPx: Float,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier
                .graphicsLayer {
                    translationY = -scroll.value.toFloat() / 2f // Parallax effect
                    alpha = (-1f / headerHeightPx) * scroll.value + 1
                }
        ) {
            ImageSlider(
                modifier = Modifier,
                images = attraction.value?.images?.map { it.src }.orEmpty(),
                noImageRes = R.drawable.no_image_holder2,
                contentScale = ContentScale.FillBounds,
                true
            ) {}
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.onSecondary
                            ),
                            startY = 3 * headerHeightPx / 4 // Gradient applied to wrap the title only
                        )
                    )
            )
        }
    }

    @Composable
    private fun Body(scroll: ScrollState, modifier: Modifier = Modifier, onClick: () -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(headerHeight))
            val state = attraction.value
            Text(
                text = state?.originalUrl.takeIf { it?.isNotBlank() == true } ?: "--",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        onClick()
                    }
            )

            state.introduction.split("\r\n\r\n").forEach {
                Row {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .padding(horizontal = 8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f))
                        )
                    }
                }
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    @Composable
    private fun Web(isShow: Boolean, click: () -> Unit) {
        val mUrl = attraction.value.originalUrl
        if (isShow) {
            val webViewChromeClient = object : WebChromeClient() {
                override fun onGeolocationPermissionsShowPrompt(
                    origin: String?,
                    callback: GeolocationPermissions.Callback
                ) {
//                    super.onGeolocationPermissionsShowPrompt(origin, callback);
                    callback.invoke(origin, true, false);
                    logD("rewfewe", "123")
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    // 回調網頁內容加載進度
//                    onProgressChange(newProgress)
                    super.onProgressChanged(view, newProgress)
                    logD("onProgressChanged_C", "onProgressChanged", newProgress)
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    // onReceivedTitle, 捷運中山站街區_心中山線形公園 | 臺北旅遊網
                    logD("onProgressChanged_C", "onReceivedTitle", title)
                }
            }
            val webViewClient = object : WebViewClient() {

                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    super.onPageCommitVisible(view, url)
                    logD("onProgressChanged_", "onPageCommitVisible")
                }

                override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
                    logD("onProgressChanged_", "shouldOverrideKeyEvent")
                    return super.shouldOverrideKeyEvent(view, event)

                }

                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    val url = request.url
                    if (url != null && url.toString().startsWith("https://www.google.com/maps")) {
                        view.context.startActivity(Intent(Intent.ACTION_VIEW, url))
                        return true
                    } else if (url.scheme == "intent") {
                        try {
                            logD("werwerwer", "try")
                            val intent = Intent.parseUri(url.toString(), Intent.URI_INTENT_SCHEME)
                            view.context.startActivity(intent)
                            return true
                        } catch (expected: ActivityNotFoundException) {
                            logD("werwerwer", expected)
                            return false
                        }
                    } else if (request?.url?.scheme == "tel") {
                        // 如果是电话号码链接，则打开系统的电话应用程序
                        val intent = Intent(Intent.ACTION_DIAL, request?.url)
                        startActivity(intent)
                        return true
                    } else {
//                        view.loadUrl(request.url.toString())
                        return super.shouldOverrideUrlLoading(view, request)
                    }
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
            var webView: WebView? = null
            val coroutineScope = rememberCoroutineScope()
            AndroidView(modifier = Modifier.fillMaxSize(), factory = { ctx ->
                WebView(ctx).apply {
                    this.setDownloadListener {

                            url, userAgent, contentDisposition, mimeType, contentLength ->
                        logD("Werwerrrrrr",url ,mimeType)
                        // 在这里处理下载请求
                        // url: 下载链接
                        // userAgent: 下载链接的用户代理
                        // contentDisposition: 下载内容描述
                        // mimeType: 下载文件的 MIME 类型
                        // contentLength: 下载文件的大小（字节）
//                        val request = DownloadManager.Request(Uri.parse(url))
//                            .setTitle("Download File") // 下载文件的标题
//                            .setDescription("Downloading") // 下载描述
//                            .setMimeType(mimeType) // 下载文件的 MIME 类型
//                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // 显示下载通知
//                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, null, null)) // 下载文件保存的路径
//
//                        val downloadManager = ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//                        downloadManager.enqueue(request)
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    }
//                    this.setFindListener()
//                    this.setAutofillHints()
//                    this.setBackgroundColor(Color.Black.toArgb())

                    this.settings.apply {
                        this.setGeolocationEnabled(true)
                        setJavaScriptEnabled(true);
                        setGeolocationEnabled(true);
                        val databasePath = ctx.getExternalFilesDir(null)?.path + "/geolocation"
                        setGeolocationDatabasePath(databasePath)
                        setDatabaseEnabled(true);
                        setAllowFileAccessFromFileURLs(true);
                        setAllowUniversalAccessFromFileURLs(true);
                        setJavaScriptCanOpenWindowsAutomatically(true);
                        setDomStorageEnabled(true);
                        setBuiltInZoomControls(true);
                        setAllowFileAccess(true);
                        setAllowContentAccess(true);
                        setSupportZoom(true);


                        setPluginState(WebSettings.PluginState.ON);
                        setMediaPlaybackRequiresUserGesture(false);
//                        forceDark = WebSettings.FORCE_DARK_ON // 强制开启夜间模式
                        // 允許 JS 交互
                        javaScriptEnabled = true
                        // 将图片调整到适合webView的大小
//                        useWideViewPort = true
                        // 縮放至螢幕大小
//                        loadWithOverviewMode = true
//                        setUseWideViewPort(true); //将图片调整到适合webview的大小
//                        setLoadWithOverviewMode(false); // 缩放至屏幕的大小
                        //缩放操作
                        setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
                        builtInZoomControls = true; //设置内置的缩放控件。若为false，则该WebView不可缩放
                        displayZoomControls = false; //隐藏原生的缩放控件


                        // 是否支援通過 JS 開啟新窗口
                        javaScriptCanOpenWindowsAutomatically = true
                        cacheMode = LOAD_CACHE_ELSE_NETWORK
                    }
                    this.webViewClient = webViewClient
                    this.webChromeClient = webViewChromeClient
//                    fun getHistoryUrls(): List<String> {
//                        val historyUrls: MutableList<String> = mutableListOf()
//                        val backStackEntryCount = webView.copyBackForwardList().size
//                        for (i in 0 until backStackEntryCount) {
//                            val historyItem = webView.copyBackForwardList().getItemAtIndex(i)
//                            val url = historyItem.url
//                            historyUrls.add(url)
//                        }
//                        return historyUrls
//                    }

                    webView = this
                    loadUrl(mUrl)
                }
            })
            if (isShow) {
                BackHandler {
                    if (webView?.canGoBack() == true) {
                        webView?.goBack()
                    } else {
                        click()
                        // finish()
                    }
                }
            }

//            CustomWebView(
//                modifier = Modifier.fillMaxSize(),
//                url = mUrl,
//                onProgressChange = { progress ->
//                  rememberWebViewProgress = progress
//                },
//                initSettings = { settings ->
//                    settings?.apply {
//                        // 允許 JS 交互
//                        javaScriptEnabled = true
//                        //将图片调整到适合webView的大小
//                        useWideViewPort = true
//                        // 縮放至螢幕大小
//                        loadWithOverviewMode = true
//                        // 縮放操作
//                        setSupportZoom(true)
//                        builtInZoomControls = true
//                        displayZoomControls = true
//                        // 是否支援通過 JS 開啟新窗口
//                        javaScriptCanOpenWindowsAutomatically = true
//                        // 不加載緩存內容
//                        cacheMode = WebSettings.LOAD_NO_CACHE
//                    }
//                },
//                onBack = { webView ->
//                    if (webView?.canGoBack() == true) {
//                        webView.goBack()
//                    } else {
//                        isShow.value = false
//                        // finish()
//                    }
//                },
//                onReceivedError = {
//
//                }
//            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TopBar(
        scroll: ScrollState,
        headerHeightPx: Float,
//        toolbarHeightPx: Float,
        modifier: Modifier = Modifier
    ) {
        val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }
        val toolbarBottom by remember {
            mutableStateOf(headerHeightPx - toolbarHeightPx)
        }

        val showToolbar by remember {
            derivedStateOf {
                scroll.value >= toolbarBottom
            }
        }

        AnimatedVisibility(
            modifier = modifier,
            visible = showToolbar,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            TopAppBar(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
                    .height(toolbarHeight),
                navigationIcon = {
                    val context = LocalContext.current
                    IconButton(
                        onClick = {
                            close()
//                            (context as? OnCloseListener)?.onClose()
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                title = {},
                colors = TopAppBarDefaults.smallTopAppBarColors(Color.Transparent)
            )
        }
    }

    @Composable
    private fun Title(
        scroll: ScrollState,
        modifier: Modifier = Modifier
    ) {
        var titleHeightPx by rememberSaveable { mutableStateOf(0f) }
        var titleWidthPx by rememberSaveable { mutableStateOf(0f) }

        AutoSizeText(
            text = attraction.value?.name ?: "",
            targetFontSize = 28.sp,
            minFontSize = 2.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .graphicsLayer {
                    val collapseRange: Float = (headerHeight.toPx() - toolbarHeight.toPx())
                    val collapseFraction: Float = (scroll.value / collapseRange).coerceIn(0f, 1f)

                    val scaleXY = lerp(
                        titleFontScaleStart.dp,
                        titleFontScaleEnd.dp,
                        collapseFraction
                    )

                    val titleExtraStartPadding = titleWidthPx.toDp() * (1 - scaleXY.value) / 2f

                    val titleYFirstInterpolatedPoint = lerp(
                        headerHeight - titleHeightPx.toDp() - paddingMedium,
                        headerHeight / 2,
                        collapseFraction
                    )

                    val titleXFirstInterpolatedPoint = lerp(
                        titlePaddingStart,
                        (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                        collapseFraction
                    )

                    val titleYSecondInterpolatedPoint = lerp(
                        headerHeight / 2,
                        toolbarHeight / 2 - titleHeightPx.toDp() / 2,
                        collapseFraction
                    )

                    val titleXSecondInterpolatedPoint = lerp(
                        (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                        titlePaddingEnd - titleExtraStartPadding,
                        collapseFraction
                    )

                    val titleY = lerp(
                        titleYFirstInterpolatedPoint,
                        titleYSecondInterpolatedPoint,
                        collapseFraction
                    )

                    val titleX = lerp(
                        titleXFirstInterpolatedPoint,
                        titleXSecondInterpolatedPoint,
                        collapseFraction
                    )

                    translationY = titleY.toPx()
                    translationX = titleX.toPx()
                    scaleX = scaleXY.value
                    scaleY = scaleXY.value
                }
                .onGloballyPositioned {
                    titleHeightPx = it.size.height.toFloat()
                    titleWidthPx = it.size.width.toFloat()
                }
        )
    }

    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun DefaultPreview() {
        CollapsingToolbar(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surface
                )
        )
    }

    @Preview(showBackground = true)
    @Composable
    private fun Preview() {
        TaipeiTourTheme {
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
    }

}
