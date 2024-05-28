package android.goes.pokemon.data.source.paging

import android.goes.pokemon.data.remote.api.ApiConst
import android.goes.pokemon.data.remote.api.ApiInterface
import android.goes.pokemon.data.remote.model.Pokemon
import android.goes.pokemon.utils.PokemonType
import android.goes.pokemon.utils.homeSprite
import android.goes.pokemon.utils.pokemonId
import androidx.paging.PagingSource
import androidx.paging.PagingState

class PokemonRemotePagingSource(
    private val service: ApiInterface,
    private val filter: Pair<String, List<PokemonType>> = Pair("", emptyList())
) : PagingSource<Int, Pokemon>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            val offset = params.key ?: ApiConst.STARTING_OFFSET
            val loadSize = params.loadSize

            // Fetch the list of Pokémon
            val response = service.requestPokemonList(offset, loadSize)
            val pokemonList = response.results.map { pokemon ->
                val pokemonId = pokemon.pokemonId
                val details = service.requestPokemonDetails(pokemonId)
                pokemon.copy(
                    id = pokemonId,
                    displaySprite = pokemon.homeSprite,
                    types = details.types.map { it.type.name }
                )
            }

            // Filter by type if the type filter is not empty
            val filteredList = if (filter.second.isNotEmpty()) {
                pokemonList.filter { pokemon ->
                    pokemon.types.any { type ->
                        filter.second.contains(PokemonType.fromString(type))
                    }
                }
            } else {
                pokemonList
            }

            // Filter by name if the name filter is not empty
            val finalList = if (filter.first.isNotEmpty()) {
                filteredList.filter { pokemon ->
                    pokemon.name.lowercase() == filter.first.lowercase()
                }
            } else {
                filteredList
            }

            LoadResult.Page(
                data = finalList,
                prevKey = if (offset == ApiConst.STARTING_OFFSET) null else offset - loadSize,
                nextKey = if (response.next == null) null else offset + loadSize
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition
    }
}
