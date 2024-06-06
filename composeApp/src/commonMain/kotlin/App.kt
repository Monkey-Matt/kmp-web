import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
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
import kmpweb.composeapp.generated.resources.smartphone_filled
import kmpweb.composeapp.generated.resources.smartphone_outline
import kotlinx.coroutines.delay

const val phonePictureHeight = 1634

@Composable
@Preview
fun App() {
    MaterialTheme {
        val screenSizeInfo = getScreenSizeInfo()
        val screenHeight = screenSizeInfo.hPX.coerceAtMost(phonePictureHeight).pxToDp
        val screenWidth = screenSizeInfo.wDP

        var phoneState by remember { mutableStateOf(PhoneState(true)) }
        val toggleShowContent = { phoneState = phoneState.toggleShowContent() }

        var initialInvisibility by remember { mutableStateOf(true) }
        var phoneDisplayed by remember { mutableStateOf(false) }
        val offsetAnimationSpec = spring(stiffness = Spring.StiffnessLow, visibilityThreshold = Dp.VisibilityThreshold)
        val offsetX: Dp by animateDpAsState(if (phoneDisplayed) 0.dp else 100.dp, offsetAnimationSpec)
        val offsetY: Dp by animateDpAsState(if (phoneDisplayed) 0.dp else screenHeight * 1.1f, offsetAnimationSpec)
        val rotationDegrees by animateFloatAsState(if (phoneDisplayed) 0f else 40f, spring(stiffness = 300f))

        var showDebugInfo by remember { mutableStateOf(false) }

        val bigEnoughForWeb = screenWidth > 600.dp && screenHeight > 400.dp && screenWidth > (screenHeight * 0.9f)
        var forcePhoneVersion by remember { mutableStateOf(false) }
        val shouldShowWebVersion = derivedStateOf { bigEnoughForWeb && !forcePhoneVersion }
        var showWebVersion by remember { mutableStateOf(false) }
        var showPhoneVersion by remember { mutableStateOf(false) }

        LaunchedEffect(shouldShowWebVersion.value) {
            if (shouldShowWebVersion.value) {
                showPhoneVersion = false
                showWebVersion = true
                if (initialInvisibility) {
                    phoneDisplayed = false
                    delay(400)
                }
                initialInvisibility = false
                phoneDisplayed = true
            } else {
                if (!phoneDisplayed) {
                    phoneDisplayed = true
                    delay(500)
                }
                showPhoneVersion = true
                showWebVersion = false
            }
        }

        AnimatedVisibility(showPhoneVersion, enter = fadeIn() + scaleIn(initialScale = 0.5f), exit = fadeOut() + scaleOut(targetScale = 0.5f)) {
            PhoneContent(
                phoneState  = phoneState,
                onVisibilityButtonClick = toggleShowContent,
                modifier = Modifier.fillMaxSize()
            )
        }
        AnimatedVisibility(showWebVersion, enter = fadeIn() + scaleIn(initialScale = 2f), exit = fadeOut() + scaleOut(targetScale = 2f)) {
            Box(
                modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxSize()
            ) {
                WebContent(
                    phoneState = phoneState,
                    onVisibilityButtonClick = toggleShowContent,
                    screenHeight = screenHeight,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(x = offsetX, y = offsetY)
                        .rotate(rotationDegrees)
                        .alpha(if (initialInvisibility) 0f else 1f),
                )
            }
        }
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Row {
                AnimatedVisibility(bigEnoughForWeb) {
                    Row {
                        WebToggleButton(
                            filled = forcePhoneVersion,
                            filledIcon = painterResource(Res.drawable.smartphone_filled),
                            outlineIcon = painterResource(Res.drawable.smartphone_outline),
                            onClick = { forcePhoneVersion = !forcePhoneVersion },
                        )
                        AnimatedVisibility(!forcePhoneVersion) {
                            WebToggleButton(
                                filled = phoneDisplayed,
                                filledIcon = painterResource(Res.drawable.animation_filled),
                                outlineIcon = painterResource(Res.drawable.animation_outline),
                                onClick = { phoneDisplayed = !phoneDisplayed },
                            )
                        }
                    }
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
                    Text("initialV: $initialInvisibility, phone: $phoneDisplayed, forcePhone: $forcePhoneVersion")
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

    Box(modifier = modifier.wrapContentSize().offset(x = (-100).dp * (screenHeight.toPx / phonePictureHeight))) {
        MaterialTheme(typography = scaledTypography) {
            val safePadding = PaddingValues(
                top = 15.dp * scaleFactor,
                bottom = 12.dp * scaleFactor,
                start = 12.dp * scaleFactor,
                end = 12.dp * scaleFactor,
            )
            CompositionLocalProvider(LocalSafePadding provides safePadding) {
                PhoneContent(
                    phoneState = phoneState,
                    onVisibilityButtonClick = onVisibilityButtonClick,
                    modifier = Modifier
                        .width(screenHeight * 0.35f)
                        .height(screenHeight * 0.75f)
                        .offset(screenHeight * 0.35f, screenHeight * 0.07f)
                        .clip(RoundedCornerShape(15.dp * scaleFactor))
                        .background(Color.White)
                )
            }
        }
        Image(
            painter = painterResource(Res.drawable.phone_hand),
            contentDescription = null,
        )
    }
}

val Int.pxToDp @Composable
get() = (this / LocalDensity.current.density).dp

val Dp.toPx @Composable
get() = (value * LocalDensity.current.density)

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

val LocalSafePadding = compositionLocalOf { PaddingValues(horizontal = 16.dp, vertical = 0.dp) }
val PaddingValues.top: Dp get() = calculateTopPadding()
val PaddingValues.bottom: Dp get() = calculateBottomPadding()
val PaddingValues.horizontal: Dp @Composable get() = calculateStartPadding(LocalLayoutDirection.current)
