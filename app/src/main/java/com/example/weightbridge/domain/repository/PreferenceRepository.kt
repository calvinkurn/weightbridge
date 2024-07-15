package com.example.weightbridge.domain.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.weightbridge.ui.utils.Constant

interface PreferenceRepository {
    fun savePreferences(context: Context?, data: String)
    fun getPreferences(context: Context?): String
}

class PreferenceRepositoryImpl: PreferenceRepository {
    private var sharedPreferences: SharedPreferences? = null

    override fun savePreferences(context: Context?, data: String) {
        if (sharedPreferences == null) {
            sharedPreferences = context?.getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        sharedPreferences?.let {
            val editor = it.edit()
            editor.putString(Constant.KEY, data)
            editor.apply()
        }
    }

    override fun getPreferences(context: Context?): String {
        if (sharedPreferences == null && context != null) {
            sharedPreferences = context.getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        return sharedPreferences?.let {
            it.getString(Constant.KEY, "") ?: " "
        } ?: ""
    }
}