package com.tewelde.rijksmuseum

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.util.Locale

fun Project.addKspDependencyForAllTargets(dependencyNotation: Any) =
    addKspDependencyForAllTargets("", dependencyNotation)

private fun Project.addKspDependencyForAllTargets(
    configurationNameSuffix: String,
    dependencyNotation: Any,
) {
    val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
    val kspTargets = kmpExtension.targets.names.map { targetName ->
        targetName.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString()
        }
    }
    dependencies {
        kspTargets
            .asSequence()
            .map { target ->
                if (target == "Metadata") "CommonMainMetadata" else target
            }
            .forEach { targetConfigSuffix ->
                add("ksp${targetConfigSuffix}$configurationNameSuffix", dependencyNotation)
            }
    }
}