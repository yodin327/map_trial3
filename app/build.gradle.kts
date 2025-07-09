plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.save_map"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.save_map"
        minSdk = 33
        targetSdk = 36
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
}

dependencies {
    // Material Design 3
    implementation("androidx.compose.material3:material3:1.2.0")
    // Google Maps & Location
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    // Google APIs
    implementation("com.google.maps.android:maps-ktx:3.4.0")
    implementation("com.google.android.libraries.places:places:3.2.0")
    // Room Database
    implementation("androidx.room:room-runtime:2.5.0")
    // Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // MPAndroidChart for statistics chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}