package com.fang.taipeitour.ui.main

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fang.taipeitour.BuildConfig
import com.fang.taipeitour.FeatureFlag
import com.fang.taipeitour.R
import com.fang.taipeitour.ui.component.FragmentContainer
import com.fang.taipeitour.ui.component.TopBar
import com.fang.taipeitour.ui.component.dsl.stateValue
import com.fang.taipeitour.ui.screen.attraction.AttractionScreen
import com.fang.taipeitour.ui.screen.attraction.AttractionViewModel
import com.fang.taipeitour.ui.screen.attraction.guide.AttractionGuideFragment
import com.fang.taipeitour.ui.screen.attraction.guide.OnCloseListener
import com.fang.taipeitour.ui.screen.setting.SettingScreen
import com.fang.taipeitour.ui.screen.setting.SettingViewModel
import com.fang.taipeitour.ui.theme.TaipeiTourTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), OnCloseListener {

    private val mainViewModel by viewModel<MainViewModel>()
    private val viewModel by viewModel<AttractionViewModel>()
    private val settingViewModel by viewModel<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @OptIn(ExperimentalMaterial3Api::class)
        setContent {
            val current = remember {
                mutableStateOf(ScreenMenu.HOME)
            }
            TaipeiTourTheme(darkTheme = mainViewModel.darkModeState.stateValue().enabled) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isShowGuide = viewModel.guideState.collectAsState().value != null
                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    val coroutine = rememberCoroutineScope()
                    val dialog = remember {
                        mutableStateOf(false)
                    }
                    Column() {
                        ModalNavigationDrawer(
                            modifier = Modifier.weight(1f),
                            drawerState = drawerState,
                            gesturesEnabled = drawerState.isOpen,
                            drawerContent = {
                                MenuDrawer {
                                    current.value = it
                                    coroutine.launch {
                                        drawerState.animateTo(DrawerValue.Closed, tween(800))
                                    }
                                }
                            }
                        ) {
                            Column {
                                TopBar(text = current.value.title) {
                                    coroutine.launch {
                                        drawerState.open()
                                    }
                                }

                                Crossfade(
                                    targetState = current.value,
                                    animationSpec = tween(500)
                                ) { selectedColor ->
                                    val scaleAlpha: Float by animateFloatAsState(
                                        targetValue = if (drawerState.targetValue == DrawerValue.Open) .9f else 1f,
                                        animationSpec = tween(durationMillis = 300)
                                    )
                                    Box(Modifier.scale(scaleAlpha)) {
                                        when (selectedColor) {
                                            ScreenMenu.HOME -> AttractionScreen(
                                                viewModel
                                            ) {
                                                viewModel.setAttractionGuide(it)
                                            }
                                            ScreenMenu.SETTING -> SettingScreen(settingViewModel)
                                        }
                                    }

                                }
                            }
                        }
                    }

                    val scale by animateFloatAsState(
                        targetValue = if (isShowGuide) 1f else .9f,
                        tween(400)
//                        animationSpec = spring(
//                            dampingRatio = Spring.DampingRatioMediumBouncy,
//                        )
                    )
                    Crossfade(
                        targetState = viewModel.guideState.collectAsState().value,
                        animationSpec = tween(400)
                    ) { attr ->
                        attr?.let {
                            FragmentContainer(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .scale(scale),
                                fragment = AttractionGuideFragment.createIntent(it),
                                update = { /* no need update */ }
                            )
                        }
                    }

                    if (dialog.value) {
                        AlertDialog(onDismissRequest = { dialog.value = false }, confirmButton = {
                            Button({
                                dialog.value = false
                                finish()
                            }, modifier = Modifier) {
                                Text("confirmButton")
                            }
                        }, title = {
                            Text("title")
                        }, text = {
                            Text("text")
                        })
                    }
                    BackHandler(
                        // your condition to enable handler
//                enabled = isOverlayPresented
                    ) {
                        when {
                            drawerState.isOpen -> {
                                coroutine.launch {
                                    drawerState.animateTo(DrawerValue.Closed, tween(1000))
                                }
                            }
                            isShowGuide -> {
                                viewModel.setAttractionGuide(null)
                            }
                            current.value != ScreenMenu.HOME -> current.value = ScreenMenu.HOME
                            else -> dialog.value = true
                        }
                        // your action to be called if back handler is enabled
//                isOverlayPresented = false
                    }
                }
            }
        }
    }

    @Composable
    private fun Screen() {
        TaipeiTourTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {

            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MenuDrawer(modifier: Modifier = Modifier, onMenuSelected: (ScreenMenu) -> Unit) {
        ModalDrawerSheet(modifier = modifier) {
            Column(modifier = Modifier.padding(16.dp)) {
                // avatar
                Row(
                    modifier = Modifier
                        .padding(vertical = 40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = "avatar image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Lisa",
                        fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // menu list
                LazyColumn {
                    items(ScreenMenu.all) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable { onMenuSelected(it) },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(id = it.icon),
                                contentDescription = it.title,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = it.title,
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 16.dp),
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
                if (FeatureFlag.Flag.value) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(text = "primary", color = MaterialTheme.colorScheme.primary)
                        Text(text = "onPrimary", color = MaterialTheme.colorScheme.onPrimary)
                        Text(
                            text = "primaryContainer",
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                        Text(
                            text = "onPrimaryContainer",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "inversePrimary",
                            color = MaterialTheme.colorScheme.inversePrimary
                        )

                        Text(text = "secondary", color = MaterialTheme.colorScheme.secondary)
                        Text(text = "onSecondary", color = MaterialTheme.colorScheme.onSecondary)
                        Text(
                            text = "secondaryContainer",
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                        Text(
                            text = "onSecondaryContainer",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        Text(text = "tertiary", color = MaterialTheme.colorScheme.tertiary)
                        Text(text = "onTertiary", color = MaterialTheme.colorScheme.onTertiary)
                        Text(
                            text = "tertiaryContainer",
                            color = MaterialTheme.colorScheme.tertiaryContainer
                        )
                        Text(
                            text = "onTertiaryContainer",
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )

                        Text(text = "background", color = MaterialTheme.colorScheme.background)
                        Text(text = "onBackground", color = MaterialTheme.colorScheme.onBackground)
                        Text(text = "surface", color = MaterialTheme.colorScheme.surface)
                        Text(text = "onSurface", color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            text = "surfaceVariant",
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Text(
                            text = "onSurfaceVariant",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(text = "surfaceTint", color = MaterialTheme.colorScheme.surfaceTint)
                        Text(
                            text = "inverseSurface",
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                        Text(
                            text = "inverseOnSurface",
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                        Text(text = "error", color = MaterialTheme.colorScheme.error)
                        Text(text = "onError", color = MaterialTheme.colorScheme.onError)
                        Text(
                            text = "errorContainer",
                            color = MaterialTheme.colorScheme.errorContainer
                        )
                        Text(
                            text = "onErrorContainer",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(text = "outline", color = MaterialTheme.colorScheme.outline)
                        Text(
                            text = "outlineVariant",
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        Text(text = "scrim", color = MaterialTheme.colorScheme.scrim)
                    }
                } else {
                    // space divide
                    Spacer(modifier = Modifier.weight(1f))
                }

                // version
                Text(
                    text = "App version: ${BuildConfig.VERSION_NAME}",
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun ScreenPreview() {
        Screen()
    }

    override fun onClose() {
        viewModel.setAttractionGuide(null)
    }
}
