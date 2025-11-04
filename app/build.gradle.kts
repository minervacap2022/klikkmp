plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
}

android {
    namespace = "com.klikcalendar.shared"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.klikcalendar.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":shared"))

    // Compose
    implementation(compose.runtime)
    implementation(compose.ui)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(compose.animation)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")      // ← 启用 @Preview
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

    // Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")


    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
