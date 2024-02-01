plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "gr.gkortsaridis.marvelherodownloader"
    compileSdk = 34

    defaultConfig {
        minSdk = 29
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
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.room:room-common:2.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Retrofit & Glide
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-compiler:2.50")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.50")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.50")
    testImplementation("com.google.dagger:hilt-android-testing:2.50")
    kaptTest("com.google.dagger:hilt-compiler:2.50")
}