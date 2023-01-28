package com.andes.metamon.core.data.local.data_source

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class LocalPreferenceUserDataSourceImpl @Inject constructor(
    private val localPreferences: SharedPreferences
) : LocalPreferenceUserDataSource {

    override fun getAccessToken(): String =
        localPreferences.getString(METAMON_ACCESS_TOKEN, "") ?: ""

    override fun saveAccessToken(accessToken: String) {
        localPreferences.edit { putString(METAMON_ACCESS_TOKEN, accessToken) }
    }

    override fun getIsFirstVisited(): Boolean =
        localPreferences.getBoolean(IS_FIRST_VISITED, true)

    override fun setIsFirstVisited(isFirstVisited: Boolean) {
        localPreferences.edit { putBoolean(IS_FIRST_VISITED, isFirstVisited) }
    }

    override fun removeAccessToken() {
        localPreferences.edit { remove(METAMON_ACCESS_TOKEN) }
    }

    override fun clearUserInfo() {
        localPreferences.edit { clear() }
    }

    companion object {
        const val METAMON_ACCESS_TOKEN = "METAMON_ACCESS_TOKEN"
        const val IS_FIRST_VISITED = "IS_FIRST_VISITED"
    }
}
