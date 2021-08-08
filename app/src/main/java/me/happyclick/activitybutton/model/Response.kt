package me.happyclick.activitybutton.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response<T>(val allActions: T)