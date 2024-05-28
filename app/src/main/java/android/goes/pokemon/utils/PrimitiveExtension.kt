package android.goes.pokemon.utils

import java.util.Locale

fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
    else it.toString()
}
