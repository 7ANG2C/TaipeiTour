package com.fang.taipeitour.ui.component.tabhorizontalpager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun TabPage(
    modifier: Modifier = Modifier,
    tabPages: List<CustomTabPage>,
    selectIndex: Int,
    tabSpace: Int = 8,
    onTabSelect: (tabIndex: Int) -> Unit = {},
) {
    Column(modifier = modifier) {
        Box(Modifier.weight(1f)) {
            tabPages[selectIndex].PageScreen()
        }
        CustomTabRow(
            tabSpace = tabSpace,
            tabs = tabPages,
            selectIndex = selectIndex
        ) { tabIndex ->
            onTabSelect(tabIndex)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TabPage(
        modifier = Modifier.fillMaxSize(),
        tabPages = listOf(
            object : CustomTabPage {
                override val tabName = "First"

                @Composable
                override fun PageScreen() {
                    Text(text = "First page", fontSize = 16.sp, color = Color.Gray)
                }
            },
            object : CustomTabPage {
                override val tabName = "Second"

                @Composable
                override fun PageScreen() {
                    Text(text = "Second page", fontSize = 16.sp, color = Color.Gray)
                }
            }
        ),
        tabSpace = 8,
        selectIndex = 1,
        onTabSelect = {},
    )
}
