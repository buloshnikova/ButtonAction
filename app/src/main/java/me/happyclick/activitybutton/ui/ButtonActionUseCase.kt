package me.happyclick.activitybutton.ui

import android.content.Context
import io.reactivex.Single
import me.happyclick.activitybutton.model.ActionModel
import me.happyclick.activitybutton.model.ButtonActionRepository

interface ButtonActionUseCase {

    fun getActions(context: Context): Single<List<ActionModel>>
}

class ButtonActionUseCaseImpl(private val repository: ButtonActionRepository) :
    ButtonActionUseCase {
    override fun getActions(context: Context): Single<List<ActionModel>> =
        repository.getAllActions(context)
}