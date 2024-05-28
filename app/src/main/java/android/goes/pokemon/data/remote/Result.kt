package android.goes.pokemon.data.remote

sealed class Result<T> {
	class Nothing<T> : Result<T>() {
		override fun toString() = "Result.Nothing"
	}

	class Loading<T> : Result<T>() {
		override fun toString() = "Result.Loading"
	}

	class Success<T>(val data: T?) : Result<T>() {
		override fun toString() = "Result.Success with item : [$data]"
	}

	class Error<T>(
		val message: String,
		val data: Any? = null,
		val status_code: Int = 0
	) : Result<T>() {
		override fun toString() = "Result.Error with Item Of [Message: $data, Code: ${status_code}]"
	}

	inline fun <R : Any> mapResult(transform: (T?) -> R): Result<R> {
		return when (this) {
			is Nothing -> nothing()
			is Loading -> loading()
			is Success -> success(transform(data))
			is Error -> error(message, data, status_code)
		}
	}

	companion object {
		fun <T> nothing() = Nothing<T>()
		fun <T> loading() = Loading<T>()
		fun <T> success(data: T?) = Success(data)
		fun <T> error(
			message: String,
			data: Any? = null,
			code: Int = 0
		) = Error<T>(message, data, code)
	}
}
