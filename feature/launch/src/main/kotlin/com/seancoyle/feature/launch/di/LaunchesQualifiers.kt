package com.seancoyle.feature.launch.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class PastLaunches

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class UpcomingLaunches
