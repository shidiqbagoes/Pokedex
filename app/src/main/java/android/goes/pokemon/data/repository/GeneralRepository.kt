package android.goes.pokemon.data.repository

import android.goes.pokemon.data.source.GeneralRemoteDataSource
import android.goes.pokemon.data.source.callback.GeneralSourceCallback
import android.goes.pokemon.utils.PokemonType

class GeneralRepository(
	private val generalRemoteDataSource: GeneralRemoteDataSource
) : GeneralSourceCallback {

	override fun getPokemonList(filter: Pair<String, List<PokemonType>>) = generalRemoteDataSource.requestPokemonList(filter)
}
