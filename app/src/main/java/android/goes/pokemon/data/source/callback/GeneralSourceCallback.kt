package android.goes.pokemon.data.source.callback

import android.goes.pokemon.data.remote.model.Pokemon
import android.goes.pokemon.data.remote.model.PokemonResponse
import android.goes.pokemon.utils.PokemonType
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface GeneralSourceCallback {
	fun getPokemonList(filter: Pair<String, List<PokemonType>> = Pair("", emptyList())): Flow<PagingData<Pokemon>>
}
