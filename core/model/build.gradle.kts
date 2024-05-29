plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin{
    jvm()
    listOf(iosArm64(), iosSimulatorArm64())
}