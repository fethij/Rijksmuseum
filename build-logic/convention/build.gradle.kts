
plugins{
    `kotlin-dsl`
}

group = "com.tewelde.rijksmusuem.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
//java {
//    sourceCompatibility = JavaVersion.VERSION_17
//    targetCompatibility = JavaVersion.VERSION_17
//}
//tasks.withType<KotlinCompile>().configureEach {
//    kotlinOptions {
//        jvmTarget = JavaVersion.VERSION_17.toString()
//    }
//}

dependencies {
//    compileOnly(libs.plugins.kotlinMultiplatform)
//    compileOnly(libs.plugins.androidApplication)
    compileOnly(libs.plugins.jetbrainsCompose)
//    compileOnly(libs.plugins.compose.compiler)
//    alias(libs.plugins.androidApplication)
}

