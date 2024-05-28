package android.goes.pokemon.utils

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Return [Gson] Constructor
 */
val gson get() = Gson()

/**
 * Convert any Value within pair of Field and Value to JsonObject
 */
inline fun Gson.toJsonElement(source: HashMap<String, Any>.() -> Unit): JsonElement {
	val hashMap = HashMap<String, Any>()
	hashMap.source()

	return toJsonTree(hashMap)
}

/**
 * Convert any Value within pair of Field and Value to JsonString
 */
inline fun Gson.toJsonString(source: HashMap<String, Any>.() -> Unit): String {
	val hashMap = HashMap<String, Any>()
	hashMap.source()

	return toJson(hashMap)
}

/**
 * Convert [String] as JsonString to [T] object
 */
inline fun <reified T> Gson.fromJson(source: String): T {
	val type = object : TypeToken<T>() {}.type
	return fromJson(source, type)
}


/**
 * Convert [ResponseBody.charStream] to [T] object
 */
inline fun <reified T : Any?> ResponseBody.parse(): T? {
	val classType = object : TypeToken<T>() {}.type
	return Gson().fromJson(charStream(), classType)
}

/**
 * Convert [Int] from error Body to Custom Error Message
 */
fun Int.handleCode() = when (this) {
	401 -> "Silahkan Login/Daftar untuk melanjutkan"
	403 -> "Silahkan Login/Daftar untuk melanjutkan"
	404 -> "Halaman yang dituju tidak dapat ditemukan, harap mencoba ulang secara berkala"
	422 -> "Data yang diinputkan tidak dapat diproses, harap coba input data yang berbeda atau tunggu beberapa saat"
	else -> "Mohon maaf terjadi kesalahan, tunggu beberapa saat untuk mencoba kembali"
}

// Exception Handler
private fun Throwable.handleException() = when (this) {
	is IOException -> "Failed to read response!"
	is SocketTimeoutException -> "Timeout!"
	is UnknownHostException -> "Check your internet connection!"
	else -> "An error occurred!"
}

/**
 * Convert [Exception]/[Throwable]  to Custom Error Message
 */
val Throwable.parsedMessage get() = handleException()

/**
 * @return [Flow] of [Result] object with 3 Step of Result Handling
 * @param responseBody Used for [Response] callback value from ApiCallback
 */
/*inline fun <reified T> flowResponse(
    handleError: Boolean = true,
    crossinline errorMessage: (String) -> String = { "" },
    crossinline responseBody: suspend () -> Response<BaseResponse<T>>
) = flow<Result<T>> {

    val response = responseBody.invoke()
    val body = response.body()

    if (response.isSuccessful) {
        val result = body?.data


        emit(Result.success(result))
    } else {
        val isError = response.code() in 400..599
        val errorBody = response.errorBody()?.parse<BaseResponse<T>>()

        val message =
            if (isError && handleError) response.code().handleCode()
            else if (errorMessage.invoke(errorBody?.message ?: "") != "") errorMessage.invoke(
                errorBody?.message ?: ""
            )
            else errorBody?.message ?: ""

        emit(
            Result.error(
                message = message,
                data = errorBody?.data,
                code = response.code()
            )
        )
    }}
        .onStart { emit(Result.loading()) }
        .flowOn(Dispatchers.IO)
        .retryWhen { cause, attempt ->
            attempt <= 3 && cause is SocketTimeoutException
        }
        .catch { throwable ->
			Timber.e("Error @${T::class.java} : $throwable")
			emit(Result.error<T>(throwable.parsedMessage, code = 500))
		}*/
