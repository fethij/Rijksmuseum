package com.tewelde.rijksmuseum.feature.arts.gallery.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.max

private const val MULTIPLIER_SELECTED_PAGE = 4
private val baseWidth = 8.dp
private val height = 3.dp

@Composable
fun RijksmuseumPager(
    pagerState: PagerState,
    indicatorColor: Color = Color.White
) {
    val indicatorScrollState = rememberLazyListState()
    LaunchedEffect(pagerState.currentPage) {
        val currentPage = pagerState.currentPage
        val size = indicatorScrollState.layoutInfo.visibleItemsInfo.size
        if (size == 0) return@LaunchedEffect
        val lastVisibleIndex = indicatorScrollState.layoutInfo.visibleItemsInfo.last().index
        val firstVisibleItemIndex = indicatorScrollState.firstVisibleItemIndex

        if (currentPage > lastVisibleIndex - 1) {
            indicatorScrollState.animateScrollToItem(currentPage - size + 2)
        } else if (currentPage <= firstVisibleItemIndex + 1) {
            indicatorScrollState.animateScrollToItem(max(currentPage - 1, 0))
        }
    }

    LazyRow(
        state = indicatorScrollState,
        modifier = Modifier
            .width(((6 + 16) * 2 + 3 * (10 + 16)).dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { iteration ->
            item(key = iteration) {
                val currentPage = pagerState.currentPage
                val offsetIntPart = pagerState.currentPageOffsetFraction.toInt()
                val offsetFractionalPart = pagerState.currentPageOffsetFraction - offsetIntPart
                val targetPage =
                    if (pagerState.currentPageOffsetFraction < 0) currentPage - 1 else currentPage + 1
                val currentPageWidth =
                    baseWidth * (1 + (1 - abs(offsetFractionalPart)) * MULTIPLIER_SELECTED_PAGE)
                val targetPageWidth =
                    baseWidth * (1 + abs(offsetFractionalPart) * MULTIPLIER_SELECTED_PAGE)
                val width = when (iteration) {
                    currentPage -> currentPageWidth
                    targetPage -> targetPageWidth
                    else -> baseWidth
                }

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            indicatorColor,
                            RoundedCornerShape(50)
                        )
                        .height(height)
                        .width(width)
                )
            }
        }
    }
}