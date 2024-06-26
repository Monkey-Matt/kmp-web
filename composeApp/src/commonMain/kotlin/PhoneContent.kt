
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import kmpweb.composeapp.generated.resources.Res
import kmpweb.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource


val maxWidth = 800.dp

/**
 * Built after following this tutorial:
 * https://www.droidcon.com/2022/10/10/collapsing-toolbar-with-parallax-effect-and-curved-motion-in-jetpack-compose-%F0%9F%98%8E/
 */
@Composable
fun CollapsingToolbarPhoneContent(
    drawerState: DrawerState,
    modifier: Modifier = Modifier
) {
    val drawerStateWorkaround = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalDrawer(
        modifier = modifier,
        drawerContent = {
            DrawerContent(modifier = Modifier.padding(top = LocalSafePadding.current.top))
        },
        drawerState = drawerStateWorkaround,
    ) {
        CollapsingToolbar(
            onMenuClick = {
                scope.launch {
                    if (drawerStateWorkaround.isOpen) drawerStateWorkaround.close() else drawerStateWorkaround.open()
                }
            }
        )
    }
}

@Composable
fun DrawerContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val safePaddingStart = LocalSafePadding.current.horizontal
        Text(
            "Drawer title",
            modifier = Modifier.padding(vertical = 16.dp).padding(start = safePaddingStart),
        )
        Divider()
    }
}

@Composable
fun CollapsingToolbar(modifier: Modifier = Modifier, onMenuClick: () -> Unit) {
    var availableHeightPx by remember { mutableStateOf(0) }
    var availableWidthPx by remember { mutableStateOf(0) }
    val bottomSafePadding = LocalSafePadding.current.bottom
    val density = LocalDensity.current
    val headerHeight by derivedStateOf {
        475.dp.coerceAtMost(with(density) { availableHeightPx.toDp() } - bottomSafePadding)
    }

    Box(modifier = modifier.fillMaxSize().onGloballyPositioned {
        availableWidthPx = it.size.width
        availableHeightPx = it.size.height
    }) {
        val scroll: ScrollState = rememberScrollState(0)
        Header(scroll, headerHeight)
        Body(scroll, headerHeight)
        Toolbar(scroll, headerHeight, onMenuClick)
        Title(scroll, headerHeight, availableWidthPx)
    }
}

@Composable
private fun Header(scroll: ScrollState, headerHeight: Dp) {
    val headerHeightPx = headerHeight.toPx - LocalSafePadding.current.top.toPx - 56.dp.toPx
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .padding(top = LocalSafePadding.current.top)
            .graphicsLayer {
                translationY = -scroll.value.toFloat() / 3f // Parallax effect
                alpha = (-1f / headerHeightPx) * scroll.value + 1
            }
        ,
    ) {
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
        )
    }
}

@Composable
private fun Body(scroll: ScrollState, headerHeight: Dp) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .verticalScrollWithTouch(
                scroll,
                coroutineScope = coroutineScope,
            )
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

@Composable
private fun Toolbar(
    scroll: ScrollState,
    headerHeight: Dp,
    onMenuClick: () -> Unit,
) {
    val toolbarHeight = 56.dp
    val toolbarHeightPx = toolbarHeight.toPx + LocalSafePadding.current.top.toPx
    val headerHeightPx = headerHeight.toPx
    val toolbarBottom = headerHeightPx - toolbarHeightPx
    val showToolbar by derivedStateOf { scroll.value >= toolbarBottom }

    AnimatedVisibility(
        visible = showToolbar,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)),
    ) {
        Column {
            TopAppBar(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(top = LocalSafePadding.current.top)
                    .height(toolbarHeight),
                navigationIcon = {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "",
                            tint = MaterialTheme.colors.onBackground,
                        )
                    }
                },
                title = {},
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )

            // Shadow
            Box(
                modifier = Modifier
                    .height(3.dp)
                    .fillMaxWidth()
                    .background(brush = Brush.verticalGradient(colors = listOf(Color.Black.copy(alpha = 0.1f), Color.Transparent)))
            )
        }
    }
}

@Composable
private fun Title(scroll: ScrollState, headerHeight: Dp, availableWidthPx: Int) {
    var titleHeightPx by remember { mutableStateOf(0f) }
    val titleHeightDp = with(LocalDensity.current) { titleHeightPx.toDp() }
    val toolbarHeight = 56.dp
    val topSafePadding = LocalSafePadding.current.top
    val horizontalSafePadding = LocalSafePadding.current.horizontal

    var titleXOffset by remember { mutableStateOf(0.dp) }
    val availableWidthDp = availableWidthPx.pxToDp
    val titleWidth by derivedStateOf {
        (availableWidthDp - titleXOffset) - (horizontalSafePadding)
    }

    val text = "Compose for ${getPlatform().name}"

    val collapseRange: Float = (headerHeight.toPx - (toolbarHeight + topSafePadding).toPx)
    val collapseFraction: Float = (scroll.value / collapseRange).coerceIn(0f, 1f)

    val fontScale = lerp(
        1.2f, // start X
        1f, // end X
        collapseFraction
    )

    Text(
        text = text,
        style = MaterialTheme.typography.h6.scale(fontScale).copy(fontWeight = FontWeight.Bold),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .widthIn(max = titleWidth)
            .graphicsLayer {
                val titleY = lerp(
                    headerHeight - titleHeightDp - 16.dp, // start Y
                    toolbarHeight / 2 + topSafePadding - titleHeightDp / 2, // end Y
                    collapseFraction
                )

                val titleX = lerp(
                    horizontalSafePadding, // start X
                    16.dp + 36.dp + 16.dp, // end X
                    collapseFraction
                )

                translationY = titleY.toPx()
                translationX = titleX.toPx()
                titleXOffset = titleX
            }
            .onGloballyPositioned {
                // We don't know title height in advance to calculate the lerp
                // so we wait for initial composition
                titleHeightPx = it.size.height.toFloat()
            }
    )
}
