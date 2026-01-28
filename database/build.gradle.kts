apply(from = "$rootDir/android-base.gradle")
apply(from = "$rootDir/hilt.gradle")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.seancoyle.core.database"


    defaultConfig {
        testInstrumentationRunner = libs.plugins.hiltTestRunner.get().pluginId

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    implementation(libs.kotlin.serialization)
    ksp(libs.room.compiler)

    androidTestImplementation(projects.core.test)
    androidTestImplementation(libs.bundles.unitTestBundle)
    androidTestImplementation(libs.bundles.androidTestBundle)
}