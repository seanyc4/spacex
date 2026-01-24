package com.seancoyle.core.common.analytics

object AnalyticsEvents {

    // ==========================================
    // ENGAGEMENT EVENTS
    // ==========================================

    /** User viewed a screen */
    const val SCREEN_VIEW = "screen_view"

    /** User started a new session */
    const val SESSION_START = "session_start"

    /** User scrolled through the launch list */
    const val LIST_SCROLL_DEPTH = "list_scroll_depth"

    /** User tapped on a launch card in the list */
    const val LIST_ITEM_CLICK = "list_item_click"

    /** User viewed launch detail screen */
    const val DETAIL_VIEW = "detail_view"

    /** User expanded a collapsible section on detail */
    const val DETAIL_SECTION_EXPAND = "detail_section_expand"

    /** Time user spent on detail screen (logged on exit) */
    const val DETAIL_TIME_SPENT = "detail_time_spent"

    /** User scrolled through detail content */
    const val DETAIL_SCROLL_DEPTH = "detail_scroll_depth"

    /** User switched between upcoming/past tabs */
    const val TAB_SWITCH = "tab_switch"

    /** User played a video */
    const val VIDEO_PLAY = "video_play"

    /** User watched video for a duration */
    const val VIDEO_WATCH_DURATION = "video_watch_duration"

    /** User tapped an external link (wiki, map, etc.) */
    const val EXTERNAL_LINK_TAP = "external_link_tap"

    /** User interacted with the launch site map */
    const val MAP_INTERACTION = "map_interaction"

    /** User viewed mission patch images */
    const val MISSION_PATCH_VIEW = "mission_patch_view"

    /** User zoomed an image */
    const val IMAGE_ZOOM = "image_zoom"

    // ==========================================
    // SEARCH & FILTER EVENTS
    // ==========================================

    /** User opened the filter bottom sheet */
    const val FILTER_OPEN = "filter_open"

    /** User applied filters */
    const val FILTER_APPLY = "filter_apply"

    /** User cleared all filters */
    const val FILTER_CLEAR = "filter_clear"

    /** User performed a search (metadata only, not query text) */
    const val SEARCH_QUERY = "search_query"

    /** Search returned results */
    const val SEARCH_RESULT_COUNT = "search_result_count"

    /** User clicked on a search result */
    const val SEARCH_RESULT_CLICK = "search_result_click"

    /** User abandoned search without clicking a result */
    const val SEARCH_ABANDON = "search_abandon"

    /** User tapped a recent search item */
    const val RECENT_SEARCH_TAP = "recent_search_tap"

    // ==========================================
    // PERFORMANCE EVENTS
    // ==========================================

    /** Time to load the launch list */
    const val LIST_LOAD_TIME = "list_load_time"

    /** Time to load launch detail */
    const val DETAIL_LOAD_TIME = "detail_load_time"

    /** Time to load an image */
    const val IMAGE_LOAD_TIME = "image_load_time"

    /** User pulled to refresh */
    const val PULL_REFRESH = "pull_refresh"

    /** Pagination triggered */
    const val PAGINATION_LOAD = "pagination_load"

    /** Error shown to user */
    const val ERROR_DISPLAYED = "error_displayed"

    /** User tapped retry after error */
    const val RETRY_TAP = "retry_tap"
}
