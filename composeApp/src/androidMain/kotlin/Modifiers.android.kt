import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope

@Composable
actual fun Modifier.verticalScrollWithTouch(
    state: ScrollState,
    enabled: Boolean,
    flingBehavior: FlingBehavior?,
    reverseScrolling: Boolean,
    coroutineScope: CoroutineScope,
): Modifier {
    return this
        .verticalScroll(
            state = state,
            enabled = enabled,
            flingBehavior = flingBehavior,
            reverseScrolling = reverseScrolling,
        )
}