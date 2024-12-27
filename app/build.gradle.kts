plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.example.login4"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.login4"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding =true
    }

}

dependencies {
    // other dependencies
    implementation("androidx.fragment:fragment:1.5.7")
    implementation("androidx.core:core-ktx:1.13.1")

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:23.0.0")

    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.1")
    implementation ("androidx.cardview:cardview:1.0.0")


    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.navigation:navigation-fragment:2.8.4")
    implementation("androidx.navigation:navigation-ui:2.8.4")
    implementation ("com.google.firebase:firebase-storage:20.2.0")

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")


    implementation("com.google.firebase:firebase-firestore:25.1.1")
    implementation ("com.google.code.gson:gson:2.8.8")
    implementation ("com.google.firebase:firebase-firestore:24.0.0")


        implementation ("androidx.cardview:cardview:1.0.0") // or later






    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation ("com.github.TutorialsAndroid:GButton:v1.0.19")
    implementation ("com.google.android.gms:play-services-auth:21.2.0")


    testImplementation ("com.google.truth:truth:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1") // Core Espresso library for UI testing
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.5.1") // Additional components (RecyclerView, etc.)
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")// Testing intents with Espresso
    androidTestImplementation ("androidx.test.espresso:espresso-accessibility:3.5.1") // Accessibility checks
    androidTestImplementation ("androidx.test.espresso:espresso-web:3.5.1") // For testing web views
    androidTestImplementation ("androidx.test.espresso.idling:idling-concurrent:3.5.1") // Idling resource for concurrent tasks

    androidTestImplementation ("androidx.test:runner:1.5.0") // AndroidX test runner
    androidTestImplementation ("androidx.test:rules:1.5.0") // AndroidX test rules

}