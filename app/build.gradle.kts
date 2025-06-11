plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "io.celox.xgym"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.celox.xgym"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Set the APK name
        setProperty("archivesBaseName", "XGym-${versionName}")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Room database
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Image loading
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // JSON parsing
    implementation(libs.gson)

    // Charts
    implementation(libs.mpandroidchart)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}