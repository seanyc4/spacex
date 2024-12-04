package com.seancoyle.core.datastore.implementation.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.seancoyle.core.common.di.ApplicationScope
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.datastore.LaunchPreferences
import com.seancoyle.core.datastore.implementation.LaunchPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DataStoreProtoModule {

    @Singleton
    @Provides
    fun providesDataStoreProto(
        @ApplicationContext context: Context,
        @IODispatcher ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        preferencesSerializer: LaunchPreferencesSerializer
    ): DataStore<LaunchPreferences> =
        DataStoreFactory.create(
            serializer = preferencesSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher)
        ) {
            context.dataStoreFile(LAUNCH_PREFERENCES)
        }

    private companion object {
        const val LAUNCH_PREFERENCES = "launch_preferences"
    }
}