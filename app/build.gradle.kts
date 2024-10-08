plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.jefferson.apps.real_world.android.real_worldandroidapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jefferson.apps.real_world.android.real_worldandroidapp"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(project(":logging"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.monitor)
    implementation(libs.reactivex.room)
    ksp(libs.androidx.room.compiler)

    // Hilt - DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Retrofit e okhttp
    implementation(libs.retrofit)
    implementation(libs.moshi)
    implementation(libs.okhttp)
    implementation(libs.log.okhttp.inteceptor)
    ksp(libs.moshi.kotlin.codegen)

    // Fragment-Navigation
    implementation(libs.frag.navigation)
    implementation(libs.frag.navigation.ui)

    // Glide
    implementation(libs.glide)

    // biblioteca rxjava
    implementation(libs.reactivex.rxjava)
    implementation(libs.reactivex.rxkotlin)
    implementation(libs.reactivex.rxandroid)

    // Testes
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockito)
    testImplementation(libs.roletric)
    testImplementation(libs.org.hamcrast)
    testImplementation(libs.okhttp3.mockwebserver)
    androidTestImplementation(libs.okhttp3.mockwebserver)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.hilt.test)
    debugImplementation(libs.androidx.arch.core.testing)
    debugImplementation(libs.kotlinx.test)
    debugImplementation(libs.fragment.test)
    debugImplementation(libs.truth)
}