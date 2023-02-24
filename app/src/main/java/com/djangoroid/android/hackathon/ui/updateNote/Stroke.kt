package com.djangoroid.android.hackathon.ui.updateNote

import android.graphics.Path

data class Stroke(
    val color: Int,
    val strokeWidth: Int,
    val path: Path? = null
)