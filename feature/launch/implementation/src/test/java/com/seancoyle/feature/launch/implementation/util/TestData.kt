package com.seancoyle.feature.launch.implementation.util

import com.seancoyle.database.entities.CompanyEntity
import com.seancoyle.database.entities.LaunchDateStatusEntity
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.LinksEntity
import com.seancoyle.database.entities.RocketEntity
import com.seancoyle.feature.launch.api.LaunchConstants.LAUNCH_OPTIONS_ROCKET
import com.seancoyle.feature.launch.api.LaunchConstants.LAUNCH_OPTIONS_SORT
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.api.domain.model.Rocket
import com.seancoyle.feature.launch.api.domain.model.RocketWithMission
import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDto
import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchDto
import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchesDto
import com.seancoyle.feature.launch.implementation.data.network.launch.LinksDto
import com.seancoyle.feature.launch.implementation.data.network.launch.PatchDto
import com.seancoyle.feature.launch.implementation.data.network.launch.RocketDto
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.model.Options
import com.seancoyle.feature.launch.implementation.domain.model.Populate
import com.seancoyle.feature.launch.implementation.domain.model.Select
import com.seancoyle.feature.launch.implementation.domain.model.Sort
import java.time.LocalDateTime

internal object TestData {

    val companyModel = Company(
        employees = 100,
        founded = 2000,
        founder = "Elon Musk",
        launchSites = 4,
        name = "SpaceX",
        valuation = 74000000000L
    )

    val companyDto = CompanyDto(
        employees = 100,
        founded = 2000,
        founder = "Elon Musk",
        launchSites = 4,
        name = "SpaceX",
        valuation = 74000000000L
    )

    val companyEntity = CompanyEntity(
        id = "1",
        employees = 100,
        founded = 2000,
        founder = "founder",
        launchSites = 4,
        name = "name",
        valuation = 74000000000L
    )

    val launchModel = LaunchTypes.Launch(
        id = "5",
        launchDate = "2024-01-01",
        launchDateLocalDateTime = LocalDateTime.of(2024, 1, 1, 0, 0),
        launchYear = "2024",
        launchStatus = LaunchStatus.SUCCESS,
        links = Links(
            missionImage = "https://example.com/mission3.jpg",
            articleLink = "https://example.com/article3",
            webcastLink = "https://example.com/webcast3",
            wikiLink = "https://example.com/wiki3"
        ),
        missionName = "Starlink Mission",
        rocket = Rocket("Falcon 9 Block 5"),
        launchDateStatus = LaunchDateStatus.FUTURE,
        launchDays = "5 days",
        isLaunchSuccess = true
    )

    val launchesModel = listOf(
        launchModel,
        launchModel
    )

    val launchEntity = LaunchEntity(
        id = "5",
        launchDate = "2024-01-01",
        launchDateLocalDateTime = LocalDateTime.of(2024, 1, 1, 0, 0),
        launchYear = "2024",
        launchStatus = LaunchStatusEntity.SUCCESS,
        links = LinksEntity(
            missionImage = "https://example.com/mission3.jpg",
            articleLink = "https://example.com/article3",
            webcastLink = "https://example.com/webcast3",
            wikiLink = "https://example.com/wiki3"
        ),
        missionName = "Starlink Mission",
        rocket = RocketEntity("Falcon 9 Block 5"),
        launchDateStatus = LaunchDateStatusEntity.FUTURE,
        launchDays = "5 days",
        isLaunchSuccess = true
    )

    val launchesEntity = listOf(
        launchEntity,
        launchEntity
    )

    val launchDto = LaunchDto(
        flightNumber = 136,
        launchDate = "2024-01-01T00:00:00Z",
        links = LinksDto(
            patch = PatchDto(missionImage = "https://example.com/images/missions/patch_small_136.png"),
            articleLink = null,
            webcastLink = "https://youtube.com/watch?v=xyz9876",
            wikiLink = "https://en.wikipedia.org/wiki/SpaceX_Starlink_Mission"
        ),
        missionName = "Starlink Mission",
        rocket = RocketDto(
            name = "Falcon Heavy",
            type = "Block 5"
        ),
        isLaunchSuccess = true
    )

    val launchesDto = LaunchesDto(
        listOf(launchDto)
    )

    val launchOptions = LaunchOptions(
        options = Options(
            populate = listOf(
                Populate(
                    path = LAUNCH_OPTIONS_ROCKET,
                    select = Select(
                        name = 1,
                        type = 2
                    )
                )
            ),
            sort = Sort(
                flightNumber = LAUNCH_OPTIONS_SORT,
            ),
            limit = 500
        )
    )

    val carouselModel = LaunchTypes.Carousel(
        id = "1",
        items = listOf(
            RocketWithMission(
                links = Links(
                    missionImage = "https://example.com/mission3.jpg",
                    articleLink = "https://example.com/article3",
                    webcastLink = "https://example.com/webcast3",
                    wikiLink = "https://example.com/wiki3"
                ),
                rocket = Rocket("Falcon 9 Block 5")
            ),
        )
    )

    val gridModel = LaunchTypes.Grid(
        id = "1",
        RocketWithMission(
            links = Links(
                missionImage = "https://example.com/mission3.jpg",
                articleLink = "https://example.com/article3",
                webcastLink = "https://example.com/webcast3",
                wikiLink = "https://example.com/wiki3"
            ),
            rocket = Rocket("Falcon 9 Block 5")
        ),
    )
}