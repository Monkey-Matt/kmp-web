/*
 * Copyright 2024 Vacasa LLC
 *
 */

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


/**
 * A wrapper for a scrollable view that adds a little shadow to the top and bottom when there is
 * content scrolled off screen making it appear that the content is going under whatever is above
 * or below the scrollable view
 */
@Composable
fun <T: ScrollableState> InternalShadowScrollable(
    modifier: Modifier = Modifier,
    listState: T,
    showTopShadow: Boolean = true,
    showBottomShadow: Boolean = true,
    content: @Composable BoxScope.(T) -> Unit
) {
    Box(modifier) {
        content(listState)

        val showHeaderShadow by remember {
            derivedStateOf { listState.canScrollBackward }
        }
        InternalShadow(
            modifier = Modifier.align(Alignment.TopCenter),
            showShadow = showHeaderShadow && showTopShadow,
            isTopShadow = true,
        )

        val showFooterShadow by remember {
            derivedStateOf { listState.canScrollForward }
        }
        InternalShadow(
            modifier = Modifier.align(Alignment.BottomCenter),
            showShadow = showFooterShadow && showBottomShadow,
            isTopShadow = false,
        )
    }
}

@Composable
fun InternalShadow(
    modifier: Modifier = Modifier,
    showShadow: Boolean,
    isTopShadow: Boolean,
) {
    val isDarkMode = !MaterialTheme.colors.isLight
    val shadowColor = if (isDarkMode) Color.White else Color.Black
    val shadowColors = if (isDarkMode) {
        listOf(shadowColor.copy(alpha = 0.1f), shadowColor.copy(alpha = 0.1f))
    } else {
        if (isTopShadow) {
            listOf(shadowColor.copy(alpha = 0.1f), Color.Transparent)
        } else {
            listOf(Color.Transparent, shadowColor.copy(alpha = 0.1f))
        }
    }
    val shadowHeight = if (isDarkMode) 1.dp else 3.dp

    AnimatedVisibility(
        modifier = modifier,
        visible = showShadow,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .height(shadowHeight)
                .fillMaxWidth()
                .background(brush = Brush.verticalGradient(colors = shadowColors))
        )
    }
}
