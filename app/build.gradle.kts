plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.timurvg.slapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.timurvg.slapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // Строковые ресурсы для сервиса доступности
        buildConfigField("String", "ACCESSIBILITY_SERVICE_LABEL", "\"Screen Lock Service\"")
        buildConfigField("String", "ACCESSIBILITY_SERVICE_DESC", "\"Service for locking screen with gestures\"")
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

    // Включение функции buildConfig
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.core:core:1.12.0")
}