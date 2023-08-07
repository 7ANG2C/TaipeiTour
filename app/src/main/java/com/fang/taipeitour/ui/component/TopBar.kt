package com.fang.taipeitour.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fang.taipeitour.R
import com.fang.taipeitour.dsl.Invoke

@Composable
fun TopBar(modifier: Modifier = Modifier, text: String, onClick: Invoke) {
    Surface(
        modifier = modifier,
        shadowElevation = 4.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_menu),
                contentDescription = "menu",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterStart)
                    .clickable {
                        onClick()
                    },
            )
            Text(
                text = text,
                modifier = Modifier
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
