plugins {
    // Versions declared in settings.gradle.kts pluginManagement block
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    id("org.jetbrains.compose") version "1.6.10" apply false
    id("com.android.library") version "8.2.0" apply false
    id("com.android.application") version "8.2.0" apply false
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}
