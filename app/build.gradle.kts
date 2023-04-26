plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Google services Gradle plugin
    id("com.google.gms.google-services")

    id("kotlin-android")

    // Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")

    // Performance Monitoring Gradle plugin
    id("com.google.firebase.firebase-perf")

    // AboutLibraries
    id("com.mikepenz.aboutlibraries.plugin")

    // Dependency Injection with Hilt
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file(System.getenv("ANDROID_KEYSTORE_FILE"))
            storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("ANDROID_KEY_ALIAS")
            keyPassword = System.getenv("ANDROID_KEY_PASSWORD")
        }
    }
    namespace = "com.mohandass.botforge"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.mohandass.botforge"
        minSdk = 28
        targetSdk = 33
        versionCode = 30
        versionName = "1.3.0"

        vectorDrawables.useSupportLibrary = true
        signingConfig = signingConfigs.getByName("debug")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        named("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            @Suppress("UnstableApiUsage")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    @Suppress("UnstableApiUsage")
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

configurations {
    implementation.get().exclude(group = "org.jetbrains", module = "annotations")
}

dependencies {
    val composeBomVersion = "2023.04.00"
    val activityVersion = "1.7.0"
    val coreVersion = "1.10.0"
    val lifecycleVersion = "2.6.1"
    val navVersion = "2.5.3"
    val composeRuntimeVersion = "1.4.1"
    val datastorePreferencesVersion = "1.0.0"
    val roomVersion = "2.5.1"
    val daggerHiltVersion = "2.45"
    val hiltNavVersion = "1.0.0"
    val firebaseBomVersion = "31.5.0"
    val gsonVersion = "2.10.1"
    val openaiKotlinVersion = "3.2.1"
    val percentageUnitsVersion = "1.0.0"
    val prettytimeVersion = "5.0.6.Final"
    val aboutLibsRelease = "10.6.2"
    val accompanistVersion = "0.31.0-alpha"
    val composeMarkdownVersion = "0.3.1"
    val coilVersion = "2.3.0"
    val markwonVersion = "4.6.2"
    val leakCanaryVersion = "2.10"
    val okioVersion = "3.3.0"

    val playServicesAuthVersion = "20.5.0"

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.activity:activity-compose:$activityVersion")

    implementation("androidx.core:core-ktx:$coreVersion")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")

    // Navigation
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // LiveData
    implementation("androidx.compose.runtime:runtime-livedata:$composeRuntimeVersion")

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:$datastorePreferencesVersion")

    // Room
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Dependency Injection with Hilt
    implementation("com.google.dagger:hilt-android:$daggerHiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$daggerHiltVersion")

    implementation("androidx.hilt:hilt-navigation-compose:$hiltNavVersion")

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:$firebaseBomVersion"))
    // Firebase SDK for Google Analytics, Crashlytics, Authentication, DB, AppCheck
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-appcheck-playintegrity")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("com.google.firebase:firebase-appcheck-ktx")

    implementation("com.google.android.gms:play-services-auth:$playServicesAuthVersion")

    // gson
    implementation("com.google.code.gson:gson:$gsonVersion")

    // OpenAI Kotlin
    implementation(platform("com.aallam.openai:openai-client-bom:$openaiKotlinVersion"))
    implementation("com.aallam.openai:openai-client")
    implementation("io.ktor:ktor-client-okhttp")

    // Scale units as DH (Device Height) and DW (Device Width)
    implementation("com.github.slaviboy:JetpackComposePercentageUnits:$percentageUnitsVersion")

    // pretty time
    implementation("org.ocpsoft.prettytime:prettytime:$prettytimeVersion")

    // AboutLibraries
    implementation("com.mikepenz:aboutlibraries-core:${aboutLibsRelease}")
    implementation("com.mikepenz:aboutlibraries-compose:${aboutLibsRelease}")

    // Accompanist Navigation Animation
    implementation("com.google.accompanist:accompanist-navigation-animation:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-placeholder-material:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-flowlayout:$accompanistVersion")

    // Compose Markdown
    implementation("com.github.jeziellago:compose-markdown:$composeMarkdownVersion")
    implementation("io.coil-kt:coil-compose:$coilVersion")

    implementation("io.noties.markwon:core:$markwonVersion")
    implementation("io.noties.markwon:ext-strikethrough:$markwonVersion")
    implementation("io.noties.markwon:ext-tables:$markwonVersion")
    implementation("io.noties.markwon:html:$markwonVersion")

    // LeakCanary for memory leak detection
    // https://square.github.io/leakcanary/
    debugImplementation("com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion")

    implementation("com.squareup.okio:okio:$okioVersion")
}

// Dependency Injection with Hilt
// Allow references to generated code
kapt {
    correctErrorTypes = true
}
