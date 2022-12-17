package com.happy.recipe.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object Converters {
    @TypeConverter
    fun fromString(value: String?): ArrayList<Ingredient> {
        val listType: Type = object : TypeToken<ArrayList<Ingredient?>?>(){}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Ingredient?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}