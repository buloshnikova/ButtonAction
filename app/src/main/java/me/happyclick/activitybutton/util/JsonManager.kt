package me.happyclick.activitybutton.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.happyclick.activitybutton.model.ActionModel
import java.io.IOException

class JsonManager {

    companion object {

        fun getJsonDataFromAsset(context: Context, fileName: String): String? {
            val jsonString: String
            try {
                jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }

        fun getAllButtonActions(applicationContext: Context): List<ActionModel> {
            val jsonFileString = getJsonDataFromAsset(applicationContext, "actions.json")
            val gson = Gson()
            val listActionType = object : TypeToken<List<ActionModel>>() {}.type
            val allActions: List<ActionModel> = gson.fromJson(jsonFileString, listActionType)
            return allActions
        }
    }
}

