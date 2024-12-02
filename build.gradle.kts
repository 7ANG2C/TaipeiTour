import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidBasePlugin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.spotless)
}

allprojects {
    // setup spotless
    apply(plugin = "com.diffplug.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint().setEditorConfigPath("${rootProject.rootDir}/.editorconfig")
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
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
            with(buildFeatures) {
                buildConfig = true
            }
        }
        afterEvaluate {
            with(extensions.getByType<KotlinAndroidProjectExtension>()) {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }
}
