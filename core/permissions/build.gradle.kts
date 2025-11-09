plugins {
    alias(libs.plugins.rijksmuseum.kotlinMultiplatform)
    alias(libs.plugins.rijksmuseum.featureMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
        val mokoImplMain by creating {
            dependsOn(commonMain.get())

            dependencies {
                implementation(libs.moko.permissions.core)
                implementation(libs.moko.permissions.storage)
            }
        }
        androidMain {
            dependsOn(mokoImplMain)
        }
        iosMain {
            dependsOn(mokoImplMain)
        }
    }
}
