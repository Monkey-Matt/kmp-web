import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kmpweb.composeapp.generated.resources.Res
import kmpweb.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource


val maxWidth = 800.dp
private val headerHeight = 475.dp

data class PhoneState(
    val showContent: Boolean,
) {
    fun toggleShowContent(): PhoneState {
        return this.copy(showContent = !showContent)
    }
}

@Composable
fun PhoneContent(
    phoneState: PhoneState,
    onVisibilityButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CollapsingToolbar(modifier)
}

@Composable
fun CollapsingToolbar(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        val scroll: ScrollState = rememberScrollState(0)
        Header()
        Body(scroll)
//        Toolbar()
//        Title()
    }
}

@Composable
private fun Header() {
    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }

    Box(modifier = Modifier.fillMaxWidth().height(headerHeight).padding(top = LocalSafePadding.current.top)) {
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xAA000000)),
                        startY = 3 * headerHeightPx / 4 // to wrap the title only
                    )
                )
        )
    }
}

@Composable
private fun Body(scroll: ScrollState) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .verticalScrollWithTouch(
                scroll,
                coroutineScope = coroutineScope,
            )
//            .verticalScroll(scroll)
//            .draggable(
//                orientation = Orientation.Vertical,
//                state = rememberDraggableState { delta ->
//                    coroutineScope.launch {
//                        scroll.scrollBy(-delta)
//                    }
//                }
//            )
        ,
    ) {
        Spacer(Modifier.height(headerHeight))

        Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.background)) {
            Column(
                Modifier
                    .widthIn(max = maxWidth).align(Alignment.Center)
                    .padding(horizontal = LocalSafePadding.current.horizontal)
                    .padding(bottom = LocalSafePadding.current.bottom)
            ) {
                repeat(5) {
                    Text(
                        text = "stringResource(R.string.lorem_ipsum) and other words that make this get a little bit long. It needs to be basically a entire paragraph so that it looks legit. The red and brown fox jumps over the quirky looking little zebra.",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}
