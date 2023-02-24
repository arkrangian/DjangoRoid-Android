package com.djangoroid.android.hackathon.di

import android.content.Context
import android.util.Log
import com.djangoroid.android.hackathon.network.RestService
import com.djangoroid.android.hackathon.ui.user.UserViewModel
import com.djangoroid.android.hackathon.util.AuthStorage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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

    single<Moshi> {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single { AuthStorage(get()) }

    viewModel { UserViewModel(get(), get()) }
}