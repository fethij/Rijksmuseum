pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven("https://packages.jetbrains.team/maven/p/firework/dev")
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://packages.jetbrains.team/maven/p/firework/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://packages.jetbrains.team/maven/p/firework/dev")
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
        maven("https://packages.jetbrains.team/maven/p/firework/dev")
    }
}

rootProject.name = "Rijksmuseum"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":composeApp")

include(":core:common")
include(":core:data")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:network")
include(":core:permissions")

include(":feature:arts")
include(":feature:detail")