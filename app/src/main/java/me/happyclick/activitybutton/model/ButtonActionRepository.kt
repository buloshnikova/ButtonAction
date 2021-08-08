package me.happyclick.activitybutton.model

import android.content.Context
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers.single
import me.happyclick.activitybutton.model.api.ActionService
import me.happyclick.activitybutton.util.JsonManager

class ButtonActionRepository(private val actionService: ActionService) {

    fun getAllActions(context: Context): Single<List<ActionModel>> {
        return actionService.getActions()
            .map { response ->
                // I wouldn't do it in a real life obviously - to pass a context and get the json from the file
                JsonManager.getAllButtonActions(context)
            }
            .onErrorResumeNext{ t: Throwable ->
                Observable.just(JsonManager.getAllButtonActions(context)).first(JsonManager.getAllButtonActions(context))
            }.map { list -> list }
    }
}