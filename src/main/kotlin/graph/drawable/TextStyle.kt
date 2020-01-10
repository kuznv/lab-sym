package graph.drawable

import model.utils.AnsiCode
import model.utils.AnsiCode.*
import model.utils.orEmpty

class TextStyle(
        val color: Color? = null,
        val background: BackgroundColor? = null,
        val intensity: Intensity? = null,
        vararg val params: Param
) {
    operator fun invoke(string: String): String =
            color.orEmpty().toString() +
                    background.orEmpty() +
                    intensity.orEmpty() +
                    params.joinToString(separator = "")+
                    string +
                    SANE

    companion object {
        val DEFAULT = TextStyle()
    }
}