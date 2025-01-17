import com.google.devtools.ksp.gradle.KspExtension
import com.tewelde.rijksmuseum.addKspDependencyForAllTargets
import com.tewelde.rijksmuseum.configureParcelize
import com.tewelde.rijksmuseum.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FutureMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("ksp").get().get().pluginId)
            apply(libs.findPlugin("kotlin.parcelize").get().get().pluginId)
        }

        extensions.configure<KotlinMultiplatformExtension> {
            configureParcelize()
            sourceSets.apply {
                commonMain {
                    dependencies {
                        implementation(libs.findBundle("kotlinInjectAnvil").get())
                        implementation(libs.findBundle("circuit").get())
                    }
                }
            }
        }

        extensions.configure<KotlinMultiplatformExtension> {
            addKspDependencyForAllTargets(libs.findLibrary("circuit.codegen").get())
            addKspDependencyForAllTargets(libs.findLibrary("kotlinInject.compiler").get())
            addKspDependencyForAllTargets(libs.findLibrary("kotlinInject.anvil.compiler").get())
        }

        extensions.configure<KspExtension> {
            arg("circuit.codegen.lenient", "true")
            arg("circuit.codegen.mode", "kotlin_inject_anvil")
            arg(
                "kotlin-inject-anvil-contributing-annotations",
                "com.slack.circuit.codegen.annotations.CircuitInject"
            )
        }
    }
}