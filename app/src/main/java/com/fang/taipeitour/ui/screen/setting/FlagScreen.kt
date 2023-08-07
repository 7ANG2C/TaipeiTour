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

private val colorFlagRed = Color(0xFFE94E4E)
private val colorFlagYellow = Color(0xFFF8CC44)
private val colorFlagBlue = Color(0xFF3C4EB1)
private val colorFlagWhite = Color(0xFFF1F1F1)

@Composable
fun FlagTw() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFlagRed)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth(0.25f)
                .clip(RoundedCornerShape(bottomEnd = 4.dp))
                .background(colorFlagBlue)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_flag_tw_sun),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center),
                tint = colorFlagWhite
            )
        }
    }
}

@Composable
fun FlagCn() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFlagRed)
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
                tint = colorFlagYellow
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
                        tint = colorFlagYellow
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
                val color = if (it % 2 == 0) colorFlagRed else colorFlagWhite
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
                .background(colorFlagBlue),
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
                            tint = colorFlagWhite
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
            .background(colorFlagWhite)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(colorFlagRed)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun FlagKo() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFlagWhite),
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
            .background(colorFlagRed)
    ) {
        Box(
            modifier = Modifier
                .background(colorFlagYellow)
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
                tint = colorFlagRed
            )
        }
    }
}

@Composable
fun FlagId() {
    Column(modifier = Modifier.fillMaxSize()) {
        listOf(
            colorFlagRed to 1f,
            colorFlagWhite to 1f,
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
            colorFlagRed to 0.7f,
            colorFlagWhite to 0.9f,
            colorFlagBlue to 1.2f,
            colorFlagWhite to 0.9f,
            colorFlagRed to 0.7f,
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
            .background(colorFlagRed)

    ) {
        Icon(
            painter = painterResource(R.drawable.ic_star),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center),
            tint = colorFlagYellow
        )
    }
}
