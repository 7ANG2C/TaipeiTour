package com.fang.taipeitour

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fang.taipeitour.model.attraction.Attraction2
import com.fang.taipeitour.ui.component.CustomImage
import com.fang.taipeitour.ui.component.FragmentContainer
import com.fang.taipeitour.ui.component.TopBar
import com.fang.taipeitour.ui.screen.attraction.AttractionScreen
import com.fang.taipeitour.ui.screen.attraction.AttractionViewModel
import com.fang.taipeitour.ui.screen.attraction.guide.AttractionGuideFragment
import com.fang.taipeitour.ui.screen.setting.SettingScreen
import com.fang.taipeitour.ui.screen.setting.SettingViewModel
import com.fang.taipeitour.ui.theme.TaipeiTourTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    enum class Page {
        HOME, SETTING
    }

    private val viewModel by viewModel<AttractionViewModel>()
    private val settingViewModel by viewModel<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @OptIn(ExperimentalMaterial3Api::class)
        setContent {
            val current = remember {
                mutableStateOf(Page.HOME)
            }
            TaipeiTourTheme(darkTheme = current.value == Page.HOME) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isShowGuide by remember {
                        mutableStateOf<Attraction2?>(null)
                    }

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
                                ModalDrawerSheet {
                                    Column() {
                                        Row {
                                            CustomImage(res = R.drawable.avatar)
                                            Text("User")
                                        }
                                        Text(Page.HOME.name, Modifier.clickable {
                                            coroutine.launch {
                                                current.value = Page.HOME
                                                drawerState.close()
                                            }
                                        })
                                        Text("SETTING", Modifier.clickable {
                                            coroutine.launch {
                                                current.value = Page.SETTING
                                                drawerState.close()
                                            }
                                        })
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text("APP VER ${BuildConfig.VERSION_CODE}")
                                    }
                                }
                            }
                        ) {
                            Column {
                                TopBar(text = "TITLE") {
                                    coroutine.launch {
                                        drawerState.open()
                                    }
                                }
                                when (current.value) {
                                    Page.HOME -> AttractionScreen(
                                        viewModel.state.collectAsState().value
                                    ) {
                                        isShowGuide = it
                                    }
                                    Page.SETTING -> SettingScreen(settingViewModel)
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        modifier = Modifier.fillMaxSize(),
                        visible = isShowGuide != null,
                        enter = fadeIn(animationSpec = tween(3000)),
                        exit = fadeOut(animationSpec = tween(3000))
                    ) {
                        isShowGuide?.let {
                            FragmentContainer(
                                modifier = Modifier.fillMaxSize(),
                                fragment = AttractionGuideFragment.createIntent(it),
                                update = { /* no need update */ }
                            )
                        }
                    }

                    if (dialog.value) {
                        AlertDialog(onDismissRequest = { }, confirmButton = {
                            Button({ finish() }, modifier = Modifier) {
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
                                    drawerState.close()
                                }
                            }
                            isShowGuide != null -> isShowGuide = null
                            current.value == Page.SETTING -> current.value = Page.HOME
                            else -> {
                                dialog.value = true
                            }
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
                val index = remember {
                    mutableStateOf(0)
                }
                val isShowGuide = remember {
                    mutableStateOf<Attraction2?>(null)
                }

                val isShowGuideValue = isShowGuide.value
                if (isShowGuideValue != null) {
//                    FragmentContainer(
//                        modifier = Modifier.fillMaxSize(),
//                        fragment = AttractionGuideFragment.createIntent()
//                    ) {
//                        (this as? AttractionListener)?.onAttraction(sfdsfd)
//                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun ScreenPreview() {
        Screen()
    }
}
