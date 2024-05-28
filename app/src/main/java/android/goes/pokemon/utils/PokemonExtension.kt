package android.goes.pokemon.utils

import android.goes.pokemon.data.remote.model.Pokemon
import android.graphics.Color
import java.util.Locale

val Pokemon.pokemonId get() = url.split("/".toRegex()).dropLast(1).last().toInt()

val Pokemon.homeSprite get(): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/$pokemonId.png"
}

sealed class PokemonType(val color: Int) {
    data object Normal : PokemonType(Color.parseColor("#A8A77A"))
    data object Fire : PokemonType(Color.parseColor("#EE8130"))
    data object Water : PokemonType(Color.parseColor("#6390F0"))
    data object Electric : PokemonType(Color.parseColor("#F7D02C"))
    data object Grass : PokemonType(Color.parseColor("#7AC74C"))
    data object Ice : PokemonType(Color.parseColor("#96D9D6"))
    data object Fighting : PokemonType(Color.parseColor("#C22E28"))
    data object Poison : PokemonType(Color.parseColor("#A33EA1"))
    data object Ground : PokemonType(Color.parseColor("#E2BF65"))
    data object Flying : PokemonType(Color.parseColor("#A98FF3"))
    data object Psychic : PokemonType(Color.parseColor("#F95587"))
    data object Bug : PokemonType(Color.parseColor("#A6B91A"))
    data object Rock : PokemonType(Color.parseColor("#B6A136"))
    data object Ghost : PokemonType(Color.parseColor("#735797"))
    data object Dragon : PokemonType(Color.parseColor("#6F35FC"))
    data object Dark : PokemonType(Color.parseColor("#705746"))
    data object Steel : PokemonType(Color.parseColor("#B7B7CE"))
    data object Fairy : PokemonType(Color.parseColor("#D685AD"))

    companion object {
        fun fromString(type: String): PokemonType {
            return when (type.lowercase(Locale.getDefault())) {
                "normal" -> Normal
                "fire" -> Fire
                "water" -> Water
                "electric" -> Electric
                "grass" -> Grass
                "ice" -> Ice
                "fighting" -> Fighting
                "poison" -> Poison
                "ground" -> Ground
                "flying" -> Flying
                "psychic" -> Psychic
                "bug" -> Bug
                "rock" -> Rock
                "ghost" -> Ghost
                "dragon" -> Dragon
                "dark" -> Dark
                "steel" -> Steel
                "fairy" -> Fairy
                else -> Normal
            }
        }
    }
}

fun lightenColor(color: Int, factor: Float = 0.5f): Int {
    val r = Color.red(color)
    val g = Color.green(color)
    val b = Color.blue(color)
    return Color.argb(
        Color.alpha(color),
        Math.min(255, (r + 255 * factor).toInt()),
        Math.min(255, (g + 255 * factor).toInt()),
        Math.min(255, (b + 255 * factor).toInt())
    )
}
