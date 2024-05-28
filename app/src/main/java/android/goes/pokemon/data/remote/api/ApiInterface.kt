package android.goes.pokemon.data.remote.api

import android.goes.pokemon.data.remote.model.PokemonDetails
import android.goes.pokemon.data.remote.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET(ApiConst.POKEMON_PREFIX)
    suspend fun requestPokemonList(
        @Query(ApiConst.OFFSET) offset: Int,
        @Query(ApiConst.LIMIT) limit: Int,
    ): PokemonResponse

    @GET(ApiConst.POKEMON_ID_PREFIX)
    suspend fun requestPokemonDetails(
        @Path(ApiConst.ID) id: Int,
    ): PokemonDetails
}