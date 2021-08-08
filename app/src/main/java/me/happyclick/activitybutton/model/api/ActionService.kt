package me.happyclick.activitybutton.model.api

import io.reactivex.Single
import me.happyclick.activitybutton.model.ActionModel
import me.happyclick.activitybutton.model.DummyModel
import retrofit2.Response
import retrofit2.http.GET

interface ActionService {

    @GET("api/activity")
    fun getActions(): Single<Response<DummyModel>>
}