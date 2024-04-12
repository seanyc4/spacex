apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}
android {
    namespace = "com.seancoyle.datastore.test"
}

dependencies {
    implementation(projects.datastore.api)
}