plugins {
    id("com.android.application")
    id("kotlin-parcelize")
    kotlin("android")
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation("androidx.compose.ui:ui:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["compose_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.activity:activity-compose:1.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["compose_version"]}")
    debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"]}")


    implementation("io.insert-koin:koin-android:3.1.3")
    implementation("io.insert-koin:koin-androidx-compose:3.1.3")
    implementation("androidx.navigation:navigation-compose:2.4.0-beta02")
    implementation("io.coil-kt:coil-compose:1.4.0")
    implementation("io.ktor:ktor-client-serialization:1.6.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.1.0-beta02")
    implementation("com.google.accompanist:accompanist-pager:0.21.2-beta")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.21.3-beta")

}

android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "com.izanaminightz.mochi.android"
        minSdkVersion(22)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["compose_version"] as String
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}