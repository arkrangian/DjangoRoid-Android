package com.djangoroid.android.hackathon.util

import retrofit2.HttpException

sealed class ApiResult<out T> {

    data class Success<out T>(val data: T) : ApiResult<T>()

    data class Error(val code: Int, val message: String?) : ApiResult<Nothing>()

    data class Exception(val e: Throwable) : ApiResult<Nothing>()
}

suspend fun <T : Any>handleApi(
    api: suspend () -> T,
): ApiResult<T>{
    try {
        return ApiResult.Success(api())
    } catch (e: HttpException) {
        return ApiResult.Error(code = e.code(), message = e.message())
    } catch (e: Exception) {
        return ApiResult.Exception(e = e)
    }
}