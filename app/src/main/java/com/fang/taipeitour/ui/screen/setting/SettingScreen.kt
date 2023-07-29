package com.fang.taipeitour.ui.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.fang.taipeitour.model.Language
import com.fang.taipeitour.model.get
import com.fang.taipeitour.util.screenHeightDp
import org.koin.core.context.GlobalContext

/**
 * 使用者設定 Screen
 */
@Composable
fun SettingScreen(settingViewModel: SettingViewModel) {

    Column {
        val set = settingViewModel.state.collectAsState()
        val settingList = remember {
            mutableStateOf(GlobalContext.get().get<SettingFlavorBehavior>().apply())
        }

        val isShow = remember {
            mutableStateOf(false)
        }

        ElevatedCard {
            Row {
                RadioButton(selected = true, onClick = {

                })
                Spacer(modifier = Modifier.weight(1f))
                Text(settingList.toString(), Modifier.clickable {
                    isShow.value = true
                })
            }
        }

        ElevatedCard {
            Row {
                RadioButton(selected = true, onClick = {

                })
                Text("DarkMode", Modifier.clickable {
                    isShow.value = true
                })
            }
        }

        val context = LocalContext.current
        if (isShow.value) {
            BaseDialog(onDismiss = { isShow.value = false }) {
                Column {
                    repeat(Language.values().size) {
                        Row {
                            RadioButton(selected = true, onClick = {

                            })
                            Text(text = Language.values()[it].get(context, "zh", "TW"))
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun BaseDialog(onDismiss: () -> Unit, content: @Composable BoxScope.() -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        val dialogRound = RoundedCornerShape(8.dp)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(0.dp, (screenHeightDp * 0.9f).dp),
            shape = dialogRound,
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black, dialogRound)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center,
                content = content
            )
        }
    }
}
