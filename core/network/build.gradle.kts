import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.buildConfig)
}

val secretKeyProperties by lazy {
    val secretKeyPropertiesFile = rootProject.file("secrets.properties")
    Properties().apply { secretKeyPropertiesFile.inputStream().use { secret -> load(secret) } }
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    jvm("desktop")
    listOf(iosArm64(), iosSimulatorArm64())
    wasmJs { browser() }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.ktor.client.android)
            implementation(libs.kotlinx.coroutines.android)
        }
        commonMain.dependencies {
            implementation(projects.core.model)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.bundles.ktor.common)
            implementation(libs.kotlinx.coroutines.core)
        }
        desktopMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.java)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.tewelde.rijksmuseum.core.network"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        consumerProguardFiles("consumer-proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

buildConfig {
    packageName = "com.tewelde.rijksmuseum"
    buildConfigField(
        "String",
        "RIJKSMUSEUM_API_KEY",
        "\"${secretKeyProperties["rijksmuseum.api.key"]}\""
    )
}