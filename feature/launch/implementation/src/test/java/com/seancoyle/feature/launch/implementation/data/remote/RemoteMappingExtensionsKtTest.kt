package com.seancoyle.feature.launch.implementation.data.remote

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.feature.launch.implementation.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.util.TestData.createImageDto
import com.seancoyle.feature.launch.implementation.util.TestData.createLaunchDto
import com.seancoyle.feature.launch.implementation.util.TestData.createLaunchesDto
import com.seancoyle.feature.launch.implementation.util.TestData.createNetPrecisionDto
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RemoteMappingExtensionsKtTest {

    @Test
    fun `map should return NETWORK_CONNECTION_FAILED for IOException`() {
        val exception = IOException("Connection failed")
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_CONNECTION_FAILED, result)
    }

    @Test
    fun `map should return NETWORK_UNAUTHORIZED for 401 HttpException`() {
        val response = Response.error<Any>(401, okhttp3.ResponseBody.create(null, ""))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_UNAUTHORIZED, result)
    }

    @Test
    fun `map should return NETWORK_FORBIDDEN for 403 HttpException`() {
        val response = Response.error<Any>(403, okhttp3.ResponseBody.create(null, ""))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_FORBIDDEN, result)
    }

    @Test
    fun `map should return NETWORK_NOT_FOUND for 404 HttpException`() {
        val response = Response.error<Any>(404, okhttp3.ResponseBody.create(null, ""))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_NOT_FOUND, result)
    }

    @Test
    fun `map should return NETWORK_TIMEOUT for 408 HttpException`() {
        val response = Response.error<Any>(408, okhttp3.ResponseBody.create(null, ""))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_TIMEOUT, result)
    }

    @Test
    fun `map should return NETWORK_PAYLOAD_TOO_LARGE for 413 HttpException`() {
        val response = Response.error<Any>(413, okhttp3.ResponseBody.create(null, ""))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_PAYLOAD_TOO_LARGE, result)
    }

    @Test
    fun `map should return NETWORK_INTERNAL_SERVER_ERROR for 500 HttpException`() {
        val response = Response.error<Any>(500, okhttp3.ResponseBody.create(null, ""))
        val exception = HttpException(response)
        val result = map(exception)
        assertEquals(RemoteError.NETWORK_INTERNAL_SERVER_ERROR, result)
    }

    @Test
    fun `map should return NETWORK_UNKNOWN_ERROR for other HttpException codes`() {
        val response = Response.error<Any>(503, okhttp3.ResponseBody.create(null, ""))
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
    fun `LaunchDto toDomain should map all fields correctly`() {
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
            agencyLaunchAttemptCountYear = 4
        )

        val launchesDto = createLaunchesDto(results = listOf(launchDto))
        val result = launchesDto.toDomain()

        assertEquals(1, result.size)
        val launch = result[0]

        assertEquals("test-id", launch.id)
        assertEquals("https://example.com/launch", launch.url)
        assertEquals("Test Launch", launch.name)
        assertEquals("list", launch.responseMode)
        assertEquals("2025-12-05T18:39:36Z", launch.lastUpdated)
        assertEquals("2025-12-13T05:34:00Z", launch.net)
        assertEquals("Minute", launch.netPrecision?.name)
        assertEquals("2025-12-13T09:34:00Z", launch.windowEnd)
        assertEquals("2025-12-13T05:34:00Z", launch.windowStart)
        assertEquals("Starlink night fairing", launch.image?.name)
        assertEquals("https://example.com/info.png", launch.infographic)
        assertEquals(80, launch.probability)
        assertEquals("None", launch.weatherConcerns)
        assertNull(launch.failReason)
        assertEquals(launch.webcastLive, true)
        assertEquals(10, launch.orbitalLaunchAttemptCount)
        assertEquals(5, launch.locationLaunchAttemptCount)
        assertEquals(3, launch.padLaunchAttemptCount)
        assertEquals(20, launch.agencyLaunchAttemptCount)
        assertEquals(2, launch.orbitalLaunchAttemptCountYear)
        assertEquals(1, launch.locationLaunchAttemptCountYear)
        assertEquals(1, launch.padLaunchAttemptCountYear)
        assertEquals(4, launch.agencyLaunchAttemptCountYear)

        // Verify calculated fields are null (to be populated by use case)
        assertNull(launch.launchDateLocalDateTime)
        assertNull(launch.launchYear)
        assertNull(launch.launchDateStatus)
        assertEquals(LaunchStatus.SUCCESS, launch.launchStatus)
        assertNull(launch.launchDays)
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

    @Test
    fun `LaunchDto toDomain should return null when net is missing`() {
        val launchDto = createLaunchDto(net = null)
        val launchesDto = createLaunchesDto(results = listOf(launchDto))

        val result = launchesDto.toDomain()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `NetPrecisionDto toDomain should map all fields correctly`() {
        val customNetPrecision = createNetPrecisionDto(
            id = 1,
            name = "Minute",
            abbrev = "MIN",
            description = "The T-0 is accurate to the minute."
        )

        val launchDto = createLaunchDto(netPrecision = customNetPrecision)
        val launchesDto = createLaunchesDto(results = listOf(launchDto))
        val result = launchesDto.toDomain()

        assertNotNull(result[0].netPrecision)
        assertEquals(1, result[0].netPrecision?.id)
        assertEquals("Minute", result[0].netPrecision?.name)
        assertEquals("MIN", result[0].netPrecision?.abbrev)
        assertEquals("The T-0 is accurate to the minute.", result[0].netPrecision?.description)
    }

    @Test
    fun `ImageDto toDomain should map all fields correctly`() {
        val customImage = createImageDto(
            id = 1296,
            name = "Starlink night fairing",
            imageUrl = "https://example.com/image.png",
            thumbnailUrl = "https://example.com/thumb.png",
            credit = "SpaceX"
        )

        val launchDto = createLaunchDto(image = customImage)
        val launchesDto = createLaunchesDto(results = listOf(launchDto))
        val result = launchesDto.toDomain()

        assertNotNull(result[0].image)
        assertEquals(1296, result[0].image?.id)
        assertEquals("Starlink night fairing", result[0].image?.name)
        assertEquals("https://example.com/image.png", result[0].image?.imageUrl)
        assertEquals("https://example.com/thumb.png", result[0].image?.thumbnailUrl)
        assertEquals("SpaceX", result[0].image?.credit)
    }
}
