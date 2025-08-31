import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.gateopener"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gateopener"
        minSdk = 28
        targetSdk = 35
        versionCode = 5
        versionName = "5.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val localProperties = Properties()
    val localPropertiesFile = File(rootDir, "secrets.properties")
    if (localPropertiesFile.exists() && localPropertiesFile.isFile){
        localPropertiesFile.inputStream().use {
            localProperties.load(it)
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "API_KEY", localProperties.getProperty("API_KEY"))
            buildConfigField("String", "SERVICE_ADDRESS", localProperties.getProperty("SERVICE_ADDRESS"))
            buildConfigField("String", "SERVICE_ENDPOINT", localProperties.getProperty("SERVICE_ENDPOINT"))
        }
        debug {
            buildConfigField("String", "API_KEY", localProperties.getProperty("API_KEY"))
            buildConfigField("String", "SERVICE_ADDRESS", localProperties.getProperty("SERVICE_ADDRESS"))
            buildConfigField("String", "SERVICE_ENDPOINT", localProperties.getProperty("SERVICE_ENDPOINT"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.car.app:app:1.2.0")
    implementation("androidx.car.app:app-projected:1.2.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}