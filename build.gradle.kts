import dev.iurysouza.modulegraph.ModuleType
import dev.iurysouza.modulegraph.Theme

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.buildConfig) apply false
    alias(libs.plugins.modulegraph)
}

moduleGraphConfig {
    readmePath.set("./README.md")
    heading = "### Module Graph"
    showFullPath = false
    setStyleByModuleType = true
    ModuleType.AndroidApp("#3CD483")

//    theme.set(
//        Theme.BASE(
//            moduleTypes = listOf(
//                ModuleType.Custom(id = "app.compose", color = "#0E0E0E"),
//                ModuleType.AndroidApp("#3CD483"),
//                ModuleType.AndroidLibrary("#292B2B"),
//                ModuleType.KotlinMultiplatform("#b04ec7"),
//                ModuleType.KotlinMultiplatform("#d2497f"),
//            ),
//        ),
//    )
}