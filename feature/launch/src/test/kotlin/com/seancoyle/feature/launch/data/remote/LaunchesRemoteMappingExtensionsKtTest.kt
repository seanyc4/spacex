package com.seancoyle.feature.launch.data.remote

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.feature.launch.util.TestData
import com.seancoyle.feature.launch.util.TestData.createLaunchDto
import com.seancoyle.feature.launch.util.TestData.createLaunchesDto
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LaunchesRemoteMappingExtensionsKtTest {

    @Test
    fun `GIVEN IOException WHEN map THEN returns NETWORK_CONNECTION_FAILED`() {
        val exception = IOException("Connection failed")

        val result = map(exception)

        assertEquals(RemoteError.NETWORK_CONNECTION_FAILED, result)
    }

    @Test
    fun `GIVEN 401 HttpException WHEN map THEN returns NETWORK_UNAUTHORIZED`() {
        val response = Response.error<Any>(401, "".toResponseBody(null))
        val exception = HttpException(response)

        val result = map(exception)

        assertEquals(RemoteError.NETWORK_UNAUTHORIZED, result)
    }

    @Test
    fun `GIVEN 403 HttpException WHEN map THEN returns NETWORK_FORBIDDEN`() {
        val response = Response.error<Any>(403, "".toResponseBody(null))
        val exception = HttpException(response)

        val result = map(exception)

        assertEquals(RemoteError.NETWORK_FORBIDDEN, result)
    }

    @Test
    fun `GIVEN 404 HttpException WHEN map THEN returns NETWORK_NOT_FOUND`() {
        val response = Response.error<Any>(404, "".toResponseBody(null))
        val exception = HttpException(response)

        val result = map(exception)

        assertEquals(RemoteError.NETWORK_NOT_FOUND, result)
    }

    @Test
    fun `GIVEN 408 HttpException WHEN map THEN returns NETWORK_TIMEOUT`() {
        val response = Response.error<Any>(408, "".toResponseBody(null))
        val exception = HttpException(response)

        val result = map(exception)

        assertEquals(RemoteError.NETWORK_TIMEOUT, result)
    }

    @Test
    fun `GIVEN 413 HttpException WHEN map THEN returns NETWORK_PAYLOAD_TOO_LARGE`() {
        val response = Response.error<Any>(413, "".toResponseBody(null))
        val exception = HttpException(response)

        val result = map(exception)

        assertEquals(RemoteError.NETWORK_PAYLOAD_TOO_LARGE, result)
    }

    @Test
    fun `GIVEN 500 HttpException WHEN map THEN returns NETWORK_INTERNAL_SERVER_ERROR`() {
        val response = Response.error<Any>(500, "".toResponseBody(null))
        val exception = HttpException(response)

        val result = map(exception)

        assertEquals(RemoteError.NETWORK_INTERNAL_SERVER_ERROR, result)
    }

    @Test
    fun `GIVEN 503 HttpException WHEN map THEN returns NETWORK_UNKNOWN_ERROR`() {
        val response = Response.error<Any>(503, "".toResponseBody(null))
        val exception = HttpException(response)

        val result = map(exception)

        assertEquals(RemoteError.NETWORK_UNKNOWN_ERROR, result)
    }

    @Test(expected = CancellationException::class)
    fun `GIVEN CancellationException WHEN map THEN rethrows exception`() {
        val exception = CancellationException("Cancelled")

        map(exception)
    }

    @Test
    fun `GIVEN unknown exception WHEN map THEN returns NETWORK_UNKNOWN_ERROR`() {
        val exception = RuntimeException("Unknown error")

        val result = map(exception)

        assertEquals(RemoteError.NETWORK_UNKNOWN_ERROR, result)
    }

    @Test
    fun `GIVEN launchesDto with null results WHEN toDomain THEN returns empty list`() {
        val launchesDto = LaunchesDto(
            count = 0,
            next = null,
            previous = null,
            results = null
        )

        val result = launchesDto.toDomain()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `GIVEN launchesDto with empty results WHEN toDomain THEN returns empty list`() {
        val launchesDto = LaunchesDto(
            count = 0,
            next = null,
            previous = null,
            results = emptyList()
        )

        val result = launchesDto.toDomain()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `GIVEN launchesDto with valid and invalid launches WHEN toDomain THEN filters out invalid ones`() {
        val validLaunchDto = createLaunchDto()
        val invalidLaunchDto = createLaunchDto(id = null)

        val launchesDto = createLaunchesDto(
            count = 2,
            results = listOf(validLaunchDto, invalidLaunchDto)
        )

        val result = launchesDto.toDomain()

        assertEquals(1, result.size)
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", result[0].id)
    }

    @Test
    fun `GIVEN launchDto with all fields WHEN toDomain THEN maps all fields including rocket stages correctly`() {
        val updates = listOf(TestData.createLaunchUpdateDto())
        val infoUrls = listOf(TestData.createInfoUrlDto())
        val vidUrls = listOf(TestData.createVidUrlDto())
        val padTurnaround = "P1DT2H"
        val missionPatches = listOf(TestData.createMissionPatchDto())
        val launcherStage = listOf(TestData.createLauncherStageDto())
        val spacecraftStage = listOf(TestData.createSpacecraftStageDto())
        val customNetPrecision = TestData.createNetPrecisionDto()
        val customImage = TestData.createImageDto()
        val rocketDto = TestData.createRocketDto(
            launcherStage = launcherStage,
            spacecraftStage = spacecraftStage
        )

        val launchDto = createLaunchDto(
            id = "test-id",
            url = "https://example.com/launch",
            name = "Test Launch",
            infographic = "https://example.com/info.png",
            probability = 80,
            weatherConcerns = "None",
            webcastLive = true,
            orbitalLaunchAttemptCount = 10,
            locationLaunchAttemptCount = 5,
            padLaunchAttemptCount = 3,
            agencyLaunchAttemptCount = 20,
            orbitalLaunchAttemptCountYear = 2,
            locationLaunchAttemptCountYear = 1,
            padLaunchAttemptCountYear = 1,
            agencyLaunchAttemptCountYear = 4,
            updates = updates,
            infoUrls = infoUrls,
            vidUrls = vidUrls,
            padTurnaround = padTurnaround,
            missionPatches = missionPatches,
            netPrecision = customNetPrecision,
            image = customImage,
            rocket = rocketDto
        )

        val launch = launchDto.toDomain()!!

        assertEquals("test-id", launch.id)
        assertEquals("https://example.com/launch", launch.url)
        assertEquals("Test Launch", launch.missionName)
        assertEquals("2025-12-05T18:39:36Z", launch.lastUpdated)
        assertEquals("2025-12-13T05:34:00Z", launch.net)
        assertNotNull(launch.status)
        assertEquals("Go for Launch", launch.status.name)
        assertEquals("Starlink night fairing", launch.image.name)
        assertEquals(1296, launch.image.id)
        assertEquals(
            "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/falcon2520925_image_20221009234147.png",
            launch.image.imageUrl
        )
        assertEquals(
            "https://thespacedevs-dev.nyc3.digitaloceanspaces.com/media/images/255bauto255d__image_thumbnail_20240305192320.png",
            launch.image.thumbnailUrl
        )
        assertEquals("SpaceX", launch.image.credit)
        assertEquals(updates[0].comment, launch.updates?.get(0)?.comment)
        assertEquals(infoUrls[0].title, launch.infoUrls?.get(0)?.title)
        assertEquals(vidUrls[0].title, launch.vidUrls?.get(0)?.title)
        assertEquals(padTurnaround, launch.padTurnaround)
        assertEquals(missionPatches[0].name, launch.missionPatches?.get(0)?.name)
        assertNotNull(launch.rocket)
        assertNotNull(launch.rocket.launcherStage)
        assertEquals(1, launch.rocket.launcherStage.size)
        assertEquals(launcherStage[0].type, launch.rocket.launcherStage[0].type)
        assertNotNull(launch.rocket.spacecraftStage)
        assertEquals(1, launch.rocket.spacecraftStage.size)
        assertEquals(spacecraftStage[0].destination, launch.rocket.spacecraftStage[0].destination)
    }

    @Test
    fun `GIVEN launchDto with null id WHEN toDomain THEN returns null`() {
        val launchDto = createLaunchDto(id = null)

        val result = launchDto.toDomain()

        assertEquals(result, null)
    }

    @Test
    fun `GIVEN launchDto with null name WHEN toDomain THEN returns null`() {
        val launchDto = createLaunchDto(name = null)

        val result = launchDto.toDomain()

        assertEquals(result, null)
    }

    @Test
    fun `GIVEN launchDto with pad location country WHEN toSummary THEN maps location from country name`() {
        val countryDto = TestData.createCountryDto(name = "United States")
        val locationDto = TestData.createLocationDto(country = countryDto)
        val padDto = TestData.createPadDto(location = locationDto)
        val launchDto = createLaunchDto(pad = padDto)

        val result = launchDto.toSummary()

        assertNotNull(result)
        assertEquals("United States", result?.location)
    }

    @Test
    fun `GIVEN launchDto with null pad WHEN toSummary THEN returns null`() {
        val launchDto = createLaunchDto(pad = null)

        val result = launchDto.toSummary()

        assertEquals(null, result)
    }

    @Test
    fun `GIVEN launchDto with pad and location WHEN toDomain THEN maps all location fields correctly`() {
        val countryDto = TestData.createCountryDto(name = "United States")
        val locationDto = TestData.createLocationDto(
            id = 27,
            name = "Kennedy Space Center, FL, USA",
            country = countryDto,
            totalLaunchCount = 500
        )
        val padDto = TestData.createPadDto(location = locationDto)
        val launchDto = createLaunchDto(pad = padDto)

        val launch = launchDto.toDomain()

        assertNotNull(launch)
        assertNotNull(launch?.pad?.location)
        assertEquals(27, launch?.pad?.location?.id)
        assertEquals("Kennedy Space Center, FL, USA", launch?.pad?.location?.name)
        assertEquals("United States", launch?.pad?.location?.country?.name)
        assertEquals(500, launch?.pad?.location?.totalLaunchCount)
    }
}
