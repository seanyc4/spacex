package com.seancoyle.feature.launch.presentation.util

import java.time.Duration
import java.time.LocalDateTime

/**
 * Utility object for launch timeline calculations.
 * Extracted from composable files to improve testability and separation of concerns.
 */
object LaunchTimelineCalculator {

    /**
     * Calculates the position of the launch time within the launch window.
     * Returns a value between 0.0 and 1.0 representing the position in the window.
     *
     * @param windowStart The start of the launch window
     * @param windowEnd The end of the launch window
     * @param launchTime The actual launch time
     * @return A float between 0.0 and 1.0 representing the launch position within the window
     */
    fun calculateLaunchPosition(
        windowStart: LocalDateTime?,
        windowEnd: LocalDateTime?,
        launchTime: LocalDateTime?
    ): Float {
        if (windowStart == null || windowEnd == null || launchTime == null) return 0.5f

        // If launch time is before window, show at start
        if (launchTime.isBefore(windowStart)) return 0f

        // If launch time is after window, show at end
        if (launchTime.isAfter(windowEnd)) return 1f

        // Calculate position of launch time within the window
        val totalDuration = Duration.between(windowStart, windowEnd).toMillis().toFloat()
        val launchOffset = Duration.between(windowStart, launchTime).toMillis().toFloat()

        // Avoid division by zero
        if (totalDuration == 0f) return 0.5f

        // Return position between 0 and 1
        return (launchOffset / totalDuration).coerceIn(0f, 1f)
    }

    /**
     * Calculates the duration between two times.
     *
     * @param startTime The start time
     * @param endTime The end time
     * @return Duration object representing the time between start and end
     */
    fun calculateDuration(startTime: LocalDateTime?, endTime: LocalDateTime?): Duration? {
        if (startTime == null || endTime == null) return null
        return Duration.between(startTime, endTime)
    }

    /**
     * Checks if a launch window is instantaneous (start and end are the same).
     *
     * @param windowStart The start of the launch window
     * @param windowEnd The end of the launch window
     * @return True if the window is instantaneous
     */
    fun isInstantaneousWindow(windowStart: LocalDateTime?, windowEnd: LocalDateTime?): Boolean {
        if (windowStart == null || windowEnd == null) return false
        return windowStart == windowEnd
    }
}

