package android.goes.pokemon.data.di

import android.content.Context
import android.goes.pokemon.BuildConfig
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(
	includes = [
		ApiModule::class,
		ApplicationModule::class
	]
)
@InstallIn(SingletonComponent::class)
object NetworkModule {

	const val API_ACCESS_KEY = "general"

	@Provides
	@Named(API_ACCESS_KEY)
	fun provideBaseURL() = "https://pokeapi.co/api/v2/"

	@Provides
	@Singleton
	fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
		if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
	}

	@Provides
	@Singleton
	fun provideChuckerInterceptor(context: Context) = ChuckerInterceptor.Builder(context).build()

	@Provides
	@Singleton
	fun provideOkHttpClientBuilder(okHttpClient: OkHttpClient) =
		okHttpClient.newBuilder()

	@Provides
	@Singleton
	fun provideCache(context: Context): Cache {
		val cacheSize = 10 * 1024 * 1024 // 10 MB Cache
		return Cache(context.cacheDir, cacheSize.toLong())
	}

	@Provides
	@Singleton
	fun provideOkHttpCallback(
		interceptor: HttpLoggingInterceptor,
		chuckerInterceptor: ChuckerInterceptor,
		cache: Cache
	) = OkHttpClient.Builder()
		.writeTimeout(30, TimeUnit.SECONDS)
		.readTimeout(20, TimeUnit.SECONDS)
		.addInterceptor(interceptor)
		.addInterceptor(chuckerInterceptor)
		.cache(cache)
		.build()

	@Provides
	@Singleton
	@Named(API_ACCESS_KEY)
	fun provideRetrofit(
		okHttpClient: OkHttpClient,
		@Named(API_ACCESS_KEY) baseURL: String
	): Retrofit = Retrofit.Builder()
		.baseUrl(baseURL)
		.addConverterFactory(GsonConverterFactory.create())
		.client(okHttpClient)
		.build()
}