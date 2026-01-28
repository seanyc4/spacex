package com.seancoyle.database

import androidx.paging.PagingSource
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.database.dao.PastLaunchDao
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.PastLaunchEntity
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
class PastLaunchDaoTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: Database

    private lateinit var dao: PastLaunchDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.pastLaunchDao()
    }

    @After
    fun tearDown() = runTest {
        dao.deleteAll()
    }

    @Test
    fun givenEmptyDatabase_whenGetTotalEntries_thenReturnsZero() = runTest {
        val count = dao.getTotalEntries()

        assertEquals(0, count)
    }

    @Test
    fun givenLaunchEntity_whenUpsert_thenCanBeRetrieved() = runTest {
        val entity = createTestEntity(id = "past-1")

        dao.upsert(entity)
        val count = dao.getTotalEntries()

        assertEquals(1, count)
    }

    @Test
    fun givenLaunchEntity_whenUpsert_thenCanBeRetrievedById() = runTest {
        val entity = createTestEntity(id = "past-1", missionName = "Past Mission")

        dao.upsert(entity)
        val result = dao.getById("past-1")

        assertNotNull(result)
        assertEquals("Past Mission", result.missionName)
    }

    @Test
    fun givenNonExistentId_whenGetById_thenReturnsNull() = runTest {
        val result = dao.getById("non-existent")

        assertNull(result)
    }

    @Test
    fun givenMultipleLaunches_whenUpsertAll_thenAllAreStored() = runTest {
        val entities = (1..10).map { createTestEntity(id = "past-$it") }

        dao.upsertAll(entities)
        val count = dao.getTotalEntries()

        assertEquals(10, count)
    }

    @Test
    fun givenDuplicateIds_whenUpsert_thenReplacesDuplicates() = runTest {
        val original = createTestEntity(id = "past-1", missionName = "Original")
        dao.upsert(original)

        val updated = createTestEntity(id = "past-1", missionName = "Updated")
        dao.upsert(updated)

        val count = dao.getTotalEntries()
        assertEquals(1, count)

        val result = dao.getById("past-1")
        assertEquals("Updated", result?.missionName)
    }

    @Test
    fun givenLaunches_whenDeleteAll_thenDatabaseIsEmpty() = runTest {
        val entities = (1..5).map { createTestEntity(id = "past-$it") }
        dao.upsertAll(entities)

        dao.deleteAll()
        val count = dao.getTotalEntries()

        assertEquals(0, count)
    }

    @Test
    fun givenLaunches_whenPagingSourceCreated_thenReturnsPage() = runTest {
        val entities = (1..20).map { createTestEntity(id = "past-$it", missionName = "Mission $it") }
        dao.upsertAll(entities)

        val pagingSource = dao.pagingSource()
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assertTrue(loadResult is PagingSource.LoadResult.Page)
        assertEquals(10, loadResult.data.size)
    }

    @Test
    fun givenLargePastLaunchDataset_whenPaged_thenHandlesCorrectly() = runTest {
        val entities = (1..100).map { createTestEntity(id = "past-$it", missionName = "Historical Mission $it") }
        dao.upsertAll(entities)

        val count = dao.getTotalEntries()

        assertEquals(100, count)
    }

    @Test
    fun givenRefreshLaunches_whenCalled_thenReplacesAllData() = runTest {
        val initial = (1..5).map { createTestEntity(id = "initial-$it") }
        dao.upsertAll(initial)

        val newLaunches = (1..3).map { createTestEntity(id = "new-$it") }
        dao.refreshLaunches(newLaunches)

        val count = dao.getTotalEntries()
        assertEquals(3, count)

        val oldResult = dao.getById("initial-1")
        assertNull(oldResult)

        val newResult = dao.getById("new-1")
        assertNotNull(newResult)
    }

    @Test
    fun givenLaunchWithStatus_whenUpserted_thenStatusIsPreserved() = runTest {
        val status = LaunchStatusEntity(
            id = 3,
            name = "Success",
            abbrev = "Success",
            description = "Launch was successful"
        )
        val entity = createTestEntity(id = "past-1", status = status)

        dao.upsert(entity)
        val result = dao.getById("past-1")

        assertNotNull(result)
        assertEquals(3, result.status.id)
        assertEquals("Success", result.status.name)
    }

    @Test
    fun givenMultipleStatuses_whenUpserted_thenAllStatusesPreserved() = runTest {
        val successStatus = LaunchStatusEntity(id = 3, name = "Success", abbrev = "Success", description = "Successful")
        val failedStatus = LaunchStatusEntity(id = 4, name = "Failed", abbrev = "Failed", description = "Launch failed")

        val launch1 = createTestEntity(id = "past-1", status = successStatus)
        val launch2 = createTestEntity(id = "past-2", status = failedStatus)
        dao.upsertAll(listOf(launch1, launch2))

        val result1 = dao.getById("past-1")
        val result2 = dao.getById("past-2")

        assertEquals("Success", result1?.status?.name)
        assertEquals("Failed", result2?.status?.name)
    }

    private fun createTestEntity(
        id: String = "past-id",
        missionName: String = "Past Mission",
        net: String = "2024-06-15T10:00:00Z",
        status: LaunchStatusEntity = LaunchStatusEntity(
            id = 3,
            name = "Success",
            abbrev = "Success",
            description = "Launch was successful"
        ),
        location: String = "United States of America"
    ): PastLaunchEntity = PastLaunchEntity(
        id = id,
        missionName = missionName,
        net = net,
        imageUrl = "https://example.com/image.jpg",
        status = status,
        location =location
    )
}
