import com.google.protobuf.gradle.id
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.protobuf") version "0.9.4"
}

android {
    namespace = "com.example.test_grpc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.test_grpc"
        minSdk = 24
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
        viewBinding = true
    }

    packagingOptions {
        exclude("google/protobuf/field_mask.proto")
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.2")

    // GRPC
    implementation("io.grpc:grpc-okhttp:1.60.0")
    implementation("io.grpc:grpc-protobuf:1.60.0")
    implementation("io.grpc:grpc-protobuf-lite:1.60.0")
    implementation("io.grpc:grpc-stub:1.60.0")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53") // necessary for Java 9+
    implementation("io.grpc:grpc-okhttp:1.60.0")
    implementation("io.grpc:grpc-stub:1.60.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    // Protobuf
    implementation("com.google.protobuf:protobuf-java:3.24.0")
    // gRPC-Kotlin
    implementation("io.grpc:grpc-kotlin-stub:1.4.1") // Use the latest version
    // Kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Use the latest version
}

protobuf {
    protoc {
        artifact = if (osdetector.os == "osx") {
            "com.google.protobuf:protoc:3.25.1:osx-x86_64"
        } else {
            "com.google.protobuf:protoc:3.25.1"
        }
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.60.0"
        }
//        id("grpckt") {
//            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.1:jdk8@jar"
//        }

    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc") {
                }
//                create("grpckt") {
//                } // Kotlin
            }
            task.builtins {
//                create("kotlin") {
//                }
            }
        }
    }
}

configurations {
    all {
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
    }
}