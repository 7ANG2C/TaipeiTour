package com.fang.taipeitour.datastore.user

import UserPreferences
import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest

class UserPreferencesDataStore(context: Context) {

    internal class DataSerializer : Serializer<UserPreferences> {

        override val defaultValue: UserPreferences =
            UserPreferences.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): UserPreferences {
            try {
                return UserPreferences.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
            t.writeTo(output)
        }
    }

    private companion object {
        val Context.dataStore: DataStore<UserPreferences> by dataStore(
            "user_preferences.pb", DataSerializer()
        )
    }

    private val dataStore = context.dataStore

    operator fun invoke(): Flow<com.fang.taipeitour.datastore.user.UserPreferences> {
        return dataStore.data
            .mapLatest { dataWrapper ->
                UserPreferences(
                    id = dataWrapper.darkMode,
                    darkMode = dataWrapper.darkMode,
                )
            }
            .distinctUntilChanged()
    }

    suspend fun update(id: Int) {
        dataStore.updateData { dataWrapper ->
            dataWrapper.toBuilder()
                .setDarkMode("dark")
                .build()
        }
    }

}
