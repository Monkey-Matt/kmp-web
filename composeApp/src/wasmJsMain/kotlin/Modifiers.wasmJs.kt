import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.abs

@Composable
actual fun Modifier.verticalScrollWithTouch(
    state: ScrollState,
    enabled: Boolean,
    flingBehavior: FlingBehavior?,
    reverseScrolling: Boolean,
    coroutineScope: CoroutineScope,
): Modifier {
    var dragging: Boolean
    val density = LocalDensity.current
    return this
        .verticalScroll(
            state = state,
            enabled = enabled,
            flingBehavior = flingBehavior,
            reverseScrolling = reverseScrolling,
        )
        .draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { delta ->
                coroutineScope.launch {
                    state.scrollBy(-delta)
                }
            },
            onDragStarted = {
                dragging = true
                state.stopScroll()
            },
            onDragStopped = { initialVelocity ->
                dragging = false
                var lastValue = 0f

                val animationState = AnimationState(
                    initialValue = 0f,
                    initialVelocity = -initialVelocity,
                )
                animationState.animateDecay(splineBasedDecay(density)) {
                    launch {
                        if (dragging) {
                            this@animateDecay.cancelAnimation()
                            state.stopScroll()
                        } else {
                            val delta = value - lastValue
                            val consumed = state.scrollBy(delta)
                            lastValue = value
                            // avoid rounding errors and stop if anything is unconsumed
                            if (abs(delta - consumed) > 0.5f) this@animateDecay.cancelAnimation()
                        }
                    }
                }
            }
        )
}