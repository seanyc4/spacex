package com.seancoyle.feature.launch.data.local

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
import com.seancoyle.feature.launch.domain.model.*
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
    lateinit var underTest: LaunchesLocalDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() = runTest {
        underTest.deleteAll()
    }

    private fun createTestLaunch(
        id: String,
        name: String = "Test Launch $id",
        launchStatus: LaunchStatus = LaunchStatus.SUCCESS
    ): Launch {
        return Launch(
            id = id,
            url = "https://test.com/launch/$id",
            name = name,
            responseMode = "list",
            lastUpdated = "2025-12-28T12:00:00Z",
            net = "2025-12-28T15:00:00Z",
            netPrecision = NetPrecision(
                id = 1,
                name = "Day",
                abbrev = "DAY",
                description = "Launch time is known to the day"
            ),
            windowEnd = "2025-12-28T16:00:00Z",
            windowStart = "2025-12-28T14:00:00Z",
            image = Image(
                id = 1,
                name = "Test Image",
                imageUrl = "https://test.com/image.jpg",
                thumbnailUrl = "https://test.com/thumb.jpg",
                credit = "Test Credit"
            ),
            infographic = "https://test.com/infographic.jpg",
            probability = 90,
            weatherConcerns = "None",
            failReason = null,
            launchServiceProvider = Agency(
                id = 1,
                url = "https://test.com/agency",
                name = "SpaceX",
                abbrev = "SPX",
                type = "Commercial",
                featured = true,
                country = listOf(
                    Country(
                        id = 1,
                        name = "United States",
                        alpha2Code = "US",
                        alpha3Code = "USA",
                        nationalityName = "American"
                    )
                ),
                description = "Space Exploration Technologies Corp.",
                administrator = "Elon Musk",
                foundingYear = 2002,
                launchers = "Falcon 9, Falcon Heavy",
                spacecraft = "Dragon",
                parent = null,
                image = Image(
                    id = 2,
                    name = "Agency Image",
                    imageUrl = "https://test.com/agency.jpg",
                    thumbnailUrl = "https://test.com/agency_thumb.jpg",
                    credit = "Agency Credit"
                ),
                totalLaunchCount = 100,
                consecutiveSuccessfulLaunches = 50,
                successfulLaunches = 95,
                failedLaunches = 5,
                pendingLaunches = 10,
                consecutiveSuccessfulLandings = 40,
                successfulLandings = 80,
                failedLandings = 5,
                attemptedLandings = 85,
                successfulLandingsSpacecraft = 30,
                failedLandingsSpacecraft = 2,
                attemptedLandingsSpacecraft = 32,
                successfulLandingsPayload = 25,
                failedLandingsPayload = 1,
                attemptedLandingsPayload = 26,
                infoUrl = "https://test.com/info",
                wikiUrl = "https://test.com/wiki"
            ),
            rocket = Rocket(
                id = 1,
                configuration = Configuration(
                    id = 1,
                    url = "https://test.com/rocket",
                    name = "Falcon 9",
                    fullName = "Falcon 9 Block 5",
                    variant = "Block 5",
                    families = listOf(
                        Family(id = 1, name = "Falcon")
                    )
                )
            ),
            mission = Mission(
                id = 1,
                name = "Starlink Mission",
                type = "Communications",
                description = "Deploy Starlink satellites",
                orbit = Orbit(
                    id = 1,
                    name = "Low Earth Orbit",
                    abbrev = "LEO"
                ),
                agencies = listOf()
            ),
            pad = Pad(
                id = 1,
                url = "https://test.com/pad",
                name = "LC-39A",
                location = Location(
                    id = 1,
                    url = "https://test.com/location",
                    name = "Kennedy Space Center",
                    country = Country(
                        id = 1,
                        name = "United States",
                        alpha2Code = "US",
                        alpha3Code = "USA",
                        nationalityName = "American"
                    ),
                    description = "Florida spaceport",
                    timezoneName = "America/New_York",
                    totalLaunchCount = 500
                ),
                latitude = 28.573255,
                longitude = -80.646895,
                mapUrl = "https://test.com/map",
                totalLaunchCount = 200
            ),
            webcastLive = false,
            program = listOf(
                Program(
                    id = 1,
                    url = "https://test.com/program",
                    name = "Commercial Crew Program",
                    description = "NASA's program",
                    image = Image(
                        id = 3,
                        name = "Program Image",
                        imageUrl = "https://test.com/program.jpg",
                        thumbnailUrl = "https://test.com/program_thumb.jpg",
                        credit = "NASA"
                    ),
                    startDate = "2010-01-01",
                    endDate = null,
                    agencies = listOf()
                )
            ),
            orbitalLaunchAttemptCount = 1000,
            locationLaunchAttemptCount = 500,
            padLaunchAttemptCount = 200,
            agencyLaunchAttemptCount = 100,
            orbitalLaunchAttemptCountYear = 50,
            locationLaunchAttemptCountYear = 25,
            padLaunchAttemptCountYear = 10,
            agencyLaunchAttemptCountYear = 5,
            status = launchStatus,
        )
    }

    private fun createTestLaunchList(count: Int): List<Launch> {
        return (1..count).map { index ->
            createTestLaunch(
                id = "test-$index",
                name = "Test Launch $index",
                launchStatus = when (index % 6) {
                    0 -> LaunchStatus.SUCCESS
                    1 -> LaunchStatus.FAILED
                    3 -> LaunchStatus.TBD
                    4 -> LaunchStatus.GO
                    5 -> LaunchStatus.TBC
                    else -> LaunchStatus.TBD
                },
            )
        }
    }

    @Test
    fun upsertSingleLaunch_thenGetById_returnsSuccess() = runTest {
        val testLaunch = createTestLaunch(id = "launch-1")

        val upsertResult = underTest.upsert(testLaunch)
        assertTrue(upsertResult is LaunchResult.Success)

        val getResult = underTest.getById(id = "launch-1")
        assertTrue(getResult is LaunchResult.Success)
        val retrievedLaunch = getResult.data
        assertNotNull(retrievedLaunch)
        assertEquals(testLaunch.id, retrievedLaunch.id)
        assertEquals(testLaunch.name, retrievedLaunch.name)
        assertEquals(testLaunch.status, retrievedLaunch.status)
    }

    @Test
    fun upsertSameLaunchTwice_updatesExistingRecord() = runTest {
        val originalLaunch = createTestLaunch(id = "launch-1", name = "Original Name")
        underTest.upsert(originalLaunch)

        val updatedLaunch = originalLaunch.copy(name = "Updated Name")
        val upsertResult = underTest.upsert(updatedLaunch)
        assertTrue(upsertResult is LaunchResult.Success)

        val getResult = underTest.getById(id = "launch-1")
        assertTrue(getResult is LaunchResult.Success)
        assertEquals("Updated Name", getResult.data?.name)
    }

    @Test
    fun upsertAllMultipleLaunches_insertsSuccessfully() = runTest {
        val testLaunches = createTestLaunchList(10)

        val result = underTest.upsertAll(testLaunches)

        assertTrue(result is LaunchResult.Success)

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(10, countResult.data)
    }

    @Test
    fun upsertAll_withDuplicates_updatesExisting() = runTest {
        val initialLaunches = createTestLaunchList(5)
        underTest.upsertAll(initialLaunches)

        val updatedLaunches = initialLaunches.map { it.copy(name = "Updated ${it.name}") }
        val result = underTest.upsertAll(updatedLaunches)

        assertTrue(result is LaunchResult.Success)

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(5, countResult.data)

        val firstLaunch = underTest.getById("test-1")
        assertTrue(firstLaunch is LaunchResult.Success)
        assertTrue(firstLaunch.data?.name?.startsWith("Updated") ?: false)
    }

    @Test
    fun upsertAllEmptyList_returnsSuccess() = runTest {
        val result = underTest.upsertAll(emptyList())

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun getByIdNonExistent_returnsError() = runTest {
        val result = underTest.getById(id = "non-existent-id")

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun getTotalEntries_withNoData_returnsZero() = runTest {
        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(0, result.data)
    }

    @Test
    fun getTotalEntries_afterInsertingMultiple_returnsCorrectCount() = runTest {
        val testLaunches = createTestLaunchList(25)
        underTest.upsertAll(testLaunches)

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(25, result.data)
    }

    @Test
    fun getTotalEntries_afterUpsertingDuplicates_doesNotDoubleCount() = runTest {
        val testLaunches = createTestLaunchList(5)
        underTest.upsertAll(testLaunches)
        underTest.upsertAll(testLaunches)

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(5, result.data)
    }

    @Test
    fun deleteAll_removesAllEntries() = runTest {
        val testLaunches = createTestLaunchList(20)
        underTest.upsertAll(testLaunches)

        val preDeleteCount = underTest.getTotalEntries()
        assertTrue(preDeleteCount is LaunchResult.Success)
        assertEquals(20, preDeleteCount.data)

        val deleteResult = underTest.deleteAll()
        assertTrue(deleteResult is LaunchResult.Success)

        val postDeleteCount = underTest.getTotalEntries()
        assertTrue(postDeleteCount is LaunchResult.Success)
        assertEquals(0, postDeleteCount.data)
    }

    @Test
    fun deleteAll_onEmptyDatabase_returnsSuccess() = runTest {
        val result = underTest.deleteAll()

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun deleteAll_thenGetById_returnsError() = runTest {
        val testLaunch = createTestLaunch(id = "launch-1")
        underTest.upsert(testLaunch)

        underTest.deleteAll()

        val result = underTest.getById(id = "launch-1")
        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun refreshLaunches_replacesAllData() = runTest {
        val initialLaunches = createTestLaunchList(10)
        underTest.upsertAll(initialLaunches)

        val newLaunches = createTestLaunchList(5).map {
            it.copy(id = "new-${it.id}")
        }
        underTest.refreshLaunches(newLaunches)

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(5, countResult.data)

        val oldLaunch = underTest.getById("test-1")
        assertTrue(oldLaunch is LaunchResult.Error)

        val newLaunch = underTest.getById("new-test-1")
        assertTrue(newLaunch is LaunchResult.Success)
    }

    @Test
    fun refreshLaunchesWithKeys_clearsAndInsertsData() = runTest {
        val initialLaunches = createTestLaunchList(5)
        underTest.refreshLaunchesWithKeys(
            launches = initialLaunches,
            nextPage = 2,
            prevPage = null,
            currentPage = 1,
            cachedQuery = "test",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(5, countResult.data)

        val remoteKeys = underTest.getRemoteKeys()
        assertEquals(5, remoteKeys.size)
        assertEquals(2, remoteKeys[0]?.nextKey)
        assertEquals(1, remoteKeys[0]?.currentPage)
    }

    @Test
    fun refreshLaunchesWithKeys_withDifferentPages_updatesKeys() = runTest {
        val launches = createTestLaunchList(3)

        underTest.refreshLaunchesWithKeys(
            launches = launches,
            nextPage = 3,
            prevPage = 1,
            currentPage = 2,
            cachedQuery = "starlink",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )

        val remoteKey = underTest.getRemoteKey("test-1")
        assertNotNull(remoteKey)
        assertEquals(3, remoteKey.nextKey)
        assertEquals(1, remoteKey.prevKey)
        assertEquals(2, remoteKey.currentPage)
        assertEquals("starlink", remoteKey.cachedQuery)
        assertEquals("UPCOMING", remoteKey.cachedLaunchType)
    }

    @Test
    fun appendLaunchesWithKeys_addsToExistingData() = runTest {
        val initialLaunches = createTestLaunchList(5)
        underTest.refreshLaunchesWithKeys(
            launches = initialLaunches,
            nextPage = 2,
            prevPage = null,
            currentPage = 1,
            cachedQuery = null,
            cachedLaunchType = LaunchesType.UPCOMING.name
        )

        val additionalLaunches = (6..10).map { index ->
            createTestLaunch(id = "test-$index", name = "Test Launch $index")
        }
        underTest.appendLaunchesWithKeys(
            launches = additionalLaunches,
            nextPage = 3,
            prevPage = 1,
            currentPage = 2,
            cachedQuery = null,
            cachedLaunchType = LaunchesType.UPCOMING.name
        )

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(10, countResult.data)

        val remoteKeys = underTest.getRemoteKeys()
        assertEquals(10, remoteKeys.size)
    }

    @Test
    fun appendLaunchesWithKeys_withNullPages_handlesCorrectly() = runTest {
        val launches = createTestLaunchList(3)

        underTest.appendLaunchesWithKeys(
            launches = launches,
            nextPage = null,
            prevPage = null,
            currentPage = 1,
            cachedQuery = null,
            cachedLaunchType = LaunchesType.UPCOMING.name
        )

        val remoteKey = underTest.getRemoteKey("test-1")
        assertNotNull(remoteKey)
        assertNull(remoteKey.nextKey)
        assertNull(remoteKey.prevKey)
        assertEquals(1, remoteKey.currentPage)
    }

    @Test
    fun getRemoteKeys_withNoData_returnsEmptyList() = runTest {
        val result = underTest.getRemoteKeys()

        assertTrue(result.isEmpty())
    }

    @Test
    fun getRemoteKey_forNonExistentId_returnsNull() = runTest {
        val result = underTest.getRemoteKey("non-existent")

        assertNull(result)
    }

    @Test
    fun getRemoteKeys_afterAppend_returnsAllKeys() = runTest {
        val launches = createTestLaunchList(5)
        underTest.appendLaunchesWithKeys(
            launches = launches,
            nextPage = 2,
            prevPage = null,
            currentPage = 1,
            cachedQuery = "test-query",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )

        val remoteKeys = underTest.getRemoteKeys()

        assertEquals(5, remoteKeys.size)
        remoteKeys.forEach { key ->
            assertNotNull(key)
            assertEquals(2, key.nextKey)
            assertEquals(1, key.currentPage)
            assertEquals("test-query", key.cachedQuery)
        }
    }

    @Test
    fun upsertAll_largeDataSet_handlesCorrectly() = runTest {
        val largeLaunchList = createTestLaunchList(1000)

        val upsertResult = underTest.upsertAll(largeLaunchList)
        assertTrue(upsertResult is LaunchResult.Success)

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(1000, countResult.data)
    }

    @Test
    fun refreshLaunches_largeDataSet_replacesCorrectly() = runTest {
        val initialData = createTestLaunchList(500)
        underTest.upsertAll(initialData)

        val newData = createTestLaunchList(1000).map { it.copy(id = "new-${it.id}") }
        underTest.refreshLaunches(newData)

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(1000, countResult.data)
    }

    @Test
    fun upsert_withSpecialCharactersInId_handlesCorrectly() = runTest {
        val testLaunch = createTestLaunch(id = "launch-@#$%^&*()")

        val result = underTest.upsert(testLaunch)
        assertTrue(result is LaunchResult.Success)

        val getResult = underTest.getById("launch-@#$%^&*()")
        assertTrue(getResult is LaunchResult.Success)
    }

    @Test
    fun multipleRefreshes_maintainsDataIntegrity() = runTest {
        repeat(5) { iteration ->
            val launches = createTestLaunchList(10).map {
                it.copy(id = "iteration-$iteration-${it.id}")
            }
            underTest.refreshLaunches(launches)

            val count = underTest.getTotalEntries()
            assertTrue(count is LaunchResult.Success)
            assertEquals(10, count.data)
        }
    }
}
