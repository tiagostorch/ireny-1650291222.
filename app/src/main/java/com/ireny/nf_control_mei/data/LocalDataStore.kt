package com.ireny.nf_control_mei.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalDataStore(context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = "nf_control_mei"
    )

    private object PreferencesKeys {
        val automatic_login = preferencesKey<Int>("automatic_login")
        val limit_mei = preferencesKey<String>("limit_mei")
    }

    suspend fun setAutomaticLogin(automatic: Boolean) {
        dataStore.edit { preference ->
            preference[PreferencesKeys.automatic_login] = if (automatic) 1 else 0
        }
    }

    suspend fun getAutomaticLogin(): Boolean {
        val aux =
            dataStore.data.map { preference -> preference[PreferencesKeys.automatic_login] ?: 0 }
                .first()
        return aux == 1
    }

    suspend fun setLimitMei(limit: Double) {
        dataStore.edit { preference ->
            preference[PreferencesKeys.limit_mei] = limit.toString()
        }
    }

    suspend fun getLimitMei(): Float {
        val aux = dataStore.data.map { preference ->
            preference[PreferencesKeys.limit_mei] ?: ""
        }.first()
        if(aux.isEmpty()){
            return LIMIT_MEI_DEFAULT
        }
        return aux.toFloat()
    }

    companion object{
        const val LIMIT_MEI_DEFAULT = 81000f
    }
}
