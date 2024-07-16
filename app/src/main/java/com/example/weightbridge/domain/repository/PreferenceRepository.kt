package com.example.weightbridge.domain.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.weightbridge.ui.utils.Constant
import javax.inject.Inject

interface PreferenceRepository {
    fun savePreferences(data: String)
    fun getPreferences(): String
}

class PreferenceRepositoryImpl @Inject constructor (
    private val paramContext: Context
): PreferenceRepository {
    private var sharedPreferences: SharedPreferences? = null

    override fun savePreferences(data: String) {
        if (sharedPreferences == null) {
            sharedPreferences = paramContext.getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        sharedPreferences?.let {
            val editor = it.edit()
            editor.putString(Constant.KEY, data)
            editor.apply()
        }
    }

    override fun getPreferences(): String {
        if (sharedPreferences == null) {
            sharedPreferences = paramContext.getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        return sharedPreferences?.let {
            it.getString(Constant.KEY, "") ?: " "
        } ?: ""
    }
}