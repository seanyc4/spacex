package com.seancoyle.feature.launch.presentation

class MappingExtensionsKtTest {

   /* @RelaxedMockK
    lateinit var appStringResource: Lazy<AppStringResource>

    @MockK
    lateinit var stringResource: AppStringResource

    @Before
    fun setUp() {
        every { appStringResource.get() } returns stringResource
        every { stringResource.getString(any(), any()) } returns "Mocked Summary"
    }

    @Test
    fun `toUiModel maps CompanySummary correctly`() {
        val companySummary = LaunchTypes.CompanySummary(
            id = "1",
            name = "SpaceX",
            founder = "Elon Musk",
            founded = 2002,
            employees = 8000,
            launchSites = 3,
            valuation = 74_000_000_000
        )

        val result = companySummary.toUiModel(appStringResource)

        assertEquals("1", result.id)
        assertEquals("Mocked Summary", result.summary)
        assertEquals("SpaceX", result.name)
        assertEquals("Elon Musk", result.founder)
        assertEquals("2002", result.founded)
        assertEquals(8000, result.employees)
        assertEquals("3", result.launchSites)
        assertEquals(74_000_000_000, result.valuation)
    }

    @Test
    fun `toUiModel maps Launch correctly`() {
        val links = mockk<Links>(relaxed = true)
        val rocket = mockk<Rocket>(relaxed = true)
        val launch = LaunchTypes.Launch(
            id = "2",
            launchDate = "2022-12-01",
            launchYear = "2022",
            launchStatus = LaunchStatus.SUCCESS,
            links = links,
            missionName = "Falcon Heavy Test",
            rocket = rocket,
            launchDateStatus = LaunchDateStatus.PAST,
            launchDays = 100
        )

        val result = launch.toUiModel(appStringResource)

        assertEquals("2", result.id)
        assertEquals("2022-12-01", result.launchDate)
        assertEquals("2022", result.launchYear)
        assertEquals(LaunchStatus.SUCCESS, result.launchStatus)
        assertEquals("Falcon Heavy Test", result.missionName)
        assertEquals(100, result.launchDays)
        assertEquals(R.string.days_since_now, result.launchDaysResId)
        assertEquals(R.drawable.ic_launch_success, result.launchStatusIconResId)
    }

    @Test
    fun `toUiModel throws exception for unknown type`() {
        val unknownType = mockk<LaunchTypes>(relaxed = true)
        assertThrows(IllegalArgumentException::class.kotlin) {
            unknownType.toUiModel(appStringResource)
        }
    }*/
}