package com.fang.taipeitour.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.fang.taipeitour.dsl.Invoke
import com.fang.taipeitour.ui.component.dsl.screenHeightDp

/**
 * Scrollable Themed Dialog
 */
@Composable
fun CustomDialog(
    onDismiss: Invoke,
    content: @Composable BoxScope.() -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(0.dp, (screenHeightDp * 0.9f).dp),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.onSecondary,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center,
                content = content,
            )
        }
    }
}
