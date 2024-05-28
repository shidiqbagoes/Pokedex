package android.goes.pokemon.viewmodel

import android.goes.pokemon.data.repository.GeneralRepository
import android.goes.pokemon.utils.PokemonType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val generalRepository: GeneralRepository
) : ViewModel() {

    fun requestPokemonList(
        searchQuery: String = "",
        pokemonTypes: List<PokemonType> = emptyList()
    ) = generalRepository
        .getPokemonList(Pair(searchQuery, pokemonTypes))
        .cachedIn(viewModelScope)
}