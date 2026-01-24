package com.seancoyle.feature.launch.data.local

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.data.repository.DetailLocalDataSource
import com.seancoyle.feature.launch.di.PastLaunches
import com.seancoyle.feature.launch.domain.model.Configuration
import com.seancoyle.feature.launch.domain.model.Image
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.Mission
import com.seancoyle.feature.launch.domain.model.Pad
import com.seancoyle.feature.launch.domain.model.Rocket
import com.seancoyle.feature.launch.domain.model.Status
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
internal class LaunchesLocalDataSourceTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @PastLaunches
    lateinit var underTest: DetailLocalDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() = runTest {
        underTest.deleteAllLaunchDetails()
    }

    @Test
    fun givenLaunchDetail_whenUpserted_thenCanBeRetrieved() = runTest {
        val testLaunch = createTestLaunch(id = "launch-1")

        val upsertResult = underTest.upsertLaunchDetail(testLaunch)

        assertTrue(upsertResult is LaunchResult.Success)
        val getResult = underTest.getLaunchDetail(id = "launch-1")
        assertTrue(getResult is LaunchResult.Success)
        val retrievedLaunch = getResult.data
        assertNotNull(retrievedLaunch)
        assertEquals(testLaunch.id, retrievedLaunch.id)
        assertEquals(testLaunch.missionName, retrievedLaunch.missionName)
        assertEquals(testLaunch.net, retrievedLaunch.net)
        assertEquals(testLaunch.status.name, retrievedLaunch.status.name)
        assertEquals(testLaunch.status.abbrev, retrievedLaunch.status.abbrev)
    }

    @Test
    fun givenExistingLaunch_whenUpsertedWithSameId_thenUpdatesExistingRecord() = runTest {
        val originalLaunch = createTestLaunch(id = "launch-1", name = "Original Name")
        underTest.upsertLaunchDetail(originalLaunch)
        val updatedLaunch = originalLaunch.copy(missionName = "Updated Name")

        val upsertResult = underTest.upsertLaunchDetail(updatedLaunch)

        assertTrue(upsertResult is LaunchResult.Success)
        val getResult = underTest.getLaunchDetail(id = "launch-1")
        assertTrue(getResult is LaunchResult.Success)
        assertEquals("Updated Name", getResult.data?.missionName)
    }

    @Test
    fun givenMultipleLaunches_whenUpsertAll_thenAllAreInserted() = runTest {
        val testLaunches = createTestLaunchList(10)

        val result = underTest.upsertAllLaunchDetails(testLaunches)

        assertTrue(result is LaunchResult.Success)
        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(10, countResult.data)
    }

    @Test
    fun givenDuplicateLaunches_whenUpsertAll_thenUpdatesExisting() = runTest {
        val initialLaunches = createTestLaunchList(5)
        underTest.upsertAllLaunchDetails(initialLaunches)
        val updatedLaunches = initialLaunches.map { it.copy(missionName = "Updated ${it.missionName}") }

        val result = underTest.upsertAllLaunchDetails(updatedLaunches)

        assertTrue(result is LaunchResult.Success)
        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(5, countResult.data)
        val firstLaunch = underTest.getLaunchDetail("test-1")
        assertTrue(firstLaunch is LaunchResult.Success)
        assertTrue(firstLaunch.data?.missionName?.startsWith("Updated") ?: false)
    }

    @Test
    fun givenEmptyList_whenUpsertAll_thenReturnsSuccess() = runTest {
        val result = underTest.upsertAllLaunchDetails(emptyList())

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun givenNonExistentId_whenGetLaunchDetail_thenReturnsSuccessWithNull() = runTest {
        val result = underTest.getLaunchDetail(id = "non-existent-id")

        assertTrue(result is LaunchResult.Success)
        assertNull(result.data)
    }

    @Test
    fun givenEmptyDatabase_whenGetTotalEntries_thenReturnsZero() = runTest {
        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(0, result.data)
    }

    @Test
    fun givenMultipleLaunches_whenGetTotalEntries_thenReturnsCorrectCount() = runTest {
        val testLaunches = createTestLaunchList(25)
        underTest.upsertAllLaunchDetails(testLaunches)

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(25, result.data)
    }

    @Test
    fun givenDuplicatesUpserted_whenGetTotalEntries_thenDoesNotDoubleCount() = runTest {
        val testLaunches = createTestLaunchList(5)
        underTest.upsertAllLaunchDetails(testLaunches)
        underTest.upsertAllLaunchDetails(testLaunches)

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(5, result.data)
    }

    @Test
    fun givenLaunchesExist_whenDeleteAll_thenRemovesAllEntries() = runTest {
        val testLaunches = createTestLaunchList(20)
        underTest.upsertAllLaunchDetails(testLaunches)
        val preDeleteCount = underTest.getTotalEntries()
        assertTrue(preDeleteCount is LaunchResult.Success)
        assertEquals(20, preDeleteCount.data)

        val deleteResult = underTest.deleteAllLaunchDetails()

        assertTrue(deleteResult is LaunchResult.Success)
        val postDeleteCount = underTest.getTotalEntries()
        assertTrue(postDeleteCount is LaunchResult.Success)
        assertEquals(0, postDeleteCount.data)
    }

    @Test
    fun givenEmptyDatabase_whenDeleteAll_thenReturnsSuccess() = runTest {
        val result = underTest.deleteAllLaunchDetails()

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun givenLaunchExists_whenDeleteAllAndGetLaunchDetail_thenReturnsNull() = runTest {
        val testLaunch = createTestLaunch(id = "launch-1")
        underTest.upsertLaunchDetail(testLaunch)

        underTest.deleteAllLaunchDetails()
        val result = underTest.getLaunchDetail(id = "launch-1")

        assertTrue(result is LaunchResult.Success)
        assertNull(result.data)
    }

    @Test
    fun givenInitialLaunches_whenRefreshLaunches_thenReplacesAllData() = runTest {
        val initialLaunches = createTestLaunchList(10)
        underTest.upsertAllLaunchDetails(initialLaunches)
        val newLaunches = createTestLaunchList(5).map { it.copy(id = "new-${it.id}") }

        underTest.refreshLaunches(newLaunches)

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(5, countResult.data)
        val oldLaunch = underTest.getLaunchDetail("test-1")
        assertTrue(oldLaunch is LaunchResult.Success)
        assertNull(oldLaunch.data)
        val newLaunch = underTest.getLaunchDetail("new-test-1")
        assertTrue(newLaunch is LaunchResult.Success)
        assertNotNull(newLaunch.data)
    }

    @Test
    fun givenLargeDataSet_whenUpsertAll_thenHandlesCorrectly() = runTest {
        val largeLaunchList = createTestLaunchList(200)

        val upsertResult = underTest.upsertAllLaunchDetails(largeLaunchList)

        assertTrue(upsertResult is LaunchResult.Success)
        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(200, countResult.data)
    }

    @Test
    fun givenLargeDataSet_whenRefreshLaunches_thenReplacesCorrectly() = runTest {
        val initialData = createTestLaunchList(100)
        underTest.upsertAllLaunchDetails(initialData)
        val newData = createTestLaunchList(200).map { it.copy(id = "new-${it.id}") }

        underTest.refreshLaunches(newData)

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(200, countResult.data)
    }

    @Test
    fun givenSpecialCharactersInId_whenUpsertLaunchDetail_thenHandlesCorrectly() = runTest {
        val testLaunch = createTestLaunch(id = "launch-special-123")

        val result = underTest.upsertLaunchDetail(testLaunch)

        assertTrue(result is LaunchResult.Success)
        val getResult = underTest.getLaunchDetail("launch-special-123")
        assertTrue(getResult is LaunchResult.Success)
        assertNotNull(getResult.data)
    }

    @Test
    fun givenMultipleRefreshes_whenPerformed_thenMaintainsDataIntegrity() = runTest {
        repeat(5) { iteration ->
            val launches = createTestLaunchList(10).map { it.copy(id = "iteration-$iteration-${it.id}") }

            underTest.refreshLaunches(launches)

            val count = underTest.getTotalEntries()
            assertTrue(count is LaunchResult.Success)
            assertEquals(10, count.data)
        }
    }

    @Test
    fun givenLaunchesWithDifferentStatuses_whenUpsertAll_thenPreservesStatuses() = runTest {
        val launches = listOf(
            createTestLaunch(id = "launch-1", status = Status(1, "Go for Launch", "Go", "Launch is confirmed")),
            createTestLaunch(id = "launch-2", status = Status(2, "TBD", "TBD", "To be determined")),
            createTestLaunch(id = "launch-3", status = Status(3, "Success", "Success", "Launch was successful")),
            createTestLaunch(id = "launch-4", status = Status(4, "Failure", "Failed", "Launch failed"))
        )

        val result = underTest.upsertAllLaunchDetails(launches)

        assertTrue(result is LaunchResult.Success)
        val count = underTest.getTotalEntries()
        assertTrue(count is LaunchResult.Success)
        assertEquals(4, count.data)
        val launch1 = underTest.getLaunchDetail("launch-1")
        assertTrue(launch1 is LaunchResult.Success)
        assertEquals("Go for Launch", launch1.data?.status?.name)
        val launch3 = underTest.getLaunchDetail("launch-3")
        assertTrue(launch3 is LaunchResult.Success)
        assertEquals("Success", launch3.data?.status?.name)
    }

    @Test
    fun givenVeryLongMissionName_whenUpsertLaunchDetail_thenHandlesCorrectly() = runTest {
        val longName = "A".repeat(500)
        val testLaunch = createTestLaunch(id = "long-name", name = longName)

        val result = underTest.upsertLaunchDetail(testLaunch)

        assertTrue(result is LaunchResult.Success)
        val retrieved = underTest.getLaunchDetail("long-name")
        assertTrue(retrieved is LaunchResult.Success)
        assertEquals(longName, retrieved.data?.missionName)
    }

    private fun createTestLaunch(
        id: String,
        name: String = "Test Launch $id",
        status: Status = Status(
            id = 1,
            name = "Go for Launch",
            abbrev = "GO",
            description = "The launch is go for launch"
        )
    ): Launch {
        return Launch(
            id = id,
            url = "https://test.com/launch/$id",
            missionName = name,
            lastUpdated = "2025-12-28T15:00:00Z",
            net = "2025-12-28T15:00:00Z",
            netPrecision = null,
            status = status,
            windowEnd = "2025-12-28T16:00:00Z",
            windowStart = "2025-12-28T15:00:00Z",
            image = Image(
                id = 1,
                name = "Test Image",
                imageUrl = "https://test.com/image.jpg",
                thumbnailUrl = "https://test.com/thumb.jpg",
                credit = "Test Credit"
            ),
            infographic = null,
            probability = 90,
            weatherConcerns = null,
            failReason = null,
            launchServiceProvider = null,
            rocket = Rocket(
                id = 1,
                configuration = Configuration(
                    id = 1,
                    url = null,
                    name = "Falcon 9",
                    fullName = "Falcon 9 Block 5",
                    variant = "Block 5",
                    families = emptyList(),
                    manufacturer = null,
                    image = null,
                    wikiUrl = null,
                    description = "Test rocket",
                    alias = null,
                    totalLaunchCount = null,
                    successfulLaunches = null,
                    failedLaunches = null,
                    length = null,
                    diameter = null,
                    launchMass = null,
                    maidenFlight = null
                ),
                launcherStage = emptyList(),
                spacecraftStage = emptyList()
            ),
            mission = Mission(
                id = 1,
                name = "Test Mission",
                description = "Test description",
                type = "Test type",
                orbit = null,
                agencies = emptyList(),
                infoUrls = emptyList(),
                vidUrls = emptyList()
            ),
            pad = Pad(
                id = 1,
                url = null,
                agencies = emptyList(),
                name = "LC-39A",
                image = Image(
                    id = 1,
                    name = "Pad Image",
                    imageUrl = "https://test.com/pad.jpg",
                    thumbnailUrl = "https://test.com/pad_thumb.jpg",
                    credit = "Test Credit"
                ),
                description = null,
                country = null,
                latitude = 28.6,
                longitude = -80.6,
                mapUrl = null,
                mapImage = null,
                wikiUrl = null,
                infoUrl = null,
                totalLaunchCount = 100,
                orbitalLaunchAttemptCount = 100,
                fastestTurnaround = null,
                location = null
            ),
            webcastLive = false,
            program = emptyList(),
            orbitalLaunchAttemptCount = null,
            locationLaunchAttemptCount = null,
            padLaunchAttemptCount = null,
            agencyLaunchAttemptCount = null,
            orbitalLaunchAttemptCountYear = null,
            locationLaunchAttemptCountYear = null,
            padLaunchAttemptCountYear = null,
            agencyLaunchAttemptCountYear = null,
            updates = emptyList(),
            infoUrls = emptyList(),
            vidUrls = emptyList(),
            padTurnaround = null,
            missionPatches = emptyList()
        )
    }

    private fun createTestLaunchList(count: Int): List<Launch> {
        return (1..count).map { index ->
            createTestLaunch(
                id = "test-$index",
                name = "Test Launch $index",
                status = Status(
                    id = index,
                    name = "Status $index",
                    abbrev = "S$index",
                    description = "Description for status $index"
                )
            )
        }
    }
}
