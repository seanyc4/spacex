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
          "response_mode": "detailed",
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
          "webcast_live": false,
          "launch_service_provider": {
            "id": 121,
            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
            "name": "SpaceX",
            "abbrev": "SpX",
            "type": {
              "id": 3,
              "name": "Commercial"
            },
            "country_code": "USA",
            "description": "Space Exploration Technologies Corp."
          },
          "rocket": {
            "id": 2804,
            "configuration": {
              "id": 164,
              "url": "https://lldev.thespacedevs.com/2.3.0/config/launcher/164/",
              "name": "Falcon 9",
              "full_name": "Falcon 9 Block 5",
              "family": "Falcon",
              "variant": "Block 5"
            }
          },
          "mission": {
            "id": 6428,
            "name": "Starlink Group 15-12",
            "description": "A batch of satellites for the Starlink mega-constellation.",
            "type": "Communications",
            "orbit": {
              "id": 8,
              "name": "Low Earth Orbit",
              "abbrev": "LEO"
            }
          },
          "pad": {
            "id": 80,
            "url": "https://lldev.thespacedevs.com/2.3.0/pad/80/",
            "name": "Space Launch Complex 40",
            "latitude": "28.56194122",
            "longitude": "-80.57735736",
            "location": {
              "id": 12,
              "name": "Cape Canaveral, FL, USA",
              "country_code": "USA",
              "country": {
                "id": 1,
                "name": "United States",
                "alpha_2_code": "US",
                "alpha_3_code": "USA",
                "nationality_name": "American"
              }
            },
            "total_launch_count": 250,
            "orbital_launch_attempt_count": 250
          }
        },
        {
          "id": "test-launch-2",
          "url": "https://lldev.thespacedevs.com/2.3.0/launches/test-launch-2/",
          "name": "Falcon Heavy | Mission 2",
          "response_mode": "detailed",
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
          "webcast_live": true,
          "launch_service_provider": {
            "id": 121,
            "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
            "name": "SpaceX",
            "abbrev": "SpX",
            "type": {
              "id": 3,
              "name": "Commercial"
            },
            "country_code": "USA",
            "description": "Space Exploration Technologies Corp."
          },
          "rocket": {
            "id": 2805,
            "configuration": {
              "id": 165,
              "url": "https://lldev.thespacedevs.com/2.3.0/config/launcher/165/",
              "name": "Falcon Heavy",
              "full_name": "Falcon Heavy",
              "family": "Falcon",
              "variant": "Heavy"
            }
          },
          "mission": {
            "id": 6429,
            "name": "Mission 2",
            "description": "A heavy-lift mission.",
            "type": "Communications",
            "orbit": {
              "id": 8,
              "name": "Low Earth Orbit",
              "abbrev": "LEO"
            }
          },
          "pad": {
            "id": 81,
            "url": "https://lldev.thespacedevs.com/2.3.0/pad/81/",
            "name": "Launch Complex 39A",
            "latitude": "28.6",
            "longitude": "-80.6",
            "location": {
              "id": 12,
              "name": "Kennedy Space Center, FL, USA",
              "country_code": "USA",
              "country": {
                "id": 1,
                "name": "United States",
                "alpha_2_code": "US",
                "alpha_3_code": "USA",
                "nationality_name": "American"
              }
            },
            "total_launch_count": 150,
            "orbital_launch_attempt_count": 150
          }
        }
      ]
    }""".trimIndent()

    // Empty results list
    val emptyLaunchesResponse = """{
      "count": 0,
      "next": null,
      "previous": null,
      "results": []
    }""".trimIndent()

    // Null results
    val nullResultsResponse = """{
      "count": 0,
      "next": null,
      "previous": null,
      "results": null
    }""".trimIndent()

    // Response with partial/invalid data (missing required fields)
    val partialDataResponse = """{
      "count": 2,
      "next": null,
      "previous": null,
      "results": [
        {
          "id": "test-launch-1",
          "name": "Valid Launch",
          "net": "2025-12-13T05:34:00Z",
          "image": {
            "id": 1,
            "name": "Test Image",
            "image_url": "https://example.com/image.jpg",
            "thumbnail_url": "https://example.com/thumb.jpg",
            "credit": "SpaceX"
          },
          "status": {
            "id": 1,
            "name": "Go for Launch",
            "abbrev": "Go",
            "description": "Ready"
          },
          "pad": {
            "id": 80,
            "location": {
              "id": 12,
              "name": "Cape Canaveral, FL, USA",
              "country": {
                "id": 1,
                "name": "United States",
                "alpha_2_code": "US",
                "alpha_3_code": "USA",
                "nationality_name": "American"
              }
            }
          }
        },
        {
          "id": null,
          "name": "Invalid Launch - Missing ID",
          "net": "2025-12-14T10:00:00Z",
          "image": null,
          "status": null
        }
      ]
    }""".trimIndent()

    // Malformed JSON
    val malformedJsonResponse = "{invalid json"

    // Response with missing image (should use default)
    val responseWithoutImage = """{
      "count": 1,
      "next": null,
      "previous": null,
      "results": [
        {
          "id": "test-launch-1",
          "name": "Test Launch",
          "net": "2025-12-13T05:34:00Z",
          "image": null,
          "status": {
            "id": 1,
            "name": "Go for Launch",
            "abbrev": "Go",
            "description": "Ready"
          },
          "pad": {
            "id": 80,
            "location": {
              "id": 12,
              "name": "Cape Canaveral, FL, USA",
              "country": {
                "id": 1,
                "name": "United States",
                "alpha_2_code": "US",
                "alpha_3_code": "USA",
                "nationality_name": "American"
              }
            }
          }
        }
      ]
    }""".trimIndent()

    // Response with pagination (next page available)
    val responseWithNextPage = """{
      "count": 100,
      "next": "https://api.example.com/launches?page=2",
      "previous": null,
      "results": [
        {
          "id": "launch-1",
          "name": "Launch 1",
          "net": "2025-12-13T05:34:00Z",
          "image": {
            "id": 1,
            "name": "Image",
            "image_url": "https://example.com/image.jpg",
            "thumbnail_url": "https://example.com/thumb.jpg",
            "credit": "Credit"
          },
          "status": {
            "id": 1,
            "name": "Go for Launch",
            "abbrev": "Go",
            "description": "Ready"
          },
          "pad": {
            "id": 80,
            "location": {
              "id": 12,
              "name": "Cape Canaveral, FL, USA",
              "country": {
                "id": 1,
                "name": "United States",
                "alpha_2_code": "US",
                "alpha_3_code": "USA",
                "nationality_name": "American"
              }
            }
          }
        }
      ]
    }""".trimIndent()

    // Single launch response for getLaunch endpoint
    val singleLaunchResponse = """{
      "id": "test-launch-1",
      "url": "https://lldev.thespacedevs.com/2.3.0/launches/test-launch-1/",
      "name": "Falcon 9 Block 5 | Starlink Group 15-12",
      "status": {
        "id": 1,
        "name": "Go for Launch",
        "abbrev": "Go",
        "description": "Current T-0 confirmed by official or reliable sources."
      },
      "last_updated": "2025-12-05T18:39:36Z",
      "net": "2025-12-13T05:34:00Z",
      "window_end": "2025-12-13T09:34:00Z",
      "window_start": "2025-12-13T05:34:00Z",
      "image": {
        "id": 1296,
        "name": "Starlink night fairing",
        "image_url": "https://example.com/path_to_mission_patch.jpg",
        "thumbnail_url": "https://example.com/path_to_thumbnail.jpg",
        "credit": "SpaceX"
      },
      "launch_service_provider": {
        "id": 121,
        "url": "https://lldev.thespacedevs.com/2.3.0/agencies/121/",
        "name": "SpaceX",
        "abbrev": "SpX",
        "type": {
          "id": 3,
          "name": "Commercial"
        },
        "country_code": "USA",
        "description": "Space Exploration Technologies Corp."
      },
      "rocket": {
        "id": 2804,
        "configuration": {
          "id": 164,
          "url": "https://lldev.thespacedevs.com/2.3.0/config/launcher/164/",
          "name": "Falcon 9",
          "full_name": "Falcon 9 Block 5",
          "family": "Falcon",
          "variant": "Block 5"
        }
      },
      "mission": {
        "id": 6428,
        "name": "Starlink Group 15-12",
        "description": "A batch of satellites for the Starlink mega-constellation.",
        "type": "Communications",
        "orbit": {
          "id": 8,
          "name": "Low Earth Orbit",
          "abbrev": "LEO"
        }
      },
      "pad": {
        "id": 80,
        "url": "https://lldev.thespacedevs.com/2.3.0/pad/80/",
        "name": "Space Launch Complex 40",
        "latitude": "28.56194122",
        "longitude": "-80.57735736",
        "location": {
          "id": 12,
          "name": "Cape Canaveral, FL, USA",
          "country_code": "USA",
          "country": {
            "id": 1,
            "name": "United States",
            "alpha_2_code": "US",
            "alpha_3_code": "USA",
            "nationality_name": "American"
          }
        },
        "total_launch_count": 250,
        "orbital_launch_attempt_count": 250
      }
    }""".trimIndent()
}
