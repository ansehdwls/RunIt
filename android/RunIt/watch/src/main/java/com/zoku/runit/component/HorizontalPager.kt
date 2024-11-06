package com.zoku.runit.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import com.google.android.horologist.compose.layout.PagerScaffold

@OptIn(ExperimentalFoundationApi::class, ExperimentalWearFoundationApi::class)
@Composable
fun PagerScreen(
    modifier: Modifier = Modifier,
    state: PagerState,
    items: List<@Composable () -> Unit>,
) {
    PagerScaffold(
        modifier = modifier,
        pagerState = state,
    ) {
        HorizontalPager(
            modifier = modifier,
            state = state,
        ) { page ->
            items.getOrNull(page)?.invoke()
        }
    }
}