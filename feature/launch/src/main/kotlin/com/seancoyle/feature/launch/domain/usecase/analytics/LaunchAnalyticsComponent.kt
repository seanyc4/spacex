package com.seancoyle.feature.launch.domain.usecase.analytics

interface LaunchAnalyticsComponent {

    fun trackListItemClick(
        launchId: String,
        launchType: String,
        position: Int,
        status: String
    )

    fun trackDetailView(
        launchId: String,
        launchType: String,
        status: String,
        hasVideo: Boolean,
        agency: String
    )

    fun trackTabSwitch(fromTab: String, toTab: String)

    fun trackFilterOpen(launchType: String, filterCount: Int)

    fun trackFilterApply(
        status: String,
        hasQuery: Boolean,
        queryLength: Int,
        filterCount: Int
    )

    fun trackFilterClear(filterCount: Int)

    fun trackRecentSearchTap()

    fun trackVideoPlay(
        launchId: String,
        videoId: String,
        isLive: Boolean,
        launchType: String
    )

    fun trackExternalLinkTap(
        launchId: String,
        linkType: String,
        launchType: String
    )

    fun trackPullRefresh(launchType: String)

    fun trackRetryTap(launchType: String)

    fun trackErrorDisplayed(errorType: String, launchType: String)
}
