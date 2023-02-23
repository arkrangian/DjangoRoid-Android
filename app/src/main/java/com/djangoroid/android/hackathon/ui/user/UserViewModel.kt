package com.djangoroid.android.hackathon.ui.user

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(

): ViewModel() {
    private val _userTags = MutableStateFlow<List<String>?>(null)
    val userTags: StateFlow<List<String>?> =_userTags
}