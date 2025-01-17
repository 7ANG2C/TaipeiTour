rootProject.name = "TaipeiTour"

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id in setOf("com.android.library", "com.android.application")) {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
            if (requested.id.id in setOf("org.jetbrains.kotlin.android", "kotlin-parcelize")) {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
            if (requested.id.id in setOf("com.diffplug.spotless")) {
                useModule("com.diffplug.spotless:spotless-plugin-gradle:${requested.version}")
            }
        }
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":app", ":TaipeiTourApi")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
