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

    fun trackDetailSectionExpand(
        launchId: String,
        sectionName: String,
        launchType: String
    )

    fun trackDetailTimeSpent(
        launchId: String,
        launchType: String,
        durationSeconds: Int
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

    fun trackVideoWatchDuration(
        launchId: String,
        videoId: String,
        durationSeconds: Int,
        percentWatched: Int,
        launchType: String
    )

    fun trackExternalLinkTap(
        launchId: String,
        linkType: String,
        launchType: String
    )

    fun trackPaginationLoad(
        launchType: String,
        pageNumber: Int,
        itemCount: Int
    )

    fun trackScreenView(screenName: String, launchType: String? = null)

    fun trackPullRefresh(launchType: String)

    fun trackRetryTap(launchType: String)

    fun trackErrorDisplayed(errorType: String, launchType: String)

    fun trackScrollDepth(
        screenName: String,
        launchType: String,
        percentScrolled: Int
    )
}
