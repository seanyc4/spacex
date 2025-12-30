package com.seancoyle.feature.launch.data.remote

object MockWebServerResponseLaunches {
    val launchesResponse = """{
      "count": 2,
      "next": null,
      "previous": null,
      "results": [
        {
          "id": "test-launch-1",
          "url": "https://lldev.thespacedevs.com/2.3.0/launches/test-launch-1/",
          "name": "Falcon 9 Block 5 | Starlink Group 15-12",
          "response_mode": "list",
          "status": {
            "id": 1,
            "name": "Go for Launch",
            "abbrev": "Go",
            "description": "Current T-0 confirmed by official or reliable sources."
          },
          "last_updated": "2025-12-05T18:39:36Z",
          "net": "2025-12-13T05:34:00Z",
          "net_precision": {
            "id": 1,
            "name": "Minute",
            "abbrev": "MIN",
            "description": "The T-0 is accurate to the minute."
          },
          "window_end": "2025-12-13T09:34:00Z",
          "window_start": "2025-12-13T05:34:00Z",
          "image": {
            "id": 1296,
            "name": "Starlink night fairing",
            "image_url": "https://example.com/path_to_mission_patch.jpg",
            "thumbnail_url": "https://example.com/path_to_thumbnail.jpg",
            "credit": "SpaceX"
          },
          "infographic": null,
          "webcast_live": false
        },
        {
          "id": "test-launch-2",
          "url": "https://lldev.thespacedevs.com/2.3.0/launches/test-launch-2/",
          "name": "Falcon Heavy | Mission 2",
          "response_mode": "list",
          "status": {
            "id": 1,
            "name": "Go for Launch",
            "abbrev": "Go",
            "description": "Current T-0 confirmed by official or reliable sources."
          },
          "last_updated": "2025-12-05T18:39:36Z",
          "net": "2025-12-14T10:00:00Z",
          "net_precision": {
            "id": 1,
            "name": "Minute",
            "abbrev": "MIN",
            "description": "The T-0 is accurate to the minute."
          },
          "window_end": "2025-12-14T14:00:00Z",
          "window_start": "2025-12-14T10:00:00Z",
          "image": {
            "id": 1297,
            "name": "Mission Patch",
            "image_url": "https://example.com/path_to_another_mission_patch.jpg",
            "thumbnail_url": "https://example.com/path_to_another_thumbnail.jpg",
            "credit": "SpaceX"
          },
          "infographic": null,
          "webcast_live": true
        }
      ]
    }"""
        .trimIndent()
}
