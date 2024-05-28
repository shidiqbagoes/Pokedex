package android.goes.pokemon.data.remote.model

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>
)

data class Pokemon(
    val name: String,
    val url: String,
    val id: Int,
    var displaySprite: String? = null,
    var types: List<String> = emptyList()
)

data class PokemonDetails(
    val id: Int,
    val types: List<Type>
)

data class Type(
    val type: TypeDetails
)

data class TypeDetails(
    val name: String
)
