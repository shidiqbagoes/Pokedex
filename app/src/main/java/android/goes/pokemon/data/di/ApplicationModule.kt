package android.goes.pokemon.data.di

import android.app.Application
import android.content.Context
import android.goes.pokemon.data.remote.api.ApiInterface
import android.goes.pokemon.data.repository.GeneralRepository
import android.goes.pokemon.data.source.GeneralRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

	@Provides
	fun provideApplication(application: Application): Context = application


	@Provides
	fun provideGeneralRepository(
		apiInterface: ApiInterface
	) = GeneralRepository(
		GeneralRemoteDataSource(apiInterface)
	)
}