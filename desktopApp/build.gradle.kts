import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
}

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())
    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(projects.shared)
            implementation(projects.core.common)

            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.bundles.kotlinInjectAnvil)
            // KSP processors needs to see RijksmuseumApp's constructor types
            implementation(libs.bundles.circuit)
        }
    }
}

dependencies {
    add("kspJvm", libs.kotlinInject.compiler)
    add("kspJvm", libs.kotlinInject.anvil.compiler)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.tewelde.rijksmuseum"
            packageVersion = "1.0.0"
        }
    }
}
