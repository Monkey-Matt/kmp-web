import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kmpweb.composeapp.generated.resources.Res
import kmpweb.composeapp.generated.resources.compose_multiplatform
import kmpweb.composeapp.generated.resources.phone_hand

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val windowInfo = LocalWindowInfo.current
        val screenHeight = windowInfo.containerSize.height.pxToDp
        val screenWidth = windowInfo.containerSize.width.pxToDp

        var phoneState by remember { mutableStateOf(PhoneState(false)) }
        val toggleShowContent = { phoneState = phoneState.toggleShowContent() }

        // TODO animate the transition between web and phone layouts
        if (screenWidth > 600.dp && screenHeight > 400.dp && screenWidth > (screenHeight * 0.9f)) {
            Box(modifier = Modifier
                .background(Color.LightGray)
                .fillMaxSize()
            ) {
                WebContent(
                    phoneState  = phoneState,
                    onVisibilityButtonClick = toggleShowContent,
                    screenWidth = screenWidth,
                    screenHeight = screenHeight,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        } else {
            PhoneContent(
                phoneState  = phoneState,
                onVisibilityButtonClick = toggleShowContent,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun WebContent(
    phoneState: PhoneState,
    onVisibilityButtonClick: () -> Unit,
    screenWidth: Dp,
    screenHeight: Dp,
    modifier: Modifier = Modifier,
) {
    // FIXME make corner radius/padding/font size scale with screen height
    Box(modifier = modifier.wrapContentSize()) {
        PhoneContent(
            phoneState = phoneState,
            onVisibilityButtonClick = onVisibilityButtonClick,
            modifier = Modifier
                .width(screenHeight * 0.35f)
                .height(screenHeight * 0.75f)
                .offset(screenHeight * 0.35f, screenHeight * 0.07f)
                .background(Color.White, shape = RoundedCornerShape(30.dp))
                .padding(top = 30.dp, bottom = 24.dp)
                .padding(horizontal = 24.dp)
        )
        Image(
            painter = painterResource(Res.drawable.phone_hand),
            contentDescription = null,
        )
    }
}

val Int.pxToDp @Composable
get() = (this / LocalDensity.current.density).dp


@Composable
fun PhoneContent(
    phoneState: PhoneState,
    onVisibilityButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showContent = phoneState.showContent
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = onVisibilityButtonClick) {
            Text(if (showContent) "Hide Content" else "Show Content!")
        }
        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}

data class PhoneState(
    val showContent: Boolean,
) {
    fun toggleShowContent(): PhoneState {
        return this.copy(showContent = !showContent)
    }
}
