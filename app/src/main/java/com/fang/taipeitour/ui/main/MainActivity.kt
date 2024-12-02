package com.fang.taipeitour.ui.main

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fang.taipeitour.BuildConfig
import com.fang.taipeitour.R
import com.fang.taipeitour.dsl.Action
import com.fang.taipeitour.dsl.ComposableInvoke
import com.fang.taipeitour.dsl.Invoke
import com.fang.taipeitour.model.language.Language
import com.fang.taipeitour.model.language.getLocaleString
import com.fang.taipeitour.ui.component.dsl.LocalDarkMode
import com.fang.taipeitour.ui.component.dsl.LocalLanguage
import com.fang.taipeitour.ui.component.dsl.LocalStaticPreferences
import com.fang.taipeitour.ui.component.dsl.stateValue
import com.fang.taipeitour.ui.screen.home.HomeScreen
import com.fang.taipeitour.ui.screen.setting.SettingScreen
import com.fang.taipeitour.ui.theme.TaipeiTourTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = koinViewModel<MainViewModel>()
            viewModel.preferencesState.stateValue()?.let { preferences ->
                CompositionLocalProvider(LocalStaticPreferences provides preferences) {
                    var menuState by rememberSaveable {
                        mutableStateOf(ScreenMenu.HOME)
                    }
                    Screen(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(
                                    animateColorAsState(
                                        targetValue = if (LocalDarkMode) Color.Black else Color.White,
                                        label = "background",
                                    ).value,
                                )
                                .statusBarsPadding()
                                .navigationBarsPadding(),
                    ) {
                        val drawerState = rememberDrawerState(DrawerValue.Closed)
                        ModalNavigationDrawer(
                            modifier = Modifier.fillMaxSize(),
                            drawerState = drawerState,
                            gesturesEnabled = drawerState.isOpen,
                            drawerContent = {
                                val coroutineScope = rememberCoroutineScope()
                                MenuDrawer {
                                    menuState = it
                                    coroutineScope.launch {
                                        drawerState.snapTo(DrawerValue.Closed)
                                    }
                                }
                            },
                        ) {
                            // main content
                            Crossfade(
                                targetState = menuState,
                                animationSpec = tween(400),
                                label = "main_content",
                            ) { menu ->
                                val scale by animateFloatAsState(
                                    targetValue =
                                        if (drawerState.targetValue == DrawerValue.Open) {
                                            0.9f
                                        } else {
                                            1f
                                        },
                                    animationSpec = tween(durationMillis = 300),
                                    label = "scale",
                                )
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .scale(scale),
                                ) {
                                    val coroutineScope = rememberCoroutineScope()
                                    val onMenuClicked: Invoke = {
                                        coroutineScope.launch { drawerState.open() }
                                    }
                                    when (menu) {
                                        ScreenMenu.HOME -> HomeScreen(onMenuClicked = onMenuClicked)
                                        ScreenMenu.SETTING -> SettingScreen(onMenuClicked = onMenuClicked)
                                    }
                                }
                            }
                        }
                        var showLeaveDialog by rememberSaveable {
                            mutableStateOf(false)
                        }
                        LeaveDialog(showLeaveDialog) {
                            showLeaveDialog = false
                        }
                        val coroutineScope = rememberCoroutineScope()
                        BackHandler {
                            when {
                                drawerState.isOpen ->
                                    coroutineScope.launch {
                                        drawerState.close()
                                    }
                                menuState != ScreenMenu.HOME -> menuState = ScreenMenu.HOME
                                else -> showLeaveDialog = true
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Screen(
        modifier: Modifier,
        content: ComposableInvoke,
    ) {
        TaipeiTourTheme {
            Surface(modifier = modifier, content = content)
        }
    }

    /**
     * 側邊 Menu
     */
    @Composable
    private fun MenuDrawer(onMenuSelected: Action<ScreenMenu>) {
        ModalDrawerSheet {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth(0.7f)
                        .padding(16.dp),
            ) {
                // avatar
                Row(
                    modifier = Modifier.padding(vertical = 40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val avatar =
                        when (LocalLanguage) {
                            Language.TAIWAN -> R.drawable.avatar_tw
                            Language.CHINA -> R.drawable.avatar_cn
                            Language.ENGLISH -> R.drawable.avatar_en
                            Language.JAPAN -> R.drawable.avatar_jp
                            Language.KOREA -> R.drawable.avatar_ko
                            Language.SPAN -> R.drawable.avatar_es
                            Language.INDONESIA -> R.drawable.avatar_id
                            Language.THAILAND -> R.drawable.avatar_th
                            Language.VIETNAM -> R.drawable.avatar_vn
                        }
                    Image(
                        painter = painterResource(avatar),
                        contentDescription = "avatar image",
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .size(50.dp)
                                .clip(shape = CircleShape),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = LocalLanguage.getLocaleString(R.string.user_name),
                        fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }

                // menu list
                LazyColumn {
                    items(ScreenMenu.all) { menu ->
                        Column {
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { onMenuSelected(menu) },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(
                                    painter = painterResource(menu.icon),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp),
                                )
                                Text(
                                    text = LocalLanguage.getLocaleString(menu.titleRes),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(12.dp),
                                    fontWeight = FontWeight.Normal,
                                )
                            }
                            if (menu != ScreenMenu.all.last()) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }

                // space divide
                Spacer(modifier = Modifier.weight(1f))

                // version
                val ver = LocalLanguage.getLocaleString(R.string.app_ver)
                Text(
                    text = "$ver: ${BuildConfig.VERSION_NAME}",
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                )
            }
        }
    }

    @Composable
    private fun LeaveDialog(
        showLeaveDialog: Boolean,
        dismissDialog: Invoke,
    ) {
        if (showLeaveDialog) {
            AlertDialog(
                onDismissRequest = dismissDialog,
                confirmButton = {
                    Button(
                        onClick = {
                            dismissDialog.invoke()
                            finish()
                        },
                    ) {
                        Text(LocalLanguage.getLocaleString(R.string.leave_dialog_confirm))
                    }
                },
                title = {
                    Text("${LocalLanguage.getLocaleString(R.string.leave_dialog_title)} \uD83D\uDE80")
                },
                text = {
                    Text(LocalLanguage.getLocaleString(R.string.leave_dialog_text))
                },
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun Preview() {
        Screen(modifier = Modifier.fillMaxWidth()) {
            Text("Preview")
        }
    }
}
