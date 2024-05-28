package android.goes.pokemon.data.source

import android.goes.pokemon.data.remote.api.ApiConst
import android.goes.pokemon.data.remote.api.ApiInterface
import android.goes.pokemon.data.source.paging.PokemonRemotePagingSource
import android.goes.pokemon.utils.PokemonType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GeneralRemoteDataSource(private val apiInterface: ApiInterface) {

    private val Pair<String, List<PokemonType>>.requestSize get() =
        if (first.isNotEmpty() || second.isNotEmpty()) ApiConst.SEARCH_LOAD_SIZE
        else ApiConst.NORMAL_LOAD_SIZE

    fun requestPokemonList(filter: Pair<String, List<PokemonType>> = Pair("", emptyList())) = Pager(
        config = PagingConfig(
            pageSize = filter.requestSize,
            enablePlaceholders = false,
            initialLoadSize = filter.requestSize
        ),
        pagingSourceFactory = { PokemonRemotePagingSource(apiInterface, filter) }
    ).flow.flowOn(Dispatchers.IO)

}
