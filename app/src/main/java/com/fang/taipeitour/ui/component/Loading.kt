package com.fang.taipeitour.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Loading(
    modifier: Modifier = Modifier,
    isFancy: Boolean = true,
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent,
    ) {
        Box(
            modifier =
                Modifier.size(
                    if (isFancy) 42.dp else 32.dp,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (isFancy) {
                CircularProgressIndicator(
                    modifier = Modifier.size(42.dp),
                    color = Color.Transparent,
                    strokeWidth = 2.dp,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                )
            }
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            )
        }
    }
}
