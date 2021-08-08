package me.happyclick.activitybutton.ui

import me.happyclick.activitybutton.model.ActionModel

sealed class ButtonActionChange {
    object Loading : ButtonActionChange()
    data class ActionList(val list: List<ActionModel>) : ButtonActionChange()
}