package com.fang.taipeitour.ui.screen.home.attraction

import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import com.fang.taipeitour.R
import com.fang.taipeitour.extension.logD
import com.fang.taipeitour.model.attraction.Attraction
import com.fang.taipeitour.ui.component.ImageSlider
import com.fang.taipeitour.ui.component.dsl.stateValue
import com.fang.taipeitour.ui.theme.TaipeiTourTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * 景點導覽
 */
class AttractionFragment : Fragment() {

    companion object {
        private const val ARG_ID = "id"
        fun createIntent(id: Attraction): Fragment {
            return AttractionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ID, id)
                }
            }
        }
    }

    private val viewModel by viewModel<AttractionViewModel> {
        parametersOf(requireArguments().getParcelable<Attraction>(ARG_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CollapsingToolbarParallaxEffect(
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
    fun CollapsingToolbarParallaxEffect(modifier: Modifier = Modifier) {
        val scroll = rememberScrollState(0)

        val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
        val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }

        Box(modifier = modifier) {
            val isShow = remember {
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
                    (context as? OnCloseListener)?.onClose()
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
                images = viewModel.attractionState.stateValue().images.map { it.src }.orEmpty(),
                noImageRes = R.drawable.no_image_holder2,
                contentScale = ContentScale.Inside,
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
            val state = viewModel.attractionState.collectAsState().value
            Text(
                text = state.originalUrl.takeIf { it.isNotBlank() } ?: "--",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        onClick()
                    }
            )
            Text(
                text = state.officialSite.takeIf { it.isNotBlank() } ?: "--",
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
                logD("werrwe", it)
                Row {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
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

        val mUrl = viewModel.attractionState.collectAsState().value.originalUrl
        if (isShow) {
            val webViewChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    // 回調網頁內容加載進度
//                    onProgressChange(newProgress)
                    super.onProgressChanged(view, newProgress)
                }
            }
            val webViewClient = object : WebViewClient() {
                override fun onPageStarted(
                    view: WebView?,
                    url: String?,
                    favicon: Bitmap?
                ) {
                    super.onPageStarted(view, url, favicon)
//                    onProgressChange(-1)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
//                    onProgressChange(100)
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    if (null == request?.url) return false
                    val showOverrideUrl = request.url.toString()
                    try {
                        if (!showOverrideUrl.startsWith("") &&
                            !showOverrideUrl.startsWith("")
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
//                    onReceivedError(error)
                }
            }
            var webView: WebView? = null
            val coroutineScope = rememberCoroutineScope()
            AndroidView(modifier = Modifier.fillMaxSize(), factory = { ctx ->
                WebView(ctx).apply {
                    this.webViewClient = webViewClient
                    this.webChromeClient = webViewChromeClient
                    this.settings.apply {
                        // 允許 JS 交互
                        javaScriptEnabled = true
                        // 将图片调整到适合webView的大小
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
                            (context as? OnCloseListener)?.onClose()
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
        var titleHeightPx by remember { mutableStateOf(0f) }
        var titleWidthPx by remember { mutableStateOf(0f) }

        Text(
            text = viewModel.attractionState.stateValue().name,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
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
        CollapsingToolbarParallaxEffect(
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
