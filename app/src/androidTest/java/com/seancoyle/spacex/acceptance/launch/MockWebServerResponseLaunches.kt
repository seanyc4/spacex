package com.seancoyle.spacex.acceptance.launch

object MockWebServerResponseLaunches {
    val launchesResponse = """{
      "docs": [
        {
          "flight_number": 102,
          "date_utc": "2022-09-15T11:14:00.000Z",
          "links": {
            "patch": {
              "small": "https://example.com/path_to_mission_patch.jpg"
            },
            "article": "https://example.com/path_to_article",
            "webcast": "https://example.com/path_to_webcast",
            "wikipedia": "https://en.wikipedia.org/wiki/SpaceX_CRS-22"
          },
          "name": "CRS-22",
          "rocket": {
            "name": "Falcon 9",
            "type": "FT"
          },
          "success": true
        },
        {
          "flight_number": 103,
          "date_utc": "2022-10-04T14:22:00.000Z",
          "links": {
            "patch": {
              "small": "https://example.com/path_to_another_mission_patch.jpg"
            },
            "article": "https://spaceflightnow.com/2022/03/19/spacex-stretches-rocket-reuse-record-with-another-starlink-launch/",
            "webcast": "https://youtu.be/0giA6VZOICs",
            "wikipedia": "https://en.wikipedia.org/wiki/SpaceX_CRS-23"
          },
          "name": "CRS-23",
          "rocket": {
            "name": "Falcon Heavy",
            "type": "Block 5"
          },
          "success": false
        },
        {
          "flight_number": 104,
          "date_utc": "2022-10-04T14:22:00.000Z",
          "links": {
            "patch": {
              "small": "https://example.com/path_to_another_mission_patch.jpg"
            },
            "article": "https://spaceflightnow.com/2022/03/19/spacex-stretches-rocket-reuse-record-with-another-starlink-launch/",
            "webcast": "https://youtu.be/0giA6VZOICs",
            "wikipedia": "https://en.wikipedia.org/wiki/SpaceX_CRS-23"
          },
          "name": "CRS-23",
          "rocket": {
            "name": "Falcon Heavy",
            "type": "Block 5"
          },
          "success": false
        },
        {
          "flight_number": 105,
          "date_utc": "2022-10-04T14:22:00.000Z",
          "links": {
            "patch": {
              "small": "https://example.com/path_to_another_mission_patch.jpg"
            },
            "article": "https://spaceflightnow.com/2022/03/19/spacex-stretches-rocket-reuse-record-with-another-starlink-launch/",
            "webcast": "https://youtu.be/0giA6VZOICs",
            "wikipedia": "https://en.wikipedia.org/wiki/SpaceX_CRS-23"
          },
          "name": "CRS-23",
          "rocket": {
            "name": "Falcon Heavy",
            "type": "Block 5"
          },
          "success": false
        },
        {
          "flight_number": 106,
          "date_utc": "2022-10-04T14:22:00.000Z",
          "links": {
            "patch": {
              "small": "https://example.com/path_to_another_mission_patch.jpg"
            },
            "article": "https://spaceflightnow.com/2022/03/19/spacex-stretches-rocket-reuse-record-with-another-starlink-launch/",
            "webcast": "https://youtu.be/0giA6VZOICs",
            "wikipedia": "https://en.wikipedia.org/wiki/SpaceX_CRS-23"
          },
          "name": "CRS-23",
          "rocket": {
            "name": "Falcon Heavy",
            "type": "Block 5"
          },
          "success": false
        },{
          "flight_number": 107,
          "date_utc": "2022-10-04T14:22:00.000Z",
          "links": {
            "patch": {
              "small": "https://example.com/path_to_another_mission_patch.jpg"
            },
            "article": "https://spaceflightnow.com/2022/03/19/spacex-stretches-rocket-reuse-record-with-another-starlink-launch/",
            "webcast": "https://youtu.be/0giA6VZOICs",
            "wikipedia": "https://en.wikipedia.org/wiki/SpaceX_CRS-23"
          },
          "name": "CRS-23",
          "rocket": {
            "name": "Falcon Heavy",
            "type": "Block 5"
          },
          "success": false
        },
        {
          "flight_number": 108,
          "date_utc": "2022-10-04T14:22:00.000Z",
          "links": {
            "patch": {
              "small": "https://example.com/path_to_another_mission_patch.jpg"
            },
            "article": "https://spaceflightnow.com/2022/03/19/spacex-stretches-rocket-reuse-record-with-another-starlink-launch/",
            "webcast": "https://youtu.be/0giA6VZOICs",
            "wikipedia": "https://en.wikipedia.org/wiki/SpaceX_CRS-23"
          },
          "name": "CRS-23",
          "rocket": {
            "name": "Falcon Heavy",
            "type": "Block 5"
          },
          "success": false
        },
        {
          "flight_number": 109,
          "date_utc": "2022-10-04T14:22:00.000Z",
          "links": {
            "patch": {
              "small": "https://example.com/path_to_another_mission_patch.jpg"
            },
            "article": "https://spaceflightnow.com/2022/03/19/spacex-stretches-rocket-reuse-record-with-another-starlink-launch/",
            "webcast": "https://youtu.be/0giA6VZOICs",
            "wikipedia": "https://en.wikipedia.org/wiki/SpaceX_CRS-23"
          },
          "name": "CRS-23",
          "rocket": {
            "name": "Falcon Heavy",
            "type": "Block 5"
          },
          "success": false
        },
        {
          "flight_number": 110,
          "date_utc": "2022-10-04T14:22:00.000Z",
          "links": {
            "patch": {
              "small": "https://example.com/path_to_another_mission_patch.jpg"
            },
          },
          "name": "CRS-23",
          "rocket": {
            "name": "Falcon Heavy",
            "type": "Block 5"
          },
          "success": false
        }
      ]
    }"""
        .trimIndent()
}