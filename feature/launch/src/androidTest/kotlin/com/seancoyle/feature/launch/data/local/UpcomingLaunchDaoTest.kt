package com.seancoyle.feature.launch.data.local

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.UpcomingLaunchEntity
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class UpcomingLaunchDaoTest {

    @Test
    fun givenLaunchEntity_whenCreated_thenHasCorrectProperties() {
        val status = LaunchStatusEntity(
            id = 1,
            name = "Go for Launch",
            abbrev = "Go",
            description = "Launch is confirmed"
        )
        val entity = UpcomingLaunchEntity(
            id = "test-1",
            missionName = "Test Mission",
            net = "2026-01-15T10:00:00Z",
            imageUrl = "https://example.com/image.jpg",
            status = status,
            location = "United States of America"
        )

        assertEquals("test-1", entity.id)
        assertEquals("Test Mission", entity.missionName)
        assertEquals("2026-01-15T10:00:00Z", entity.net)
        assertEquals("https://example.com/image.jpg", entity.imageUrl)
        assertEquals(1, entity.status.id)
        assertEquals("Go for Launch", entity.status.name)
    }

    @Test
    fun givenLaunchStatusEntity_whenCreated_thenHasCorrectProperties() {
        val status = LaunchStatusEntity(
            id = 3,
            name = "Success",
            abbrev = "Success",
            description = "Launch was successful"
        )

        assertEquals(3, status.id)
        assertEquals("Success", status.name)
        assertEquals("Success", status.abbrev)
        assertEquals("Launch was successful", status.description)
    }

    @Test
    fun givenMultipleLaunchEntities_whenCreated_thenAllHaveUniqueIds() {
        val entities = (1..5).map { index ->
            UpcomingLaunchEntity(
                id = "test-$index",
                missionName = "Mission $index",
                net = "2026-01-15T10:00:00Z",
                imageUrl = "https://example.com/image.jpg",
                status = LaunchStatusEntity(
                    id = 1,
                    name = "Go",
                    abbrev = "Go",
                    description = "Go for launch"
                ),
                location = "United States of America"
            )
        }

        val uniqueIds = entities.map { it.id }.toSet()
        assertEquals(5, uniqueIds.size)
    }

    @Test
    fun givenLaunchEntity_whenCopied_thenOriginalUnchanged() {
        val original = UpcomingLaunchEntity(
            id = "test-1",
            missionName = "Original",
            net = "2026-01-15T10:00:00Z",
            imageUrl = "https://example.com/image.jpg",
            status = LaunchStatusEntity(
                id = 1,
                name = "Go",
                abbrev = "Go",
                description = "Go for launch"
            ),
            location = "United States of America"
        )

        val copy = original.copy(missionName = "Updated")

        assertEquals("Original", original.missionName)
        assertEquals("Updated", copy.missionName)
        assertEquals(original.id, copy.id)
    }
}
