import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.mstudio.superheroarch"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.mstudio.superheroarch"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        val keystoreFile = rootProject.file("local.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        buildConfigField("String", "TMDB_API_KEY", "\"${properties.getProperty("tmdb_api_key")}\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
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
    ksp(libs.room.compiler)
    implementation(libs.room)
    implementation(libs.room.ktx)
    implementation(libs.viewmodel)
    implementation(libs.glide)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.roboletric)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
