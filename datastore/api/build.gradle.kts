apply {
    from("$rootDir/android-base.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = "com.seancoyle.datastore.api"
}

dependencies {

}