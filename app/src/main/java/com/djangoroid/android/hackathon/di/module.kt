package com.djangoroid.android.hackathon.di

import com.djangoroid.android.hackathon.ui.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { UserViewModel() }
}