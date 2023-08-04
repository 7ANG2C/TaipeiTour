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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.fang.taipeitour.R
import com.fang.taipeitour.model.Action
import com.fang.taipeitour.model.ComposableInvoke
import com.fang.taipeitour.model.Invoke
import com.fang.taipeitour.model.language.getLocaleString
import com.fang.taipeitour.ui.component.dsl.LocalLanguage
import com.fang.taipeitour.ui.component.dsl.stateValue
import com.fang.taipeitour.ui.screen.home.HomeScreen
import com.fang.taipeitour.ui.screen.setting.SettingScreen
import com.fang.taipeitour.ui.theme.TaipeiTourTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = koinViewModel<MainViewModel>()
            val language = viewModel.languageState.collectAsState().value
            CompositionLocalProvider(LocalLanguage provides language) {
                val darkTheme =
                    viewModel.darkModeState.stateValue()?.enabled ?: isSystemInDarkTheme()
                var menuState by rememberSaveable {
                    mutableStateOf(ScreenMenu.HOME)
                }
                Screen(modifier = Modifier.fillMaxSize(), darkTheme = { darkTheme }) {
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
                                    drawerState.animateTo(DrawerValue.Closed, tween(500))
                                }
                            }
                        }
                    ) {

                        // main content
                        Crossfade(
                            targetState = menuState,
                            animationSpec = tween(400)
                        ) { menu ->
                            val scaleAlpha by animateFloatAsState(
                                targetValue = if (drawerState.targetValue == DrawerValue.Open) 0.9f else 1f,
                                animationSpec = tween(durationMillis = 300)
                            )
                            Box(Modifier.scale(scaleAlpha)) {
                                val coroutineScope = rememberCoroutineScope()
                                when (menu) {
                                    ScreenMenu.HOME -> HomeScreen() {
                                        coroutineScope.launch { drawerState.open() }
                                    }
                                    ScreenMenu.SETTING -> SettingScreen() {
                                        coroutineScope.launch { drawerState.open() }
                                    }
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
                            drawerState.isOpen -> coroutineScope.launch {
                                drawerState.close()
                            }
                            !menuState.isHome -> menuState = ScreenMenu.HOME
                            else -> showLeaveDialog = true
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Screen(
        modifier: Modifier,
        darkTheme: () -> Boolean,
        content: ComposableInvoke
    ) {
        TaipeiTourTheme(darkTheme = darkTheme()) {
            Surface(modifier = modifier, content = content)
        }
    }

    /**
     * 側邊 Menu
     */
    @Composable
    private fun MenuDrawer(onMenuSelected: Action<ScreenMenu>) {
        ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.7f)) {
            Column(modifier = Modifier.padding(16.dp)) {
                val language = LocalLanguage.current
                // avatar
                Row(
                    modifier = Modifier.padding(vertical = 40.dp),
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
                        text = language.getLocaleString(R.string.user_name),
                        fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // menu list
                LazyColumn {
                    items(ScreenMenu.all) { menu ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onMenuSelected(menu) },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(
                                    painter = painterResource(id = menu.icon),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = LocalLanguage.current.getLocaleString(
                                        res = menu.titleRes
                                    ),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(12.dp),
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            if (menu != ScreenMenu.all.last()) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
                if (BuildConfig.DEBUG) {
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
                val ver = language.getLocaleString(R.string.app_ver)
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
    private fun LeaveDialog(showLeaveDialog: Boolean, dismissDialog: Invoke) {
        if (showLeaveDialog) {
            val language = LocalLanguage.current
            AlertDialog(
                onDismissRequest = dismissDialog,
                confirmButton = {
                    Button(
                        onClick = {
                            dismissDialog.invoke()
                            finish()
                        }
                    ) {
                        Text(language.getLocaleString(R.string.leave_dialog_confirm))
                    }
                },
                title = {
                    Text("${language.getLocaleString(R.string.leave_dialog_title)} \uD83D\uDE80")
                },
                text = {
                    Text(language.getLocaleString(R.string.leave_dialog_text))
                }
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun Preview() {
        Screen(modifier = Modifier.fillMaxWidth(), { true }) {
            Text("Preview")
        }
    }
}
