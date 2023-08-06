package com.fang.taipeitour.data.local

import UserPreferences as ProtoUserPreferences
import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.fang.taipeitour.model.ColorScheme
import com.fang.taipeitour.model.DarkMode
import java.io.InputStream
import java.io.OutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

/**
 * user app preferences repository DataStore
 */
class UserPreferencesDataStore(context: Context) {

    internal class DataSerializer : Serializer<ProtoUserPreferences> {

        override val defaultValue: ProtoUserPreferences =
            ProtoUserPreferences.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): ProtoUserPreferences {
            try {
                return ProtoUserPreferences.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override suspend fun writeTo(t: ProtoUserPreferences, output: OutputStream) {
            t.writeTo(output)
        }
    }

    private companion object {
        val Context.dataStore: DataStore<ProtoUserPreferences> by dataStore(
            "user_preferences.pb", DataSerializer()
        )
    }

    private val dataStore = context.dataStore

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke() = dataStore.data
        .mapLatest {
            DataStoreUserPreferences(
                language = it.language,
                darkMode = it.darkMode,
                colorScheme = it.colorScheme
            )
        }
        .flowOn(Dispatchers.Default)

    suspend fun setLanguage(key: String) {
        withContext(Dispatchers.Default) {
            dataStore.updateData {
                it.toBuilder().setLanguage(key).build()
            }
        }
    }

    suspend fun toggleDarkMode() {
        withContext(Dispatchers.Default) {
            dataStore.updateData { old ->
                if (old.darkMode.isEmpty()) {
                    DarkMode.DISABLED
                } else {
                    when (DarkMode[old.darkMode]) {
                        DarkMode.ENABLED -> DarkMode.DISABLED
                        DarkMode.DISABLED -> DarkMode.ENABLED
                        null -> null
                    }
                }?.let {
                    old.toBuilder().setDarkMode(it.key).build()
                } ?: old
            }
        }
    }

    suspend fun toggleColorScheme() {
        withContext(Dispatchers.Default) {
            dataStore.updateData { old ->
                val colorScheme = if (old.colorScheme.isEmpty()) {
                    ColorScheme.default
                } else ColorScheme[old.colorScheme]
                colorScheme?.next?.let {
                    old.toBuilder().setColorScheme(it.key).build()
                } ?: old
            }
        }
    }

    suspend fun reset() {
        dataStore.updateData {
            ProtoUserPreferences.newBuilder().build()
        }
    }
}
