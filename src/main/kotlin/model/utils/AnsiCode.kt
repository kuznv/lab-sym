package model.utils

import model.utils.AnsiCode.BackgroundColor.*
import model.utils.AnsiCode.Color.*

sealed class AnsiCode(private val code: String) : CharSequence by code {
    object SANE : AnsiCode("\u001B[0m")

    sealed class Intensity(code: String) : AnsiCode(code) {
        object HIGH_INTENSITY : Intensity("\u001B[1m")
        object LOW_INTENSITY : Intensity("\u001B[2m")
    }

    sealed class Param(code: String) : AnsiCode(code) {
        object ITALIC : Param("\u001B[3m")
        object UNDERLINE : Param("\u001B[4m")
        object BLINK : Param("\u001B[5m")
        object RAPID_BLINK : Param("\u001B[6m")
        object REVERSE_VIDEO : Param("\u001B[7m")
        object INVISIBLE_TEXT : Param("\u001B[8m")
    }

    sealed class Color(code: String, val background: BackgroundColor) : AnsiCode(code) {
        object BLACK : Color("\u001B[30m", BACKGROUND_BLACK)
        object RED : Color("\u001B[31m", BACKGROUND_RED)
        object GREEN : Color("\u001B[32m", BACKGROUND_GREEN)
        object YELLOW : Color("\u001B[33m", BACKGROUND_YELLOW)
        object BLUE : Color("\u001B[34m", BACKGROUND_BLUE)
        object MAGENTA : Color("\u001B[35m", BACKGROUND_MAGENTA)
        object CYAN : Color("\u001B[36m", BACKGROUND_CYAN)
        object WHITE : Color("\u001B[37m", BACKGROUND_WHITE)
    }

    sealed class BackgroundColor(code: String, val foreground: Color) : AnsiCode(code) {
        object BACKGROUND_BLACK : BackgroundColor("\u001B[40m", BLACK)
        object BACKGROUND_RED : BackgroundColor("\u001B[41m", RED)
        object BACKGROUND_GREEN : BackgroundColor("\u001B[42m", GREEN)
        object BACKGROUND_YELLOW : BackgroundColor("\u001B[43m", YELLOW)
        object BACKGROUND_BLUE : BackgroundColor("\u001B[44m", BLUE)
        object BACKGROUND_MAGENTA : BackgroundColor("\u001B[45m", MAGENTA)
        object BACKGROUND_CYAN : BackgroundColor("\u001B[46m", CYAN)
        object BACKGROUND_WHITE : BackgroundColor("\u001B[47m", WHITE)
    }

    override fun toString() = code

    operator fun invoke(chars: CharSequence) = "$this$chars$SANE"

    operator fun plus(chars: CharSequence) = "$this$chars"
}