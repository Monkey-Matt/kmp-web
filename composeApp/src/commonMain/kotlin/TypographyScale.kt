import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

@Composable
fun Typography.scale(scaleFactor: Float): Typography {
    return this.copy(
        h1 = this.h1.scale(scaleFactor),
        h2 = this.h2.scale(scaleFactor),
        h3 = this.h3.scale(scaleFactor),
        h4 = this.h4.scale(scaleFactor),
        h5 = this.h5.scale(scaleFactor),
        h6 = this.h6.scale(scaleFactor),
        body1 = this.body1.scale(scaleFactor),
        body2 = this.body2.scale(scaleFactor),
        subtitle1 = this.subtitle1.scale(scaleFactor),
        subtitle2 = this.subtitle2.scale(scaleFactor),
        button = this.button.scale(scaleFactor),
        caption = this.caption.scale(scaleFactor),
        overline = this.overline.scale(scaleFactor),
    )
}

fun TextStyle.scale(scaleFactor: Float): TextStyle {
    return this.copy(
        fontSize = this.fontSize * scaleFactor,
        lineHeight = this.lineHeight * scaleFactor,
    )
}