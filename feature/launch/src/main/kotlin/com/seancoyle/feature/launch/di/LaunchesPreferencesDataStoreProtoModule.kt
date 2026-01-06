package com.seancoyle.feature.launch.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.seancoyle.core.common.di.ApplicationScope
import com.seancoyle.core.datastore_proto.LaunchPreferencesProto
import com.seancoyle.feature.launch.data.local.LaunchesPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class LaunchesPreferencesDataStoreProtoModule {

    @Singleton
    @Provides
    fun providesDataStoreProto(
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope,
        preferencesSerializer: LaunchesPreferencesSerializer
    ): DataStore<LaunchPreferencesProto> =
        DataStoreFactory.create(
            serializer = preferencesSerializer,
            scope = scope
        ) {
            context.dataStoreFile(LAUNCH_PREFERENCES)
        }

    private companion object {
        const val LAUNCH_PREFERENCES = "launch_preferences"
    }
}
