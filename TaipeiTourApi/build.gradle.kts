plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.module.taipeitourapi"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    testImplementation(libs.junit)

    // custom
    implementation(libs.koin.core)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)

    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
}
