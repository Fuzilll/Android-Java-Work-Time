plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.timemarkinghr"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.timemarkinghr"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.4")

    implementation ("com.google.android.material:material:1.13.0-alpha11")

    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.12.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("androidx.core:core-ktx:1.10.1")
    implementation ("com.cloudinary:cloudinary-android:3.0.2")

}