package com.seancoyle.feature.launch.domain.usecase.analytics

import com.seancoyle.core.common.analytics.AnalyticsEvents
import com.seancoyle.core.common.analytics.AnalyticsLogger
import com.seancoyle.core.common.analytics.AnalyticsParams
import javax.inject.Inject

internal class LaunchAnalyticsComponentImpl @Inject constructor(
    private val analyticsLogger: AnalyticsLogger
) : LaunchAnalyticsComponent {

    override fun trackListItemClick(
        launchId: String,
        launchType: String,
        position: Int,
        status: String
    ) {
        analyticsLogger.logEvent(
            AnalyticsEvents.LIST_ITEM_CLICK,
            mapOf(
                AnalyticsParams.LAUNCH_ID to launchId,
                AnalyticsParams.LAUNCH_TYPE to launchType,
                AnalyticsParams.POSITION to position.toString(),
                AnalyticsParams.STATUS to status
            )
        )
    }

    override fun trackDetailView(
        launchId: String,
        launchType: String,
        status: String,
        hasVideo: Boolean,
        agency: String
    ) {
        analyticsLogger.logEvent(
            AnalyticsEvents.DETAIL_VIEW,
            mapOf(
                AnalyticsParams.LAUNCH_ID to launchId,
                AnalyticsParams.LAUNCH_TYPE to launchType,
                AnalyticsParams.STATUS to status,
                AnalyticsParams.HAS_VIDEO to hasVideo.toString(),
                AnalyticsParams.AGENCY to agency
            )
        )
    }

    override fun trackTabSwitch(fromTab: String, toTab: String) {
        analyticsLogger.logEvent(
            AnalyticsEvents.TAB_SWITCH,
            mapOf(
                AnalyticsParams.FROM_TAB to fromTab,
                AnalyticsParams.TO_TAB to toTab
            )
        )
    }

    override fun trackFilterOpen(launchType: String, filterCount: Int) {
        analyticsLogger.logEvent(
            AnalyticsEvents.FILTER_OPEN,
            mapOf(
                AnalyticsParams.LAUNCH_TYPE to launchType,
                AnalyticsParams.FILTER_COUNT to filterCount.toString()
            )
        )
    }

    override fun trackFilterApply(
        status: String,
        hasQuery: Boolean,
        queryLength: Int,
        filterCount: Int
    ) {
        analyticsLogger.logEvent(
            AnalyticsEvents.FILTER_APPLY,
            mapOf(
                AnalyticsParams.STATUS to status,
                AnalyticsParams.HAS_QUERY to hasQuery.toString(),
                AnalyticsParams.QUERY_LENGTH to queryLength.toString(),
                AnalyticsParams.FILTER_COUNT to filterCount.toString()
            )
        )
    }

    override fun trackFilterClear(filterCount: Int) {
        analyticsLogger.logEvent(
            AnalyticsEvents.FILTER_CLEAR,
            mapOf(AnalyticsParams.FILTER_COUNT to filterCount.toString())
        )
    }

    override fun trackRecentSearchTap() {
        analyticsLogger.logEvent(AnalyticsEvents.RECENT_SEARCH_TAP)
    }

    override fun trackVideoPlay(
        launchId: String,
        videoId: String,
        isLive: Boolean,
        launchType: String
    ) {
        analyticsLogger.logEvent(
            AnalyticsEvents.VIDEO_PLAY,
            mapOf(
                AnalyticsParams.LAUNCH_ID to launchId,
                AnalyticsParams.VIDEO_ID to videoId,
                AnalyticsParams.IS_LIVE to isLive.toString(),
                AnalyticsParams.LAUNCH_TYPE to launchType
            )
        )
    }

    override fun trackExternalLinkTap(
        launchId: String,
        linkType: String,
        launchType: String
    ) {
        analyticsLogger.logEvent(
            AnalyticsEvents.EXTERNAL_LINK_TAP,
            mapOf(
                AnalyticsParams.LAUNCH_ID to launchId,
                AnalyticsParams.LINK_TYPE to linkType,
                AnalyticsParams.LAUNCH_TYPE to launchType
            )
        )
    }

    override fun trackPullRefresh(launchType: String) {
        analyticsLogger.logEvent(
            AnalyticsEvents.PULL_REFRESH,
            mapOf(AnalyticsParams.LAUNCH_TYPE to launchType)
        )
    }

    override fun trackRetryTap(launchType: String) {
        analyticsLogger.logEvent(
            AnalyticsEvents.RETRY_TAP,
            mapOf(AnalyticsParams.LAUNCH_TYPE to launchType)
        )
    }

    override fun trackErrorDisplayed(errorType: String, launchType: String) {
        analyticsLogger.logEvent(
            AnalyticsEvents.ERROR_DISPLAYED,
            mapOf(
                AnalyticsParams.ERROR_TYPE to errorType,
                AnalyticsParams.LAUNCH_TYPE to launchType
            )
        )
    }
}
