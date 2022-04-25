apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}

dependencies {
    "implementation"(project(Modules.movieDomain))
  /*  "implementation"(Room.room_compiler)
    "implementation"(Room.room_ktx)
    "implementation"(Room.room_migration)
    "implementation"(Room.room_paging3)
    "implementation"(Room.room_runtime)*/
}