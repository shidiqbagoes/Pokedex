package android.goes.pokemon.data.di

import android.goes.pokemon.data.remote.api.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

	@Provides
	fun provideApiInterface(@Named(NetworkModule.API_ACCESS_KEY) retrofit: Retrofit): ApiInterface =
		retrofit.create(ApiInterface::class.java)
}