plugins {
    `kotlin-dsl`
}

group = "com.tewelde.rijksmuseum.buildlogic"

dependencies {
    compileOnly(libs.plugins.kotlin.serialization.toDep())
    compileOnly(libs.plugins.androidApplication.toDep())
    compileOnly(libs.plugins.androidLibrary.toDep())
    compileOnly(libs.plugins.jetbrainsCompose.toDep())
    compileOnly(libs.plugins.kotlinMultiplatform.toDep())
    compileOnly(libs.plugins.compose.compiler.toDep())
    compileOnly(libs.plugins.ksp.toDep())
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform"){
            id = "com.tewelde.rijksmuseum.kotlinMultiplatform"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
        register("composeMultiplatform"){
            id = "com.tewelde.rijksmuseum.composeMultiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        register("featureMultiplatform"){
            id = "com.tewelde.rijksmuseum.featureMultiplatform"
            implementationClass = "FutureMultiplatformConventionPlugin"
        }
    }
}