plugins {
    val androidVersion = "8.1.4"
    val kotlinVersion = "1.9.20"

    val daggerHiltVersion = "2.48.1"
    val aboutLibsRelease = "10.6.1"

    id("com.android.application") version androidVersion apply false
    id("com.android.library") version androidVersion apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false

    // Dependency Injection with Hilt
    id("com.google.dagger.hilt.android") version daggerHiltVersion apply false

    // AboutLibraries
    id("com.mikepenz.aboutlibraries.plugin") version aboutLibsRelease apply false
}

buildscript {
    repositories {
        google()
    }

    dependencies {
        val googleServicesVersion = "4.4.0"
        val firebaseCrashlyticsVersion = "2.9.9"
        val firebasePerformanceVersion = "1.4.2"

        // Google services Gradle plugin
        classpath("com.google.gms:google-services:${googleServicesVersion}")

        // Crashlytics Gradle plugin
        classpath("com.google.firebase:firebase-crashlytics-gradle:${firebaseCrashlyticsVersion}")

        // Performance Monitoring Gradle plugin
        classpath("com.google.firebase:perf-plugin:${firebasePerformanceVersion}")
    }
}

configurations {
    all {
        exclude(group = "androidx.lifecycle", module = "lifecycle-runtime-ktx")
    }
}
