apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.seancoyle.core.datastore_proto"

    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

androidComponents {
    onVariants { variant ->
        val buildDir = project.layout.buildDirectory.get().asFile
        android.sourceSets[variant.name].java.srcDir(File(buildDir, "generated/source/proto/${variant.name}/java"))
        android.sourceSets[variant.name].kotlin.srcDir(File(buildDir, "generated/source/proto/${variant.name}/kotlin"))
    }
}

dependencies {
    api(libs.protobuf.kotlin.lite)
    api(libs.dataStore)
}