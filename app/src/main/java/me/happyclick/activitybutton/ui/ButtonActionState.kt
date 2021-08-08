package me.happyclick.activitybutton.ui

import com.ww.roxie.BaseState
import me.happyclick.activitybutton.model.ActionModel

data class ButtonActionState(

    val activity: Boolean = false,
    val action: List<ActionModel>? = null
) : BaseState