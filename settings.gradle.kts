rootProject.name = "TaipeiTour"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id in setOf("org.jetbrains.kotlin.android", "kotlin-parcelize", "org.jetbrains.kotlin.kapt")) {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
            if (requested.id.id in setOf("com.diffplug.spotless")) {
                useModule("com.diffplug.spotless:spotless-plugin-gradle:${requested.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(
    ":app",
    ":ImageSlider"
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")