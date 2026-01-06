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
    fun `map should return NETWORK_CONNECTION_FAILED for IOException`() {
        val exception = IOException("Connection failed")
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_CONNECTION_FAILED, result)
    }

    @Test
    fun `map should return NETWORK_UNAUTHORIZED for 401 HttpException`() {
        val response = Response.error<Any>(401, "".toResponseBody(null))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_UNAUTHORIZED, result)
    }

    @Test
    fun `map should return NETWORK_FORBIDDEN for 403 HttpException`() {
        val response = Response.error<Any>(403, "".toResponseBody(null))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_FORBIDDEN, result)
    }

    @Test
    fun `map should return NETWORK_NOT_FOUND for 404 HttpException`() {
        val response = Response.error<Any>(404, "".toResponseBody(null))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_NOT_FOUND, result)
    }

    @Test
    fun `map should return NETWORK_TIMEOUT for 408 HttpException`() {
        val response = Response.error<Any>(408, "".toResponseBody(null))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_TIMEOUT, result)
    }

    @Test
    fun `map should return NETWORK_PAYLOAD_TOO_LARGE for 413 HttpException`() {
        val response = Response.error<Any>(413, "".toResponseBody(null))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_PAYLOAD_TOO_LARGE, result)
    }

    @Test
    fun `map should return NETWORK_INTERNAL_SERVER_ERROR for 500 HttpException`() {
        val response = Response.error<Any>(500, "".toResponseBody(null))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_INTERNAL_SERVER_ERROR, result)
    }

    @Test
    fun `map should return NETWORK_UNKNOWN_ERROR for other HttpException codes`() {
        val response = Response.error<Any>(503, "".toResponseBody(null))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_UNKNOWN_ERROR, result)
    }

    @Test(expected = CancellationException::class)
    fun `map should rethrow CancellationException`() {
        val exception = CancellationException("Cancelled")
        map(exception)
    }

    @Test
    fun `map should return NETWORK_UNKNOWN_ERROR for unknown exceptions`() {
        val exception = RuntimeException("Unknown error")
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_UNKNOWN_ERROR, result)
    }

    @Test
    fun `LaunchesDto toDomain should return empty list when results is null`() {
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
    fun `LaunchesDto toDomain should return empty list when results is empty`() {
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
    fun `LaunchesDto toDomain should map valid launches and filter out invalid ones`() {
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
    fun `LaunchDto toDomain should map all fields correctly including rocket stages and nested objects`() {
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

        val launchesDto = createLaunchesDto(results = listOf(launchDto))
        val result = launchesDto.toDomain()

        assertEquals(1, result.size)
        val launch = result[0]

        // Assert basic fields
        assertEquals("test-id", launch.id)
        assertEquals("https://example.com/launch", launch.url)
        assertEquals("Test Launch", launch.missionName)
        assertEquals("2025-12-05T18:39:36Z", launch.lastUpdated)
        assertEquals("2025-12-13T05:34:00Z", launch.net)
        assertEquals("2025-12-13T09:34:00Z", launch.windowEnd)
        assertEquals("2025-12-13T05:34:00Z", launch.windowStart)
        assertEquals("https://example.com/info.png", launch.infographic)
        assertEquals(80, launch.probability)
        assertEquals("None", launch.weatherConcerns)
        assertEquals("Failed", launch.failReason)
        assertEquals(true, launch.webcastLive)
        assertEquals(10, launch.orbitalLaunchAttemptCount)
        assertEquals(5, launch.locationLaunchAttemptCount)
        assertEquals(3, launch.padLaunchAttemptCount)
        assertEquals(20, launch.agencyLaunchAttemptCount)
        assertEquals(2, launch.orbitalLaunchAttemptCountYear)
        assertEquals(1, launch.locationLaunchAttemptCountYear)
        assertEquals(1, launch.padLaunchAttemptCountYear)
        assertEquals(4, launch.agencyLaunchAttemptCountYear)

        // Assert status
        assertNotNull(launch.status)
        assertEquals("Go for Launch", launch.status.name)

        // Assert netPrecision
        assertEquals("Minute", launch.netPrecision?.name)
        assertEquals(1, launch.netPrecision?.id)
        assertEquals("MIN", launch.netPrecision?.abbrev)
        assertEquals("The T-0 is accurate to the minute.", launch.netPrecision?.description)

        // Assert image
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

        // Assert updates, infoUrls, vidUrls
        assertEquals(updates[0].comment, launch.updates?.get(0)?.comment)
        assertEquals(infoUrls[0].title, launch.infoUrls?.get(0)?.title)
        assertEquals(vidUrls[0].title, launch.vidUrls?.get(0)?.title)

        // Assert padTurnaround and missionPatches
        assertEquals(padTurnaround, launch.padTurnaround)
        assertEquals(missionPatches[0].name, launch.missionPatches?.get(0)?.name)

        // Assert rocket and stages
        assertNotNull(launch.rocket)
        assertNotNull(launch.rocket.launcherStage)
        assertEquals(1, launch.rocket.launcherStage.size)
        assertEquals(launcherStage[0].type, launch.rocket.launcherStage[0].type)
        assertNotNull(launch.rocket.spacecraftStage)
        assertEquals(1, launch.rocket.spacecraftStage.size)
        assertEquals(spacecraftStage[0].destination, launch.rocket.spacecraftStage[0].destination)
    }

    @Test
    fun `LaunchDto toDomain should return null when id is missing`() {
        val launchDto = createLaunchDto(id = null)
        val launchesDto = createLaunchesDto(results = listOf(launchDto))

        val result = launchesDto.toDomain()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `LaunchDto toDomain should return null when name is missing`() {
        val launchDto = createLaunchDto(name = null)
        val launchesDto = createLaunchesDto(results = listOf(launchDto))

        val result = launchesDto.toDomain()

        assertTrue(result.isEmpty())
    }
}
