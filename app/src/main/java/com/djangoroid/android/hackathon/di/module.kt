package com.djangoroid.android.hackathon.di

import android.content.Context
import android.util.Log
import com.djangoroid.android.hackathon.network.RestService
import com.djangoroid.android.hackathon.ui.user.UserViewModel
import com.djangoroid.android.hackathon.util.AuthStorage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {

    single<Retrofit> {
        val context: Context = get()
        val sharedPreference =
            context.getSharedPreferences(AuthStorage.SharedPreferenceName, Context.MODE_PRIVATE)
        Retrofit.Builder()
            .baseUrl("http://3.38.100.94/")
            .addConverterFactory(MoshiConverterFactory.create(get()).asLenient())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor {
                        // 사용자 최초 로그인 또는 로그아웃 후 재로그인 시 헤더에 토큰을 안넘겨줌
                        if((sharedPreference.getString(AuthStorage.AccessTokenKey, "") ?: "").isEmpty()) {
                            val newRequest = it.request().newBuilder()
                                .addHeader(
                                    "Authorization",
                                    "" +  sharedPreference.getString(
                                        AuthStorage.AccessTokenKey,
                                        ""
                                    )
                                )
                                .build()
                            it.proceed(newRequest)
                        } else {
                            // 사용자가 토큰을 가지고 있을 때, 자동으로 헤더에 토큰을 넘겨줌
                            val newRequest = it.request().newBuilder()
                                .addHeader(
                                    "Authorization",
                                    "Bearer " + sharedPreference.getString(
                                        AuthStorage.AccessTokenKey,
                                        ""
                                    )
                                )
                                .build()
                            var response = it.proceed(newRequest)
                            when (response.code) {
                                401 -> {
                                    // 토큰 사용 중 401 에러 즉 사용자가 Unauthorized 되었을 때, 자동으로 토큰을 재발급받음
                                    Log.d("Token", "token is invalid, please refresh token")
                                    val refreshTokenJSON = JSONObject()
                                    refreshTokenJSON.put("refresh", sharedPreference.getString(AuthStorage.RefreshTokenKey, "")!!)
                                    // refresh token
                                    Log.d("Token", "$refreshTokenJSON")
                                    val refreshRequest = Request.Builder()
                                        .url("http://3.38.100.94/accounts/token/refresh/")
                                        .post(
                                            refreshTokenJSON.toString()
                                                .toRequestBody("application/json; charset=utf-8".toMediaType())
                                        )
                                        .build()
                                    val refreshResponse = OkHttpClient().newCall(refreshRequest).execute()
                                    Log.d("Token", "receive refreshResponse")
                                    Log.d("Token", "${refreshResponse.body}")
                                    val tokenJson = JSONObject("${refreshResponse.body?.string()}")
                                    val refreshedToken = tokenJson.getString("access")

                                    // sharedPreference 에 재발급한 토큰 저장
                                    sharedPreference.edit().putString(AuthStorage.AccessTokenKey, refreshedToken)
                                    sharedPreference.edit().commit()

                                    Log.d("Token", "token is refreshed")

                                    // chain 의 Request 객체를 복사해 재발급한 토큰을 헤더에 넣고 요청을 보낸다.
                                    val refreshedTokenRequest = it.request().newBuilder()
                                        .addHeader("Authorization",
                                            "Bearer " + sharedPreference.getString(
                                                AuthStorage.AccessTokenKey,
                                                "")
                                        )
                                        .build()
                                    response.close() // close response because cannot make a new request because the previous response is still open
                                    response = it.proceed(refreshedTokenRequest)
                                }
                            }
                            response
                        }
                    }
                    .build()
            )
            .build()
    }

    single<RestService> {
        get<Retrofit>().create(RestService::class.java)
    }

    viewModel { UserViewModel() }
}