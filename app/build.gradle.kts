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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
    
    applicationVariants.all { variant ->
        variant.outputs.all { output ->
            val outputImpl = output as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            val fileName = outputImpl.outputFileName
            if (fileName.contains("app-")) {
                outputImpl.outputFileName = fileName.replace("app-", "XGym-")
            }
            true
        }
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