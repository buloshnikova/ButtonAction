package me.happyclick.activitybutton.model

data class ActionModel (
    val type: String?,
    val enabled: Boolean = true,
    val priority: Short?,
    val valid_days: List<Short>,
    val cool_down: Long?
)