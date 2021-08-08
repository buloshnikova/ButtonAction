package me.happyclick.activitybutton.ui

import com.ww.roxie.BaseAction

sealed class ButtonAction : BaseAction {
    object GetActionButtonClicked : ButtonAction()

}