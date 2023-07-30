package com.fang.taipeitour.ui.component.tabhorizontalpager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fang.taipeitour.ui.theme.Purple40
import com.fang.taipeitour.ui.theme.Purple80

/**
 * 客製化 Tab row
 *
 * @param modifier optional [Modifier] for this CustomTabRow
 * @param tabs tab items
 * @param selectIndex 當前選擇的 tab index
 * @param onTabSelect 點擊 tab 事件
 */
@Composable
fun CustomTabRow(
    modifier: Modifier = Modifier,
    tabs: List<CustomTab>,
    selectIndex: Int,
    tabSpace: Int = 8,
    onTabSelect: (tabIndex: Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val lastIndex = tabs.lastIndex
        tabs.forEachIndexed { index, tab ->
            Text(
                text = tab.tabName,
                fontSize = 12.sp,
                color = if (index == selectIndex) {
                    Purple40
                } else {
                    Purple80
                },
                modifier = if (index != selectIndex) {
                    Modifier.clickable {
                        onTabSelect(index)
                    }
                } else {
                    Modifier
                }
                    .padding(vertical = 4.dp, horizontal = 6.dp)
            )
            if (index != lastIndex) {
                Spacer(
                    modifier = Modifier
                        .width(tabSpace.dp)
                        .height(0.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var selectTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("First", "Second").map {
        object : CustomTab {
            override val tabName = it
        }
    }
    CustomTabRow(
        tabs = tabs,
        selectIndex = selectTabIndex,
        onTabSelect = { tabIndex ->
            selectTabIndex = tabIndex
        }
    )
}
