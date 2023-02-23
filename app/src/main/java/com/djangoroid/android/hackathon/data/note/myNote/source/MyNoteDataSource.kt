package com.djangoroid.android.hackathon.data.note.myNote.source

import com.djangoroid.android.hackathon.util.ApiResult
import com.djangoroid.android.hackathon.util.handelApi

// TODO: Move it to Network package DTO
data class resultData(
    val datas: List<Any>
)

class MyNoteDataSource(
    private val djangoroidApiService: Any //Todo
) {
    suspend fun refreshMyNote(): ApiResult<resultData> {
        //return //handleApi { //djangoroidApiService.basicLogin }
    }
}