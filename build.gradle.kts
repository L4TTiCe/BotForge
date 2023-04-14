plugins {
    val androidVersion = "8.0.0"
    val kotlinVersion = "1.7.0"

    val daggerHiltVersion = "2.44"
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
        val googleServicesVersion = "4.3.15"
        val firebaseCrashlyticsVersion = "2.9.4"
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
