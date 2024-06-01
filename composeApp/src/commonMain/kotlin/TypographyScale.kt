import androidx.compose.material.Typography
import androidx.compose.runtime.Composable

@Composable
fun Typography.scale(scaleFactor: Float): Typography {
    return this.copy(
        h1 = this.h1.copy(
            fontSize = this.h1.fontSize * scaleFactor,
            lineHeight = this.h1.lineHeight * scaleFactor,
        ),
        h2 = this.h2.copy(
            fontSize = this.h2.fontSize * scaleFactor,
            lineHeight = this.h2.lineHeight * scaleFactor,
        ),
        h3 = this.h3.copy(
            fontSize = this.h3.fontSize * scaleFactor,
            lineHeight = this.h3.lineHeight * scaleFactor,
        ),
        h4 = this.h4.copy(
            fontSize = this.h4.fontSize * scaleFactor,
            lineHeight = this.h4.lineHeight * scaleFactor,
        ),
        h5 = this.h5.copy(
            fontSize = this.h5.fontSize * scaleFactor,
            lineHeight = this.h5.lineHeight * scaleFactor,
        ),
        h6 = this.h6.copy(
            fontSize = this.h6.fontSize * scaleFactor,
            lineHeight = this.h6.lineHeight * scaleFactor,
        ),
        body1 = this.body1.copy(
            fontSize = this.body1.fontSize * scaleFactor,
            lineHeight = this.body1.lineHeight * scaleFactor,
        ),
        body2 = this.body2.copy(
            fontSize = this.body2.fontSize * scaleFactor,
            lineHeight = this.body2.lineHeight * scaleFactor,
        ),
        subtitle1 = this.subtitle1.copy(
            fontSize = this.subtitle1.fontSize * scaleFactor,
            lineHeight = this.subtitle1.lineHeight * scaleFactor,
        ),
        subtitle2 = this.subtitle2.copy(
            fontSize = this.subtitle2.fontSize * scaleFactor,
            lineHeight = this.subtitle2.lineHeight * scaleFactor,
        ),
        button = this.button.copy(
            fontSize = this.button.fontSize * scaleFactor,
            lineHeight = this.button.lineHeight * scaleFactor,
        ),
        caption = this.caption.copy(
            fontSize = this.caption.fontSize * scaleFactor,
            lineHeight = this.caption.lineHeight * scaleFactor,
        ),
        overline = this.overline.copy(
            fontSize = this.overline.fontSize * scaleFactor,
            lineHeight = this.overline.lineHeight * scaleFactor,
        ),
    )
}