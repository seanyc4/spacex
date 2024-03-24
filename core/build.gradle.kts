apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(Plugins.android_library)
}

android {
    namespace = Modules.core_namespace
}

dependencies {
    implementation(Square.retrofit)
    implementation(Firebase.firebase)
}