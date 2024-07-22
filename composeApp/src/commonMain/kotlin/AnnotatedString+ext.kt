import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration

fun AnnotatedString.Builder.addLinks(text: List<String>) {
    text.forEach {
        addLink(it)
    }
}

/**
 * Adds a blue link style and URL annotation to the supplied text if it exists within the annotated string.
 * Note: the displayed text and the annotated url will be the same
 */
fun AnnotatedString.Builder.addLink(text: String) {
    addStringAnnotation(text, tag = "URL", annotation = text)
    addStyle(
        text = text,
        style = SpanStyle(
            color = Color(0xff64B5F6),
            textDecoration = TextDecoration.Underline,
        )
    )
}

fun AnnotatedString.Builder.addStringAnnotation(text: String, tag: String, annotation: String) {
    val fullString = this.toAnnotatedString().text
    val startIndex = fullString.indexOf(text)
    val endIndex = startIndex + text.length
    addStringAnnotation(tag = tag, annotation = annotation, start = startIndex, end = endIndex)
}

fun AnnotatedString.Builder.addStyle(text: String, style: SpanStyle) {
    val fullString = this.toAnnotatedString().text
    val startIndex = fullString.indexOf(text)
    val endIndex = startIndex + text.length
    addStyle(style = style, start = startIndex, end = endIndex)
}
