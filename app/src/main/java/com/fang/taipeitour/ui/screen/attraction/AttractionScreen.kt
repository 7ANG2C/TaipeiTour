package com.fang.taipeitour.ui.screen.attraction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fang.taipeitour.model.OnListItemClicked
import com.fang.taipeitour.service.Attraction
import com.module.imageslider.ImageSlider

@Composable
fun AttractionScreen(
    list: List<Attraction.Data>? = emptyList(),
    invoke: OnListItemClicked<Attraction.Data>
) {
    Surface {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list.orEmpty()) { item ->
                AttractionItem(
                    item, invoke
                )
            }
        }
    }
}

@Composable
private fun AttractionItem(
    item: Attraction.Data,
    invoke: OnListItemClicked<Attraction.Data>
) {
    Column(
        Modifier.clickable {
            invoke(item)
        }
    ) {
        ImageSlider(Modifier, item.images.map { it.src })
    }
}