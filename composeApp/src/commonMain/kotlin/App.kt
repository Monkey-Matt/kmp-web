

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kmpweb.composeapp.generated.resources.Res
import kmpweb.composeapp.generated.resources.animation_filled
import kmpweb.composeapp.generated.resources.animation_outline
import kmpweb.composeapp.generated.resources.debug_filled
import kmpweb.composeapp.generated.resources.debug_outline
import kmpweb.composeapp.generated.resources.mac_frame
import kmpweb.composeapp.generated.resources.phone_hand
import kmpweb.composeapp.generated.resources.smartphone_filled
import kmpweb.composeapp.generated.resources.smartphone_outline
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val phonePictureHeight = 1634

@Composable
@Preview
fun App() {
    MaterialTheme {
        Box {
            val screenSizeInfo = getScreenSizeInfo()
            val screenHeight = screenSizeInfo.hPX.coerceAtMost(phonePictureHeight).pxToDp
            val screenWidth = screenSizeInfo.wDP

            var initialInvisibility by remember { mutableStateOf(true) }
            var phoneDisplayed by remember { mutableStateOf(false) }

            var showDebugInfo by remember { mutableStateOf(false) }

            val bigEnoughForWeb =
                screenWidth > 600.dp && screenHeight > 400.dp && screenWidth > (screenHeight * 0.9f)
            var forcePhoneVersion by remember { mutableStateOf(false) }
            val shouldShowWebVersion = derivedStateOf { bigEnoughForWeb && !forcePhoneVersion }
            var showWebVersion by remember { mutableStateOf(false) }
            var showPhoneVersion by remember { mutableStateOf(false) }

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

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

            AnimatedVisibility(
                showPhoneVersion,
                enter = fadeIn() + scaleIn(initialScale = 0.5f),
                exit = fadeOut() + scaleOut(targetScale = 0.5f)
            ) {
                CollapsingToolbarPhoneContent(
                    drawerState = drawerState,
                    modifier = Modifier.fillMaxSize()
                )
            }
            AnimatedVisibility(
                showWebVersion,
                enter = fadeIn() + scaleIn(initialScale = 2f),
                exit = fadeOut() + scaleOut(targetScale = 2f)
            ) {
                PhoneHandInLaptop(
                    drawerState = drawerState,
                    phoneDisplayed = phoneDisplayed,
                    initialInvisibility = initialInvisibility,
                    screenHeight = screenHeight,
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(
                        color = MaterialTheme.colors.background,
                        shape = RoundedCornerShape(topEnd = 8.dp),
                    )
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.onBackground,
                        shape = RoundedCornerShape(topEnd = 8.dp),
                    )
                    .padding(horizontal = 8.dp).padding(top = 4.dp),
            ) {
                AnimatedVisibility(
                    showDebugInfo,
                    enter = fadeIn() + expandIn(),
                    exit = fadeOut() + shrinkOut(),
                ) {
                    Column {
                        Text("width: ${screenSizeInfo.wPX}, height ${screenSizeInfo.hPX}")
                        Text("initialV: $initialInvisibility, phone: $phoneDisplayed, forcePhone: $forcePhoneVersion")
                    }
                }
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
            }
        }
    }
}

@Composable
fun PhoneHandInLaptop(
    drawerState: DrawerState,
    phoneDisplayed: Boolean,
    initialInvisibility: Boolean,
    screenHeight: Dp,
    modifier: Modifier = Modifier,
) {
    val offsetAnimationSpec = spring(stiffness = Spring.StiffnessLow, visibilityThreshold = Dp.VisibilityThreshold)
    val offsetX: Dp by animateDpAsState(if (phoneDisplayed) 0.dp else 100.dp, offsetAnimationSpec)
    val offsetY: Dp by animateDpAsState(if (phoneDisplayed) 0.dp else screenHeight * 1.1f, offsetAnimationSpec)
    val rotationDegrees by animateFloatAsState(if (phoneDisplayed) 0f else 40f, spring(stiffness = 300f))
    val showLaptop = screenHeight > 1630.pxToDp

    Box(modifier = modifier) {
        val laptopWidthPx = 4967//4480
        val laptopHeightPx = 3111//2806
        Box(
            modifier = Modifier
                .size(
                    width = (laptopWidthPx - 50).pxToDp,
                    height = (laptopHeightPx - 50).pxToDp
                )
                .background(Color.LightGray)
                .align(Alignment.Center)
        )
        PhoneInHand(
            drawerState = drawerState,
            screenHeight = screenHeight,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = offsetX, y = offsetY)
                .rotate(rotationDegrees)
                .alpha(if (initialInvisibility) 0f else 1f),
        )
        AnimatedVisibility(
            visible = showLaptop,
            enter = fadeIn() + scaleIn(initialScale = 1.2f),
            exit = fadeOut() + scaleOut(targetScale = 1.2f),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                modifier = Modifier
                    .offset(y = (-90).pxToDp)
                    .wrapContentSize(align = Alignment.Center, unbounded = true)
                    .size(width = laptopWidthPx.pxToDp, height = laptopHeightPx.pxToDp)
                ,
                painter = painterResource(Res.drawable.mac_frame),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }
    }
}

@Composable
fun PhoneInHand(
    drawerState: DrawerState,
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
                CollapsingToolbarPhoneContent(
                    drawerState = drawerState,
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
