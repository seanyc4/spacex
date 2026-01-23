package com.seancoyle.feature.launch.data.local

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.PastLaunchEntity
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class PastLaunchDaoTest {

    @Test
    fun givenPastLaunchEntity_whenCreated_thenHasCorrectProperties() {
        val status = LaunchStatusEntity(
            id = 3,
            name = "Success",
            abbrev = "Success",
            description = "Launch was successful"
        )
        val entity = PastLaunchEntity(
            id = "past-1",
            missionName = "Past Mission",
            net = "2024-06-15T10:00:00Z",
            imageUrl = "https://example.com/image.jpg",
            status = status,
            location = "United States of America"
        )

        assertEquals("past-1", entity.id)
        assertEquals("Past Mission", entity.missionName)
        assertEquals("2024-06-15T10:00:00Z", entity.net)
        assertEquals("https://example.com/image.jpg", entity.imageUrl)
        assertEquals(3, entity.status.id)
        assertEquals("Success", entity.status.name)
    }

    @Test
    fun givenPastLaunchWithFailedStatus_whenCreated_thenStatusCorrect() {
        val status = LaunchStatusEntity(
            id = 4,
            name = "Failed",
            abbrev = "Failed",
            description = "Launch failed"
        )
        val entity = PastLaunchEntity(
            id = "past-1",
            missionName = "Failed Mission",
            net = "2024-01-15T10:00:00Z",
            imageUrl = "https://example.com/image.jpg",
            status = status,
            location = "United States of America"
        )

        assertEquals(4, entity.status.id)
        assertEquals("Failed", entity.status.name)
        assertEquals("Failed", entity.status.abbrev)
    }

    @Test
    fun givenMultiplePastLaunchEntities_whenCreated_thenAllHaveUniqueIds() {
        val entities = (1..5).map { index ->
            PastLaunchEntity(
                id = "past-$index",
                missionName = "Past Mission $index",
                net = "2024-0$index-15T10:00:00Z",
                imageUrl = "https://example.com/image$index.jpg",
                status = LaunchStatusEntity(
                    id = 3,
                    name = "Success",
                    abbrev = "Success",
                    description = "Launch was successful"
                ),
                location = "United States of America"
            )
        }

        val uniqueIds = entities.map { it.id }.toSet()
        assertEquals(5, uniqueIds.size)
    }

    @Test
    fun givenPastLaunchEntity_whenCopied_thenOriginalUnchanged() {
        val original = PastLaunchEntity(
            id = "past-1",
            missionName = "Original Past Mission",
            net = "2024-06-15T10:00:00Z",
            imageUrl = "https://example.com/image.jpg",
            status = LaunchStatusEntity(
                id = 3,
                name = "Success",
                abbrev = "Success",
                description = "Successful"
            ),
            location = "United States of America"
        )

        val copy = original.copy(missionName = "Updated Past Mission")

        assertEquals("Original Past Mission", original.missionName)
        assertEquals("Updated Past Mission", copy.missionName)
        assertEquals(original.id, copy.id)
    }

    @Test
    fun givenLaunchStatusEntity_whenAllStatusesCreated_thenCorrectValues() {
        val goStatus = LaunchStatusEntity(id = 1, name = "Go", abbrev = "Go", description = "Go for launch")
        val tbdStatus = LaunchStatusEntity(id = 2, name = "TBD", abbrev = "TBD", description = "To be determined")
        val successStatus = LaunchStatusEntity(id = 3, name = "Success", abbrev = "Success", description = "Successful")
        val failedStatus = LaunchStatusEntity(id = 4, name = "Failed", abbrev = "Failed", description = "Launch failed")

        assertEquals(1, goStatus.id)
        assertEquals(2, tbdStatus.id)
        assertEquals(3, successStatus.id)
        assertEquals(4, failedStatus.id)
    }
}
