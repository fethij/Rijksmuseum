package com.tewelde.rijksmuseum

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureKotlinAndroid(
    extension: LibraryExtension
) = extension.apply {

    //get module name from module path
    val moduleName = path.split(":").drop(2).joinToString(".")
    namespace = if(moduleName.isNotEmpty()) "com.tewelde.rijksmuseum.$moduleName" else "com.tewelde.rijksmuseum"

    compileSdk = libs.findVersion("android.compileSdk").get().requiredVersion.toInt()
    defaultConfig {
        minSdk = libs.findVersion("android.minSdk").get().requiredVersion.toInt()
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}