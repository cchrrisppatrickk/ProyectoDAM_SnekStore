plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.snekstorep"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.snekstorep"
        minSdk = 29
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

    // Añade esto dentro del bloque android:
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    // ====================== UI COMPONENTS ======================
// ImageSlideshow: Biblioteca para crear sliders/carruseles de imágenes deslizables
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")

// DotsIndicator: Muestra puntos indicadores para ViewPager/ViewPager2 (paginación visual)
    implementation("com.tbuonomo:dotsindicator:5.0")

// BlurView: Efectos de desenfoque (blur) en vistas con alto rendimiento
    implementation("com.github.Dimezis:BlurView:version-2.0.6")

// Material Components: Componentes UI modernos de Material Design
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")


// ====================== IMAGE LOADING ======================
// Glide: Carga, caché y procesamiento eficiente de imágenes (incluye GIFs)
    implementation("com.github.bumptech.glide:glide:4.16.0")


// ====================== DATA & SERIALIZATION ======================
// Gson: Conversión entre objetos Java/Kotlin y JSON (serialización/deserialización)
    implementation("com.google.code.gson:gson:2.9.1")


// ====================== ARCHITECTURE COMPONENTS ======================
// Lifecycle: Gestión del ciclo de vida de Activities/Fragments con ViewModel y LiveData
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

// Activity KTX: Extensiones Kotlin para Activity (simplifica ViewModel access)
    implementation("androidx.activity:activity-ktx:1.4.0")


    implementation ("com.google.android.material:material:1.9.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

//
    implementation("com.yarolegovich:sliding-root-nav:1.1.1")



}