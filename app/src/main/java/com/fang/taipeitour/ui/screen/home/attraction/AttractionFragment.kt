package com.fang.taipeitour.ui.screen.home.attraction

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.fang.taipeitour.R
import com.fang.taipeitour.dsl.Action
import com.fang.taipeitour.dsl.Invoke
import com.fang.taipeitour.model.attraction.Attraction
import com.fang.taipeitour.model.language.getLocaleString
import com.fang.taipeitour.ui.component.AutoSizeText
import com.fang.taipeitour.ui.component.ImageSlider
import com.fang.taipeitour.ui.component.dsl.BackHandler
import com.fang.taipeitour.ui.component.dsl.LocalDarkMode
import com.fang.taipeitour.ui.component.dsl.LocalLanguage
import com.fang.taipeitour.ui.component.dsl.screenHeightDp
import com.fang.taipeitour.ui.screen.home.urlintroduction.UrlIntroductionScreen
import com.google.accompanist.flowlayout.FlowRow

/**
 * Single Attraction Fragment
 */
class AttractionFragment : Fragment() {

    companion object {
        private const val ARG = "argument"
        fun newInstance(argument: AttractionArgument): Fragment {
            return AttractionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG, argument)
                }
            }
        }
    }

    var onDismiss: Invoke? = null

    private val headerHeight = 245.dp
    private val topBarHeight = 56.dp

    private val titlePaddingStart = 16.dp
    private val titlePaddingEnd = 72.dp

    private val argument by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(ARG, AttractionArgument::class.java)
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable(ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val stateArgument by rememberSaveable { mutableStateOf(argument) }
                stateArgument?.let { argument ->
                    CollapsingScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                        argument = argument
                    )
                }
                BackHandler {
                    onDismiss?.invoke()
                }
            }
        }
    }

    @Composable
    private fun CollapsingScreen(modifier: Modifier = Modifier, argument: AttractionArgument) {
        var showUrlIntroduction by rememberSaveable {
            mutableStateOf(false)
        }
        val scrollState = rememberScrollState(0)
        val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
        val topBarHeightPx = with(LocalDensity.current) { topBarHeight.toPx() }
        val attraction = argument.attraction

        Box(modifier = modifier) {
            // main content
            Content(
                modifier = Modifier.fillMaxSize(),
                scrollState = scrollState,
                attraction = attraction,
            ) {
                showUrlIntroduction = true
            }

            // Header
            var selectedPage by rememberSaveable {
                mutableStateOf(0)
            }
            Header(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight),
                scrollPosition = scrollState.value,
                headerHeightPx = headerHeightPx,
                argument = argument,
            ) {
                selectedPage = it
            }

            // selected page hint
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .height(topBarHeight)
                    .padding(end = 16.dp),
            ) {
                Text(
                    text = if (attraction.images.isEmpty()) {
                        "1/1"
                    } else {
                        "${selectedPage + 1}/${attraction.images.size}"
                    },
                    modifier = Modifier.align(Alignment.CenterEnd),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End
                )
            }

            // back
            val interactionSource = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onDismiss?.invoke()
                    }
                    .size(topBarHeight)
                    .padding(start = 6.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                    tint = Color.White
                )
            }

            // top bar
            TopBar(
                scrollState = scrollState,
                headerHeightPx = headerHeightPx,
                topBarHeightPx = topBarHeightPx
            )

            // title
            Title(scroll = scrollState, text = attraction.name)

            // Url Introduction Screen
            Crossfade(targetState = showUrlIntroduction) { show ->
                if (show) {
                    UrlIntroductionScreen(attraction.originalUrl) {
                        showUrlIntroduction = false
                    }
                }
            }
        }
    }

    @Composable
    private fun Content(
        modifier: Modifier = Modifier,
        scrollState: ScrollState,
        attraction: Attraction,
        showUrlIntroduction: Invoke
    ) {
        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .heightIn(min = screenHeightDp.dp + headerHeight)

        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                // header spacer
                Spacer(Modifier.height(headerHeight))

                // base info section
                Spacer(Modifier.height(28.dp))
                SectionTitle(
                    R.drawable.ic_basic_info,
                    LocalLanguage.getLocaleString(R.string.base_info)
                )
                Spacer(Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    val webTitle = "．Web  "
                    listOf(
                        "．Add   " to attraction.address,
                        webTitle to attraction.originalUrl,
                        "．Tel    " to attraction.tel,
                        "．Fax   " to attraction.fax,
                        "．Eml   " to attraction.email,
                    ).forEachIndexed { i, (title, content) ->
                        if (content.isNotBlank()) {
                            if (i != 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            Row(Modifier.fillMaxWidth()) {
                                Text(
                                    text = title,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Text(
                                    text = content,
                                    modifier = if (title == webTitle) {
                                        Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(4.dp))
                                            .clickable {
                                                showUrlIntroduction()
                                            }
                                    } else {
                                        Modifier.weight(1f)
                                    },
                                    fontSize = 15.sp,
                                    color = if (title == webTitle) {
                                        Color(0xFF3974E9)
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                    textDecoration = if (title == webTitle) {
                                        TextDecoration.Underline
                                    } else {
                                        null
                                    }
                                )
                            }
                        }
                    }
                }

                // attraction intro section
                Spacer(Modifier.height(28.dp))
                SectionTitle(
                    R.drawable.ic_introduction,
                    LocalLanguage.getLocaleString(R.string.attraction_intro)
                )
                Spacer(Modifier.height(10.dp))
                attraction.introduction.split("\r\n\r\n")
                    .filterNot { it.isBlank() }.forEachIndexed { i, text ->
                        if (i != 0) {
                            // dot style divider
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(3) {
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.outlineVariant)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                }
                            }
                        }
                        SelectionContainer(
                            Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = text,
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                // attraction tag
                Spacer(Modifier.height(28.dp))
                SectionTitle(
                    R.drawable.ic_label,
                    LocalLanguage.getLocaleString(R.string.attraction_tag)
                )
                Spacer(Modifier.height(10.dp))
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    val categories = attraction.categories
                    val targets = attraction.targets
                    (categories + targets).forEach { category ->
                        val color = when (category) {
                            in categories -> MaterialTheme.colorScheme.tertiary
                            in targets -> MaterialTheme.colorScheme.primary
                            else -> null
                        }
                        SuggestionChip(
                            onClick = { },
                            label = {
                                Text(text = category.name, fontSize = 14.sp)
                            },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = color?.let {
                                SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = it.copy(alpha = 0.1f),
                                    labelColor = it.copy(alpha = 0.9f),
                                )
                            } ?: SuggestionChipDefaults.suggestionChipColors(),
                            border = color?.let {
                                SuggestionChipDefaults.suggestionChipBorder(
                                    borderColor = it.copy(alpha = 0.8f),
                                )
                            },
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f)) // for height in
            // end
            Spacer(Modifier.height(72.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(top = 6.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val color = MaterialTheme.colorScheme.outline.copy(alpha = 0.8f)
                Text(
                    text = "Taipei · Travel",
                    fontSize = 12.sp,
                    color = color
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "旅遊臺北",
                    fontSize = 11.sp,
                    color = color
                )
            }
        }
    }

    @Composable
    private fun SectionTitle(res: Int, text: String) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(res),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }

    @Composable
    private fun Header(
        modifier: Modifier = Modifier,
        scrollPosition: Int,
        headerHeightPx: Float,
        argument: AttractionArgument,
        onPageSelect: Action<Int>,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = -scrollPosition.toFloat() / 2f
                    alpha = (-1f / headerHeightPx) * scrollPosition + 1
                }
        ) {
            ImageSlider(
                modifier = Modifier.fillMaxWidth(),
                images = argument.attraction.images.map { it.src },
                noImageHolderRes = argument.noImageHolderRes,
                contentScale = ContentScale.FillBounds,
                showLoading = true,
                onPageSelect = onPageSelect
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                            ),
                            startY = headerHeightPx * 0.8f
                        )
                    )
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TopBar(
        modifier: Modifier = Modifier,
        scrollState: ScrollState,
        headerHeightPx: Float,
        topBarHeightPx: Float,
    ) {
        val toolbarBottom by rememberSaveable {
            mutableStateOf(headerHeightPx - topBarHeightPx)
        }

        val showToolbar by remember {
            derivedStateOf {
                scrollState.value >= toolbarBottom
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
                                MaterialTheme.colorScheme.onSecondary,
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surface,
                            )
                        )
                    )
                    .height(topBarHeight),
                navigationIcon = {
                    val interactionSource = remember { MutableInteractionSource() }
                    Box(
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                onDismiss?.invoke()
                            }
                            .padding(16.dp)
                            .size(24.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                title = {},
                colors = topAppBarColors(containerColor = Color.Transparent)
            )
        }
    }

    @Composable
    private fun Title(
        modifier: Modifier = Modifier,
        scroll: ScrollState,
        text: String
    ) {
        var titleHeightPx by rememberSaveable { mutableStateOf(0f) }
        var titleWidthPx by rememberSaveable { mutableStateOf(0f) }

        AutoSizeText(
            text = text,
            targetFontSize = 28.sp,
            minFontSize = 2.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (LocalDarkMode) {
                Color.White
            } else {
                MaterialTheme.colorScheme.inverseSurface
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .padding(end = 8.dp)
                .graphicsLayer {
                    val collapseRange = (headerHeight.toPx() - topBarHeight.toPx())
                    val collapseFraction = (scroll.value / collapseRange).coerceIn(0f, 1f)

                    val scaleXY = lerp(1f.dp, 0.66f.dp, collapseFraction)

                    val titleExtraStartPadding = titleWidthPx.toDp() * (1 - scaleXY.value) / 2f

                    val paddingMedium = 16.dp
                    val titleY1stInterpolatedPoint = lerp(
                        headerHeight - titleHeightPx.toDp() - paddingMedium,
                        headerHeight / 2,
                        collapseFraction
                    )

                    val titleX1stInterpolatedPoint = lerp(
                        titlePaddingStart,
                        (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                        collapseFraction
                    )

                    val titleY2ndInterpolatedPoint = lerp(
                        headerHeight / 2,
                        topBarHeight / 2 - titleHeightPx.toDp() / 2,
                        collapseFraction
                    )

                    val titleX2ndInterpolatedPoint = lerp(
                        (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                        titlePaddingEnd - titleExtraStartPadding,
                        collapseFraction
                    )

                    val titleY = lerp(
                        titleY1stInterpolatedPoint,
                        titleY2ndInterpolatedPoint,
                        collapseFraction
                    )

                    val titleX = lerp(
                        titleX1stInterpolatedPoint,
                        titleX2ndInterpolatedPoint,
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
}
