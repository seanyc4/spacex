package com.seancoyle.spacex.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoFactory
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.framework.datasource.cache.database.Database
import com.seancoyle.spacex.framework.datasource.data.company.CompanyInfoDataFactory
import com.seancoyle.spacex.framework.datasource.data.launch.LaunchDataFactory
import com.seancoyle.spacex.framework.presentation.launch.LaunchViewModel
import com.seancoyle.spacex.util.AndroidTestUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ProductionModule::class]
)
object TestModule {

    @Singleton
    @Provides
    fun provideAndroidTestUtils(): AndroidTestUtils {
        return AndroidTestUtils(true)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(
        application: HiltTestApplication
    ): SharedPreferences {
        return application
            .getSharedPreferences(
                LaunchViewModel.LAUNCH_PREFERENCES,
                Context.MODE_PRIVATE
            )
    }

    @Singleton
    @Provides
    fun provideSpaceXDb(app: HiltTestApplication): Database {
        return Room
            .inMemoryDatabaseBuilder(app, Database::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideLaunchDataFactory(
        application: HiltTestApplication,
        launchFactory: LaunchFactory
    ): LaunchDataFactory {
        return LaunchDataFactory(application, launchFactory)
    }

    @Singleton
    @Provides
    fun provideCompanyInfoDataFactory(
        application: HiltTestApplication,
        companyInfoFactory: CompanyInfoFactory
    ): CompanyInfoDataFactory {
        return CompanyInfoDataFactory(application, companyInfoFactory)
    }

}















