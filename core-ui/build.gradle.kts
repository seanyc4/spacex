apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-ui.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = "com.seancoyle.core_ui"
}