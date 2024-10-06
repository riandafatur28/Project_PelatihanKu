plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.projectpelatihanku"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.projectpelatihanku"
        minSdk = 21
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
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location.license)
    implementation(libs.gms.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}