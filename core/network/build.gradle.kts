import java.util.Properties

plugins {
    alias(libs.plugins.rijksmuseum.kotlinMultiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.ksp)
}

val secretKeyProperties by lazy {
    val secretKeyPropertiesFile = rootProject.file("secrets.properties")
    Properties().apply { secretKeyPropertiesFile.inputStream().use { secret -> load(secret) } }
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }
        commonMain.dependencies {
            implementation(projects.core.model)

            implementation(libs.kotlinx.serialization.json)
            api(libs.bundles.ktor.common)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.java)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

buildConfig {
    packageName = "com.tewelde.rijksmuseum"
    useKotlinOutput { internalVisibility = true }
    buildConfigField(
        "String",
        "RIJKSMUSEUM_API_KEY",
        "\"${secretKeyProperties["rijksmuseum.api.key"]}\""
    )
    buildConfigField(
        "String",
        "APP_NAME",
        "\"${rootProject.name}\""
    )
}

dependencies {
    ksp(libs.kotlinInject.compiler)
    ksp(libs.kotlinInject.anvil.compiler)
}