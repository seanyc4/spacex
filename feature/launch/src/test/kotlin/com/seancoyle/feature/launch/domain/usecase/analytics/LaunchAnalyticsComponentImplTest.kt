package com.seancoyle.feature.launch.domain.usecase.analytics

import com.seancoyle.core.common.analytics.AnalyticsEvents
import com.seancoyle.core.common.analytics.AnalyticsLogger
import com.seancoyle.core.common.analytics.AnalyticsParams
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class LaunchAnalyticsComponentImplTest {

    @MockK(relaxed = true)
    private lateinit var analyticsLogger: AnalyticsLogger

    private lateinit var underTest: LaunchAnalyticsComponent

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchAnalyticsComponentImpl(analyticsLogger)
    }

    @Test
    fun `GIVEN launch data WHEN trackListItemClick THEN logs event with correct params`() {
        underTest.trackListItemClick(
            launchId = "launch-123",
            launchType = "UPCOMING",
            position = 5,
            status = "GO"
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.LIST_ITEM_CLICK,
                mapOf(
                    AnalyticsParams.LAUNCH_ID to "launch-123",
                    AnalyticsParams.LAUNCH_TYPE to "UPCOMING",
                    AnalyticsParams.POSITION to "5",
                    AnalyticsParams.STATUS to "GO"
                )
            )
        }
    }

    @Test
    fun `GIVEN launch details WHEN trackDetailView THEN logs event with correct params`() {
        underTest.trackDetailView(
            launchId = "launch-456",
            launchType = "PAST",
            status = "SUCCESS",
            hasVideo = true,
            agency = "SpaceX"
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.DETAIL_VIEW,
                mapOf(
                    AnalyticsParams.LAUNCH_ID to "launch-456",
                    AnalyticsParams.LAUNCH_TYPE to "PAST",
                    AnalyticsParams.STATUS to "SUCCESS",
                    AnalyticsParams.HAS_VIDEO to "true",
                    AnalyticsParams.AGENCY to "SpaceX"
                )
            )
        }
    }

    @Test
    fun `GIVEN tab names WHEN trackTabSwitch THEN logs event with correct params`() {
        underTest.trackTabSwitch(fromTab = "UPCOMING", toTab = "PAST")

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.TAB_SWITCH,
                mapOf(
                    AnalyticsParams.FROM_TAB to "UPCOMING",
                    AnalyticsParams.TO_TAB to "PAST"
                )
            )
        }
    }

    @Test
    fun `GIVEN filter state WHEN trackFilterOpen THEN logs event with correct params`() {
        underTest.trackFilterOpen(launchType = "UPCOMING", filterCount = 2)

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.FILTER_OPEN,
                mapOf(
                    AnalyticsParams.LAUNCH_TYPE to "UPCOMING",
                    AnalyticsParams.FILTER_COUNT to "2"
                )
            )
        }
    }

    @Test
    fun `GIVEN filter data WHEN trackFilterApply THEN logs event with correct params`() {
        underTest.trackFilterApply(
            status = "SUCCESS",
            hasQuery = true,
            queryLength = 6,
            filterCount = 2
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.FILTER_APPLY,
                mapOf(
                    AnalyticsParams.STATUS to "SUCCESS",
                    AnalyticsParams.HAS_QUERY to "true",
                    AnalyticsParams.QUERY_LENGTH to "6",
                    AnalyticsParams.FILTER_COUNT to "2"
                )
            )
        }
    }

    @Test
    fun `GIVEN no query WHEN trackFilterApply THEN logs event with hasQuery false`() {
        underTest.trackFilterApply(
            status = "ALL",
            hasQuery = false,
            queryLength = 0,
            filterCount = 0
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.FILTER_APPLY,
                mapOf(
                    AnalyticsParams.STATUS to "ALL",
                    AnalyticsParams.HAS_QUERY to "false",
                    AnalyticsParams.QUERY_LENGTH to "0",
                    AnalyticsParams.FILTER_COUNT to "0"
                )
            )
        }
    }

    @Test
    fun `GIVEN active filters WHEN trackFilterClear THEN logs event with filter count`() {
        underTest.trackFilterClear(filterCount = 2)

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.FILTER_CLEAR,
                mapOf(AnalyticsParams.FILTER_COUNT to "2")
            )
        }
    }

    @Test
    fun `WHEN trackRecentSearchTap THEN logs event without params`() {
        underTest.trackRecentSearchTap()

        verify {
            analyticsLogger.logEvent(AnalyticsEvents.RECENT_SEARCH_TAP)
        }
    }

    @Test
    fun `GIVEN video data WHEN trackVideoPlay THEN logs event with correct params`() {
        underTest.trackVideoPlay(
            launchId = "launch-789",
            videoId = "video-abc",
            isLive = true,
            launchType = "UPCOMING"
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.VIDEO_PLAY,
                mapOf(
                    AnalyticsParams.LAUNCH_ID to "launch-789",
                    AnalyticsParams.VIDEO_ID to "video-abc",
                    AnalyticsParams.IS_LIVE to "true",
                    AnalyticsParams.LAUNCH_TYPE to "UPCOMING"
                )
            )
        }
    }

    @Test
    fun `GIVEN non-live video WHEN trackVideoPlay THEN logs isLive as false`() {
        underTest.trackVideoPlay(
            launchId = "launch-789",
            videoId = "video-abc",
            isLive = false,
            launchType = "PAST"
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.VIDEO_PLAY,
                mapOf(
                    AnalyticsParams.LAUNCH_ID to "launch-789",
                    AnalyticsParams.VIDEO_ID to "video-abc",
                    AnalyticsParams.IS_LIVE to "false",
                    AnalyticsParams.LAUNCH_TYPE to "PAST"
                )
            )
        }
    }

    @Test
    fun `GIVEN link data WHEN trackExternalLinkTap THEN logs event with correct params`() {
        underTest.trackExternalLinkTap(
            launchId = "launch-123",
            linkType = "wiki",
            launchType = "UPCOMING"
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.EXTERNAL_LINK_TAP,
                mapOf(
                    AnalyticsParams.LAUNCH_ID to "launch-123",
                    AnalyticsParams.LINK_TYPE to "wiki",
                    AnalyticsParams.LAUNCH_TYPE to "UPCOMING"
                )
            )
        }
    }

    @Test
    fun `GIVEN launch type WHEN trackPullRefresh THEN logs event with correct params`() {
        underTest.trackPullRefresh(launchType = "UPCOMING")

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.PULL_REFRESH,
                mapOf(AnalyticsParams.LAUNCH_TYPE to "UPCOMING")
            )
        }
    }

    @Test
    fun `GIVEN launch type WHEN trackRetryTap THEN logs event with correct params`() {
        underTest.trackRetryTap(launchType = "PAST")

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.RETRY_TAP,
                mapOf(AnalyticsParams.LAUNCH_TYPE to "PAST")
            )
        }
    }

    @Test
    fun `GIVEN error data WHEN trackErrorDisplayed THEN logs event with correct params`() {
        underTest.trackErrorDisplayed(
            errorType = "NETWORK_ERROR",
            launchType = "UPCOMING"
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.ERROR_DISPLAYED,
                mapOf(
                    AnalyticsParams.ERROR_TYPE to "NETWORK_ERROR",
                    AnalyticsParams.LAUNCH_TYPE to "UPCOMING"
                )
            )
        }
    }

    @Test
    fun `GIVEN section data WHEN trackDetailSectionExpand THEN logs event with correct params`() {
        underTest.trackDetailSectionExpand(
            launchId = "launch-123",
            sectionName = "rocket_info",
            launchType = "UPCOMING"
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.DETAIL_SECTION_EXPAND,
                mapOf(
                    AnalyticsParams.LAUNCH_ID to "launch-123",
                    AnalyticsParams.SECTION to "rocket_info",
                    AnalyticsParams.LAUNCH_TYPE to "UPCOMING"
                )
            )
        }
    }

    @Test
    fun `GIVEN time spent data WHEN trackDetailTimeSpent THEN logs event with correct params`() {
        underTest.trackDetailTimeSpent(
            launchId = "launch-123",
            launchType = "PAST",
            durationSeconds = 45
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.DETAIL_TIME_SPENT,
                mapOf(
                    AnalyticsParams.LAUNCH_ID to "launch-123",
                    AnalyticsParams.LAUNCH_TYPE to "PAST",
                    AnalyticsParams.DURATION_SECONDS to "45"
                )
            )
        }
    }

    @Test
    fun `GIVEN video watch data WHEN trackVideoWatchDuration THEN logs event with correct params`() {
        underTest.trackVideoWatchDuration(
            launchId = "launch-123",
            videoId = "video-abc",
            durationSeconds = 120,
            percentWatched = 75,
            launchType = "UPCOMING"
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.VIDEO_WATCH_DURATION,
                mapOf(
                    AnalyticsParams.LAUNCH_ID to "launch-123",
                    AnalyticsParams.VIDEO_ID to "video-abc",
                    AnalyticsParams.DURATION_SECONDS to "120",
                    AnalyticsParams.PERCENT_WATCHED to "75",
                    AnalyticsParams.LAUNCH_TYPE to "UPCOMING"
                )
            )
        }
    }

    @Test
    fun `GIVEN pagination data WHEN trackPaginationLoad THEN logs event with correct params`() {
        underTest.trackPaginationLoad(
            launchType = "PAST",
            pageNumber = 3,
            itemCount = 20
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.PAGINATION_LOAD,
                mapOf(
                    AnalyticsParams.LAUNCH_TYPE to "PAST",
                    AnalyticsParams.PAGE_NUMBER to "3",
                    AnalyticsParams.ITEM_COUNT to "20"
                )
            )
        }
    }

    @Test
    fun `GIVEN screen data WHEN trackScreenView THEN logs event with correct params`() {
        underTest.trackScreenView(
            screenName = "launches",
            launchType = "UPCOMING"
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.SCREEN_VIEW,
                mapOf(
                    AnalyticsParams.SCREEN_NAME to "launches",
                    AnalyticsParams.LAUNCH_TYPE to "UPCOMING"
                )
            )
        }
    }

    @Test
    fun `GIVEN screen without launch type WHEN trackScreenView THEN logs event without launch type`() {
        underTest.trackScreenView(screenName = "settings")

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.SCREEN_VIEW,
                mapOf(AnalyticsParams.SCREEN_NAME to "settings")
            )
        }
    }

    @Test
    fun `GIVEN scroll data WHEN trackScrollDepth THEN logs event with correct params`() {
        underTest.trackScrollDepth(
            screenName = "launches_list",
            launchType = "UPCOMING",
            percentScrolled = 50
        )

        verify {
            analyticsLogger.logEvent(
                AnalyticsEvents.LIST_SCROLL_DEPTH,
                mapOf(
                    AnalyticsParams.SCREEN_NAME to "launches_list",
                    AnalyticsParams.LAUNCH_TYPE to "UPCOMING",
                    AnalyticsParams.PERCENT_SCROLLED to "50"
                )
            )
        }
    }
}
