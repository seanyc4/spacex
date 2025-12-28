package com.seancoyle.feature.launch.data.cache

private const val PAGE = 1
private const val DATA = 1000

/*
@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
internal class LaunchLocalDataSourceTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private lateinit var validLaunchYears: List<String>

    @Inject
    lateinit var underTest: LaunchLocalDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        validLaunchYears = provideValidFilterYearDates()
    }

    @After
    fun tearDown() = runTest {
        underTest.deleteAll()
    }

    private suspend fun insertTestData(num: Int = DATA): List<LaunchTypes.Launch> {
        val givenList = createLaunchListTest(num)
        underTest.insertList(givenList)
        return givenList
    }

    @Test
    fun insertLaunchGetLaunchByIdSuccess() = runTest {
        val expectedResult = createLaunchEntity(id = "1")

        underTest.insert(expectedResult)

        val result = underTest.getById(id = "1")

        assertTrue(result is LaunchResult.Success)
        assertEquals(expectedResult, result.data)}

    @Test
    fun insertLaunchesGetLaunchesSuccess() = runTest {
        val expectedResult = insertTestData()

        val result = underTest.getAll()

        assertTrue(result is LaunchResult.Success)
        assertNotNull(result.data)
        assertTrue { expectedResult.containsAll(result.data) }
    }

    @Test
    fun insertLaunchesConfirmNumLaunchesInDb() = runTest {
        val expectedResult = insertTestData()

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(expectedResult.size, result.data)}

    @Test
    fun insertLaunchDeleteLaunchConfirmDeleted() = runTest {
        val expectedResult = createLaunchEntity(id = "1")
        underTest.insert(expectedResult)

        val preDeleteResult = underTest.getById(expectedResult.id)

        assertTrue(preDeleteResult is LaunchResult.Success)
        assertEquals(expectedResult, preDeleteResult.data)
        underTest.deleteById(expectedResult.id)

        val postDeleteResult = underTest.getById(expectedResult.id)
        assertTrue(postDeleteResult is LaunchResult.Error)
        assertEquals(LocalError.CACHE_ERROR_NO_RESULTS, postDeleteResult.error)
    }

    @Test
    fun orderAllLaunchesByDateASCConfirm() = runTest {
        insertTestData()

        val result = underTest.paginate(
            launchYear = "",
            order = Order.ASC,
            launchStatus = LaunchStatus.SUCCESS,
            page = PAGE,
        )

        assertTrue(result is LaunchResult.Success)

        assertTrue(result.data.isNotEmpty())
        checkDateOrderAscending(result.data)
    }

    @Test
    fun orderAllLaunchesByDateDESCConfirm() = runTest {
        insertTestData()

        val result = underTest.paginate(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatus.SUCCESS,
            page = PAGE,
        )

        assertTrue(result is LaunchResult.Success)

        assertTrue(result.data.isNotEmpty())
        checkDateOrderDescending(result.data)
    }

    @Test
    fun filterLaunchesByYearAndDateOrderASCSuccess() = runTest {
        insertTestData()
        val launchYear = validLaunchYears.random()

        val result = underTest.paginate(
            launchYear = launchYear,
            order = Order.ASC,
            launchStatus = LaunchStatus.ALL,
            page = PAGE,
        )

        assertTrue(result is LaunchResult.Success)

        assertTrue(result.data.isNotEmpty())
        assertTrue { result.data.all { it.launchYear == launchYear } }
        checkDateOrderAscending(result.data)
    }

    @Test
    fun filterLaunchesByYearOrderDESCSuccess() = runTest {
        insertTestData()
        val launchYear = validLaunchYears.random()

        val result = underTest.paginate(
            launchYear = launchYear,
            order = Order.DESC,
            launchStatus = LaunchStatus.ALL,
            page = PAGE,
        )

        assertTrue(result is LaunchResult.Success)

        assertTrue(result.data.isNotEmpty())
        assertTrue { result.data.all { it.launchYear == launchYear } }
        checkDateOrderDescending(result.data)
    }

    @Test
    fun filterLaunchesByYearNoResultsFound() = runTest {
        insertTestData()
        val invalidYear = "1000"

        val result = underTest.paginate(
            launchYear = invalidYear,
            order = Order.DESC,
            launchStatus = LaunchStatus.ALL,
            page = PAGE,
        )

        assertTrue(result is LaunchResult.Success)

        assertTrue(result.data.isEmpty())
    }

    @Test
    fun filterLaunchesByLaunchStatusSuccess() = runTest {
        insertTestData()

        val result = underTest.paginate(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatus.SUCCESS,
            page = PAGE,
        )

        assertTrue(result is LaunchResult.Success)

        assertTrue(result.data.isNotEmpty())
        checkDateOrderDescending(result.data)
        assertTrue { result.data.all { it.launchStatus == LaunchStatus.SUCCESS } }
    }

    @Test
    fun filterLaunchesByLaunchStatusFailed() = runTest {
        insertTestData()

        val result = underTest.paginate(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatus.FAILED,
            page = PAGE,
        )

        assertTrue(result is LaunchResult.Success)

        assertTrue(result.data.isNotEmpty())
        checkDateOrderDescending(result.data)
        assertTrue { result.data.all { it.launchStatus == LaunchStatus.FAILED } }
    }

    @Test
    fun filterLaunchesByLaunchStatusAll() = runTest {
        val expectedResult = insertTestData()

        val result = underTest.paginate(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatus.ALL,
            page = PAGE,
        )

        assertTrue(result is LaunchResult.Success)

        assertTrue(result.data.isNotEmpty())
        assertTrue { expectedResult.containsAll(result.data) }
        checkDateOrderDescending(result.data)
    }

    @Test
    fun filterLaunchesByLaunchStatusUnknown() = runTest {
        insertTestData()

        val result = underTest.paginate(
            launchYear = "",
            order = Order.DESC,
            launchStatus = LaunchStatus.UNKNOWN,
            page = PAGE,
        )

        assertTrue(result is LaunchResult.Success)

        assertTrue(result.data.isNotEmpty())
        checkDateOrderDescending(result.data)
        assertTrue { result.data.all { it.launchStatus == LaunchStatus.UNKNOWN } }
    }

    @Test
    fun filterLaunchesInvalidYearByLaunchStatusNoResultsFound() = runTest {
        insertTestData()
        val year = "0000"
        val result = underTest.paginate(
            launchYear = year,
            order = Order.DESC,
            launchStatus = LaunchStatus.SUCCESS,
            page = PAGE,
        )

        assertTrue(result is LaunchResult.Success)

        assertTrue { result.data.isEmpty() }
    }

    private fun checkDateOrderAscending(launchList: List<LaunchTypes.Launch>) {
        launchList.zipWithNext().forEach { (current, next) ->
            val currentDate = current.launchDateLocalDateTime
            val nextDate = next.launchDateLocalDateTime
            if (currentDate != null && nextDate != null) {
                assertTrue(
                    currentDate <= nextDate,
                    "Dates are not in ascending order"
                )
            }
        }
    }

    private fun checkDateOrderDescending(launches: List<LaunchTypes.Launch>) {
        launches.zipWithNext().forEach { (current, next) ->
            val currentDate = current.launchDateLocalDateTime
            val nextDate = next.launchDateLocalDateTime
            if (currentDate != null && nextDate != null) {
                assertTrue(
                    currentDate >= nextDate,
                    "Dates are not in descending order"
                )
            }
        }
    }

    private fun createLaunchListTest(
        num: Int,
        id: String = UUID.randomUUID().toString()
    ): List<LaunchTypes.Launch> {
        return List(num) {
            createLaunchEntity(id = id)
        }
    }

    private fun createLaunchEntity(id: String): LaunchTypes.Launch {
        val randomLaunchState = Random.nextInt(0, 4)
        val randomYear = validLaunchYears.random()
        val netDate = "$randomYear-${Random.nextInt(1, 13).toString().padStart(2, '0')}-${Random.nextInt(1, 29).toString().padStart(2, '0')}T${Random.nextInt(0, 24).toString().padStart(2, '0')}:${Random.nextInt(0, 60).toString().padStart(2, '0')}:00Z"

        return LaunchTypes.Launch(
            id = id,
            url = "https://lldev.thespacedevs.com/2.3.0/launches/$id/",
            name = "Falcon 9 Block 5 | ${UUID.randomUUID().toString().take(8)}",
            responseMode = "list",
            lastUpdated = "2025-12-13T05:34:00Z",
            net = netDate,
            netPrecision = NetPrecision(
                id = 1,
                name = "Minute",
                abbrev = "MIN",
                description = "The T-0 is accurate to the minute."
            ),
            windowEnd = netDate,
            windowStart = netDate,
            image = Image(
                id = Random.nextInt(1, 10000),
                name = "Mission Image",
                imageUrl = DEFAULT_LAUNCH_IMAGE,
                thumbnailUrl = DEFAULT_LAUNCH_IMAGE,
                credit = "SpaceX"
            ),
            infographic = null,
            probability = if (Random.nextBoolean()) Random.nextInt(0, 100) else null,
            weatherConcerns = null,
            failReason = null,
            launchServiceProvider = null,
            rocket = null,
            mission = null,
            pad = null,
            webcastLive = Random.nextBoolean(),
            program = null,
            orbitalLaunchAttemptCount = null,
            locationLaunchAttemptCount = null,
            padLaunchAttemptCount = null,
            agencyLaunchAttemptCount = null,
            orbitalLaunchAttemptCountYear = null,
            locationLaunchAttemptCountYear = null,
            padLaunchAttemptCountYear = null,
            agencyLaunchAttemptCountYear = null,
            launchDate = netDate,
            launchDateLocalDateTime = LocalDateTime.of(2025, 12, 13, 5, 34),
            launchYear = randomYear,
            launchDateStatus = if (Random.nextBoolean()) LaunchDateStatus.PAST else LaunchDateStatus.FUTURE,
            launchStatus = when (randomLaunchState) {
                0 -> LaunchStatus.SUCCESS
                1 -> LaunchStatus.FAILED
                2 -> LaunchStatus.UNKNOWN
                else -> LaunchStatus.ALL
            },
            launchDays = "${Random.nextInt(-1000, 1000)}d"
        )
    }

    private fun provideValidFilterYearDates() = listOf(
        "2025", "2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013",
        "2012", "2011", "2010", "2009", "2008", "2007", "2006"
    ).shuffled()

}
*/
