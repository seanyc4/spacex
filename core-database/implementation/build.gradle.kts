apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlinKsp.get().pluginId)
}

android {
    namespace = Modules.core_database_impl_namespace

    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreDatabase.api)
    implementation(projects.launch.api)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
}