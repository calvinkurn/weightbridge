package com.example.weightbridge.domain.repository

import android.content.Context
import android.content.SharedPreferences

object PreferenceRepository {
    private var sharedPreferences: SharedPreferences? = null

    fun savePreferences(context: Context, data: String) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        sharedPreferences?.let {
            val editor = it.edit()
            editor.putString(KEY, data)
            editor.apply()
        }
    }

    fun getPreferences(context: Context): String {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        return sharedPreferences?.let {
            it.getString(KEY, "") ?: " "
        } ?: ""
    }

    private const val PREFERENCE_NAME = "local_weight_data"
    private const val KEY = "ticket_data"
}