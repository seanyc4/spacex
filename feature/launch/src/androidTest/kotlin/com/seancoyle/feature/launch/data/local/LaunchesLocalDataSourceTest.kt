package com.seancoyle.feature.launch.data.local

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
import com.seancoyle.feature.launch.domain.model.LaunchSummary
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
    lateinit var underTest: LaunchesLocalDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() = runTest {
        underTest.deleteAll()
    }

    private fun createTestLaunchSummary(
        id: String,
        name: String = "Test Launch $id",
        status: Status = Status(
            id = 1,
            name = "Go for Launch",
            abbrev = "GO",
            description = "The launch is go for launch"
        )
    ): LaunchSummary {
        return LaunchSummary(
            id = id,
            missionName = name,
            net = "2025-12-28T15:00:00Z",
            imageUrl = "https://test.com/image.jpg",
            status = status
        )
    }

    private fun createTestLaunchSummaryList(count: Int): List<LaunchSummary> {
        return (1..count).map { index ->
            createTestLaunchSummary(
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

    @Test
    fun upsertSingleLaunch_thenGetById_returnsSuccess() = runTest {
        val testLaunch = createTestLaunchSummary(id = "launch-1")

        val upsertResult = underTest.upsert(testLaunch)
        assertTrue(upsertResult is LaunchResult.Success)

        val getResult = underTest.getById(id = "launch-1")
        assertTrue(getResult is LaunchResult.Success)
        val retrievedLaunch = getResult.data
        assertNotNull(retrievedLaunch)
        assertEquals(testLaunch.id, retrievedLaunch.id)
        assertEquals(testLaunch.missionName, retrievedLaunch.missionName)
        assertEquals(testLaunch.net, retrievedLaunch.net)
        assertEquals(testLaunch.imageUrl, retrievedLaunch.imageUrl)
        assertEquals(testLaunch.status.name, retrievedLaunch.status.name)
        assertEquals(testLaunch.status.abbrev, retrievedLaunch.status.abbrev)
    }

    @Test
    fun upsertSameLaunchTwice_updatesExistingRecord() = runTest {
        val originalLaunch = createTestLaunchSummary(id = "launch-1", name = "Original Name")
        underTest.upsert(originalLaunch)

        val updatedLaunch = originalLaunch.copy(missionName = "Updated Name")
        val upsertResult = underTest.upsert(updatedLaunch)
        assertTrue(upsertResult is LaunchResult.Success)

        val getResult = underTest.getById(id = "launch-1")
        assertTrue(getResult is LaunchResult.Success)
        assertEquals("Updated Name", getResult.data?.missionName)
    }

    @Test
    fun upsertAllMultipleLaunches_insertsSuccessfully() = runTest {
        val testLaunches = createTestLaunchSummaryList(10)

        val result = underTest.upsertAll(testLaunches)

        assertTrue(result is LaunchResult.Success)

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(10, countResult.data)
    }

    @Test
    fun upsertAll_withDuplicates_updatesExisting() = runTest {
        val initialLaunches = createTestLaunchSummaryList(5)
        underTest.upsertAll(initialLaunches)

        val updatedLaunches = initialLaunches.map { it.copy(missionName = "Updated ${it.missionName}") }
        val result = underTest.upsertAll(updatedLaunches)

        assertTrue(result is LaunchResult.Success)

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(5, countResult.data)

        val firstLaunch = underTest.getById("test-1")
        assertTrue(firstLaunch is LaunchResult.Success)
        assertTrue(firstLaunch.data?.missionName?.startsWith("Updated") ?: false)
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
        val testLaunches = createTestLaunchSummaryList(25)
        underTest.upsertAll(testLaunches)

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(25, result.data)
    }

    @Test
    fun getTotalEntries_afterUpsertingDuplicates_doesNotDoubleCount() = runTest {
        val testLaunches = createTestLaunchSummaryList(5)
        underTest.upsertAll(testLaunches)
        underTest.upsertAll(testLaunches)

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(5, result.data)
    }

    @Test
    fun deleteAll_removesAllEntries() = runTest {
        val testLaunches = createTestLaunchSummaryList(20)
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
        val testLaunch = createTestLaunchSummary(id = "launch-1")
        underTest.upsert(testLaunch)

        underTest.deleteAll()

        val result = underTest.getById(id = "launch-1")
        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun refreshLaunches_replacesAllData() = runTest {
        val initialLaunches = createTestLaunchSummaryList(10)
        underTest.upsertAll(initialLaunches)

        val newLaunches = createTestLaunchSummaryList(5).map {
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
        val initialLaunches = createTestLaunchSummaryList(5)
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
        val launches = createTestLaunchSummaryList(3)

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
        val initialLaunches = createTestLaunchSummaryList(5)
        underTest.refreshLaunchesWithKeys(
            launches = initialLaunches,
            nextPage = 2,
            prevPage = null,
            currentPage = 1,
            cachedQuery = null,
            cachedLaunchType = LaunchesType.UPCOMING.name
        )

        val additionalLaunches = (6..10).map { index ->
            createTestLaunchSummary(id = "test-$index", name = "Test Launch $index")
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
        val launches = createTestLaunchSummaryList(3)

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
        val launches = createTestLaunchSummaryList(5)
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
        val largeLaunchList = createTestLaunchSummaryList(1000)

        val upsertResult = underTest.upsertAll(largeLaunchList)
        assertTrue(upsertResult is LaunchResult.Success)

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(1000, countResult.data)
    }

    @Test
    fun refreshLaunches_largeDataSet_replacesCorrectly() = runTest {
        val initialData = createTestLaunchSummaryList(500)
        underTest.upsertAll(initialData)

        val newData = createTestLaunchSummaryList(1000).map { it.copy(id = "new-${it.id}") }
        underTest.refreshLaunches(newData)

        val countResult = underTest.getTotalEntries()
        assertTrue(countResult is LaunchResult.Success)
        assertEquals(1000, countResult.data)
    }

    @Test
    fun upsert_withSpecialCharactersInId_handlesCorrectly() = runTest {
        val testLaunch = createTestLaunchSummary(id = "launch-@#$%^&*()")

        val result = underTest.upsert(testLaunch)
        assertTrue(result is LaunchResult.Success)

        val getResult = underTest.getById("launch-@#$%^&*()")
        assertTrue(getResult is LaunchResult.Success)
    }

    @Test
    fun multipleRefreshes_maintainsDataIntegrity() = runTest {
        repeat(5) { iteration ->
            val launches = createTestLaunchSummaryList(10).map {
                it.copy(id = "iteration-$iteration-${it.id}")
            }
            underTest.refreshLaunches(launches)

            val count = underTest.getTotalEntries()
            assertTrue(count is LaunchResult.Success)
            assertEquals(10, count.data)
        }
    }

    @Test
    fun upsertLaunchesWithDifferentStatuses_handlesCorrectly() = runTest {
        val launches = listOf(
            createTestLaunchSummary(
                id = "launch-1",
                status = Status(1, "Go for Launch", "Go", "Launch is confirmed")
            ),
            createTestLaunchSummary(
                id = "launch-2",
                status = Status(2, "TBD", "TBD", "To be determined")
            ),
            createTestLaunchSummary(
                id = "launch-3",
                status = Status(3, "Success", "Success", "Launch was successful")
            ),
            createTestLaunchSummary(
                id = "launch-4",
                status = Status(4, "Failure", "Failed", "Launch failed")
            )
        )

        val result = underTest.upsertAll(launches)
        assertTrue(result is LaunchResult.Success)

        val count = underTest.getTotalEntries()
        assertTrue(count is LaunchResult.Success)
        assertEquals(4, count.data)

        // Verify each status was preserved
        val launch1 = underTest.getById("launch-1")
        assertTrue(launch1 is LaunchResult.Success)
        assertEquals("Go for Launch", launch1.data?.status?.name)

        val launch3 = underTest.getById("launch-3")
        assertTrue(launch3 is LaunchResult.Success)
        assertEquals("Success", launch3.data?.status?.name)
    }

    @Test
    fun refreshLaunchesWithKeys_withEmptyQuery_handlesCorrectly() = runTest {
        val launches = createTestLaunchSummaryList(3)

        underTest.refreshLaunchesWithKeys(
            launches = launches,
            nextPage = 2,
            prevPage = null,
            currentPage = 1,
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )

        val remoteKey = underTest.getRemoteKey("test-1")
        assertNotNull(remoteKey)
        assertEquals("", remoteKey.cachedQuery)
    }

    @Test
    fun appendAndRefresh_maintainsSeparateOperations() = runTest {
        // First append some data
        val initialLaunches = createTestLaunchSummaryList(5)
        underTest.appendLaunchesWithKeys(
            launches = initialLaunches,
            nextPage = 2,
            prevPage = null,
            currentPage = 1,
            cachedQuery = null,
            cachedLaunchType = LaunchesType.UPCOMING.name
        )

        val countAfterAppend = underTest.getTotalEntries()
        assertTrue(countAfterAppend is LaunchResult.Success)
        assertEquals(5, countAfterAppend.data)

        // Then refresh with different data
        val refreshLaunches = createTestLaunchSummaryList(3).map {
            it.copy(id = "refresh-${it.id}")
        }
        underTest.refreshLaunchesWithKeys(
            launches = refreshLaunches,
            nextPage = 2,
            prevPage = null,
            currentPage = 1,
            cachedQuery = null,
            cachedLaunchType = LaunchesType.UPCOMING.name
        )

        // Should only have the refreshed data
        val countAfterRefresh = underTest.getTotalEntries()
        assertTrue(countAfterRefresh is LaunchResult.Success)
        assertEquals(3, countAfterRefresh.data)

        // Old data should be gone
        val oldLaunch = underTest.getById("test-1")
        assertTrue(oldLaunch is LaunchResult.Error)

        // New data should exist
        val newLaunch = underTest.getById("refresh-test-1")
        assertTrue(newLaunch is LaunchResult.Success)
    }

    @Test
    fun upsertLaunchWithVeryLongMissionName_handlesCorrectly() = runTest {
        val longName = "A".repeat(500)
        val testLaunch = createTestLaunchSummary(id = "long-name", name = longName)

        val result = underTest.upsert(testLaunch)
        assertTrue(result is LaunchResult.Success)

        val retrieved = underTest.getById("long-name")
        assertTrue(retrieved is LaunchResult.Success)
        assertEquals(longName, retrieved.data?.missionName)
    }
}
