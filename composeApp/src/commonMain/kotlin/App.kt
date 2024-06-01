import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kmpweb.composeapp.generated.resources.Res
import kmpweb.composeapp.generated.resources.animation_filled
import kmpweb.composeapp.generated.resources.animation_outline
import kmpweb.composeapp.generated.resources.compose_multiplatform
import kmpweb.composeapp.generated.resources.debug_filled
import kmpweb.composeapp.generated.resources.debug_outline
import kmpweb.composeapp.generated.resources.phone_hand
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val phonePictureHeight = 1634
        val windowInfo = LocalWindowInfo.current
        val screenHeight = (windowInfo.containerSize.height).coerceAtMost(phonePictureHeight).pxToDp
        val screenWidth = windowInfo.containerSize.width.pxToDp

        var phoneState by remember { mutableStateOf(PhoneState(true)) }
        val toggleShowContent = { phoneState = phoneState.toggleShowContent() }

        var initialInvisibility by remember { mutableStateOf(true) }
        var phoneDisplayed by remember { mutableStateOf(false) }
        val offsetAnimationSpec = spring(stiffness = Spring.StiffnessLow, visibilityThreshold = Dp.VisibilityThreshold)
        val offsetX: Dp by animateDpAsState(if (phoneDisplayed) 0.dp else 100.dp, offsetAnimationSpec)
        val offsetY: Dp by animateDpAsState(if (phoneDisplayed) 0.dp else screenHeight * 1.1f, offsetAnimationSpec)
        val rotationDegrees by animateFloatAsState(if (phoneDisplayed) 0f else 40f, spring(stiffness = 300f))
        LaunchedEffect(Unit) {
            delay(1_000)
            phoneDisplayed = true
            initialInvisibility = false
        }

        var showDebugInfo by remember { mutableStateOf(false) }

        val showWebVersion = screenWidth > 600.dp && screenHeight > 400.dp && screenWidth > (screenHeight * 0.9f)

        // TODO animate the transition between web and phone layouts
        if (showWebVersion) {
            Box(modifier = Modifier
                .background(Color.LightGray)
                .fillMaxSize()
            ) {
                WebContent(
                    phoneState  = phoneState,
                    onVisibilityButtonClick = toggleShowContent,
                    screenHeight = screenHeight,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(x = offsetX, y = offsetY)
                        .rotate(rotationDegrees)
                        .alpha(if (initialInvisibility) 0f else 1f)
                    ,
                )
            }
        } else {
            PhoneContent(
                phoneState  = phoneState,
                onVisibilityButtonClick = toggleShowContent,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Row {
                AnimatedVisibility(showWebVersion) {
                    WebToggleButton(
                        filled = phoneDisplayed,
                        filledIcon = painterResource(Res.drawable.animation_filled),
                        outlineIcon = painterResource(Res.drawable.animation_outline),
                        onClick = { phoneDisplayed = !phoneDisplayed },
                    )
                }
                WebToggleButton(
                    filled = showDebugInfo,
                    filledIcon = painterResource(Res.drawable.debug_filled),
                    outlineIcon = painterResource(Res.drawable.debug_outline),
                    onClick = { showDebugInfo = !showDebugInfo },
                )
            }
            AnimatedVisibility(showDebugInfo) {
                Column {
                    Text("width: $screenWidth, height $screenHeight")
                    Text("initialV: $initialInvisibility, phone: $phoneDisplayed")
                }
            }
        }
    }
}

@Composable
fun WebContent(
    phoneState: PhoneState,
    onVisibilityButtonClick: () -> Unit,
    screenHeight: Dp,
    modifier: Modifier = Modifier,
) {
    // 400 = 1, 600 = 1.5, 800 = 2
    val scaleFactor = (screenHeight.value/400)
    // 400 = 0.5, 600 = 0.75, 800 = 1
    val fontScaleFactor = (screenHeight.value/800)
    val scaledTypography = MaterialTheme.typography.scale(fontScaleFactor)

    Box(modifier = modifier.wrapContentSize()) {
        MaterialTheme(typography = scaledTypography) {
            PhoneContent(
                phoneState = phoneState,
                onVisibilityButtonClick = onVisibilityButtonClick,
                modifier = Modifier
                    .width(screenHeight * 0.35f)
                    .height(screenHeight * 0.75f)
                    .offset(screenHeight * 0.35f, screenHeight * 0.07f)
                    .background(Color.White, shape = RoundedCornerShape(15.dp * scaleFactor))
                    .padding(top = 15.dp * scaleFactor, bottom = 12.dp * scaleFactor)
                    .padding(horizontal = 12.dp * scaleFactor)
            )
        }
        Image(
            painter = painterResource(Res.drawable.phone_hand),
            contentDescription = null,
        )
    }
}

val Int.pxToDp @Composable
get() = (this / LocalDensity.current.density).dp

@Composable
fun WebToggleButton(
    filled: Boolean,
    filledIcon: Painter,
    outlineIcon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(48.dp)
    ) {
        AnimatedVisibility(
            filled,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Icon(
                filledIcon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        AnimatedVisibility(
            !filled,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Icon(
                outlineIcon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Composable
fun PhoneContent(
    phoneState: PhoneState,
    onVisibilityButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showContent = phoneState.showContent
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = onVisibilityButtonClick) {
            // TODO strings resource?
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
