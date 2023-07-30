import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidBasePlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.plugin.KOTLIN_OPTIONS_DSL_NAME

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.spotless)
}

allprojects {
    // setup spotless
    apply(plugin = "com.diffplug.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint()
            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint()
            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }
    }
}

subprojects {
    plugins.withType<AndroidBasePlugin> {
        configure<BaseExtension> {
            compileSdkVersion(libs.versions.compileSdk.get().toInt())
            defaultConfig {
                minSdk = libs.versions.minSdk.get().toInt()
                targetSdk = libs.versions.compileSdk.get().toInt()
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }

        afterEvaluate {
            val appModuleExtension = extensions.findByType<BaseAppModuleExtension>()
            val libraryExtension = extensions.findByType<LibraryExtension>()
            val extensionAware = (appModuleExtension ?: libraryExtension) as ExtensionAware
            val kotlinOptions = extensionAware.extensions.getByName(KOTLIN_OPTIONS_DSL_NAME) as KotlinJvmOptions
            kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }
}

true // Needed to make the Suppress annotation work for the plugins block