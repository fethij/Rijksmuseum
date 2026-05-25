import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.tewelde.rijksmuseum.configureKotlinMultiplatformAndroid
import com.tewelde.rijksmuseum.configureKotlinMultiplatformBase
import com.tewelde.rijksmuseum.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for KMP library modules under AGP 9.
 *
 * Under AGP 9 the `com.android.kotlin.multiplatform.library` plugin replaces the old
 * `com.android.library` plugin: the Android target is auto-registered when its DSL is
 * configured via `kotlin { android { ... } }`. That DSL is exposed as a NESTED extension
 * on [KotlinMultiplatformExtension] (extension name "android"), so the convention plugin
 * needs to walk through ExtensionAware to reach it.
 */
class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            apply(libs.findPlugin("androidKmpLibrary").get().get().pluginId)
            apply(libs.findPlugin("kotlin.serialization").get().get().pluginId)
        }

        extensions.configure<KotlinMultiplatformExtension>(::configureKotlinMultiplatformBase)

        // The `android { ... }` block lives as a nested extension on the KMP extension.
        extensions.configure<KotlinMultiplatformExtension> {
            val androidExt = (this as ExtensionAware).extensions
                .getByType(KotlinMultiplatformAndroidLibraryExtension::class.java)
            androidExt.configureKotlinMultiplatformAndroid(this@with)
        }
    }
}
