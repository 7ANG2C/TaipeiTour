package com.fang.taipeitour

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fang.taipeitour.ui.component.FragmentContainer
import com.fang.taipeitour.ui.component.tabhorizontalpager.CustomTabPage
import com.fang.taipeitour.ui.component.tabhorizontalpager.TabPage
import com.fang.taipeitour.ui.screen.attraction.AttractionScreen
import com.fang.taipeitour.ui.screen.attraction.guide.AttractionGuideFragment
import com.fang.taipeitour.ui.screen.attraction.guide.AttractionGuideViewModel
import com.fang.taipeitour.ui.screen.attraction.guide.AttractionListener
import com.fang.taipeitour.ui.screen.setting.SettingScreen
import com.fang.taipeitour.ui.theme.TaipeiTourTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<AttractionGuideViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Screen()
        }
        iniOnBackPressedDispatcher()
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
                    mutableStateOf(false)
                }
                TabPage(
                    modifier = Modifier.fillMaxSize(),
                    tabPages = listOf(
                        object : CustomTabPage {
                            override val tabName = "List"

                            @Composable
                            override fun PageScreen() {
                                AttractionScreen(
                                    viewModel.state.collectAsState().value
                                ) {
                                    isShowGuide.value = true
                                }
                            }
                        },
                        object : CustomTabPage {
                            override val tabName = "Setting"

                            @Composable
                            override fun PageScreen() {
                                SettingScreen()
                            }
                        }
                    ),
                    tabSpace = 8,
                    selectIndex = index.value,
                    onTabSelect = {
                        index.value = it
                    },
                )
                if (isShowGuide.value) {
                    FragmentContainer(
                        modifier = Modifier.fillMaxSize(),
                        fragment = AttractionGuideFragment.createIntent()
                    ) {
                        (this as? AttractionListener)?.onAttraction(index.value)
                    }
                }
            }
        }
    }

    private fun iniOnBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 先判斷是否有 Guide Fragment
                }
            }
        )
    }

    @Preview(showBackground = true)
    @Composable
    private fun ScreenPreview() {
        Screen()
    }
}
