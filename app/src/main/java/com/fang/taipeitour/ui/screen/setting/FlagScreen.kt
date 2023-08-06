package com.fang.taipeitour.ui.screen.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fang.taipeitour.R

private val ColorFlagRed = Color(0xFFE94E4E)
private val ColorFlagYellow = Color(0xFFF8CC44)
private val ColorFlagBlue = Color(0xFF3C4EB1)
private val ColorFlagWhite = Color(0xFFF1F1F1)

@Composable
fun FlagTw() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFlagRed)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth(0.25f)
                .clip(RoundedCornerShape(bottomEnd = 4.dp))
                .background(ColorFlagBlue)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_flag_tw_sun),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center),
                tint = ColorFlagWhite
            )
        }
    }
}

@Composable
fun FlagCn() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFlagRed)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 16.dp),
                tint = ColorFlagYellow
            )
            Column {
                repeat(4) {
                    val padding = if (it % 3 == 0) 0 else 11
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = null,
                        modifier =
                        Modifier
                            .padding(start = padding.dp)
                            .weight(1f)
                            .scale(0.5f),
                        tint = ColorFlagYellow
                    )
                }
            }
        }
    }
}

@Composable
fun FlagEn() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            (0..4).forEach {
                val color = if (it % 2 == 0) ColorFlagRed else ColorFlagWhite
                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(color)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(0.3f)
                .clip(RoundedCornerShape(bottomEnd = 4.dp))
                .background(ColorFlagBlue),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(4) {
                val count = if (it % 2 == 0) 6 else 5
                val padding = if (it % 2 == 0) 1f else 5f / 6f
                Row(
                    modifier = Modifier
                        .fillMaxWidth(padding)
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(count) {
                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = null,
                            modifier = Modifier.scale(0.6f),
                            tint = ColorFlagWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FlagJp() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFlagWhite)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(ColorFlagRed)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun FlagKo() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFlagWhite),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val color = Color(0xFF0F111D)
        Icon(
            painter = painterResource(R.drawable.ic_menu),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .scale(0.6f)
                .rotate(315f),
            tint = color
        )
        Image(
            painter = painterResource(R.drawable.ic_ko_tai_chi),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .aspectRatio(1f),
        )
        Icon(
            painter = painterResource(R.drawable.ic_menu),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .scale(0.6f)
                .rotate(315f),
            tint = color
        )
    }
}

@Composable
fun FlagEs() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFlagRed)
    ) {
        Box(
            modifier = Modifier
                .background(ColorFlagYellow)
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .align(Alignment.Center)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_flag_es_shield),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp),
                tint = ColorFlagRed
            )
        }
    }
}

@Composable
fun FlagId() {
    Column(modifier = Modifier.fillMaxSize()) {
        listOf(
            ColorFlagRed to 1f,
            ColorFlagWhite to 1f,
        ).forEach { (color, weight) ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(weight)
                    .background(color)
            )
        }
    }
}

@Composable
fun FlagTh() {
    Column(modifier = Modifier.fillMaxSize()) {
        listOf(
            ColorFlagRed to 0.7f,
            ColorFlagWhite to 0.9f,
            ColorFlagBlue to 1.2f,
            ColorFlagWhite to 0.9f,
            ColorFlagRed to 0.7f,
        ).forEach { (color, weight) ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(weight)
                    .background(color)
            )
        }
    }
}

@Composable
fun FlagVn() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFlagRed)

    ) {
        Icon(
            painter = painterResource(R.drawable.ic_star),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center),
            tint = ColorFlagYellow
        )
    }
}
