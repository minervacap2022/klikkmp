rootProject.name = "KlikCalendar"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("org.jetbrains.kotlin.android") version "2.0.0"
        id("org.jetbrains.compose") version "1.6.10"
        id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
        id("com.android.library") version "8.2.0"
        id("com.android.application") version "8.2.0"
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":app")
include(":shared")
