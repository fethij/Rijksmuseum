package com.tewelde.rijksmuseum

import java.util.Locale
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

fun Project.addKspDependencyForAllTargets(
    dependencyNotation: Any,
    includeCommonMainMetadata: Boolean = true,
) {
    val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
    dependencies {
        kmpExtension.targets
            .asSequence()
            .filter { target ->
                // Don't add KSP for common target, only final platforms
                target.platformType != KotlinPlatformType.common
            }
            .forEach { target -> add("ksp${target.targetName.capitalized()}", dependencyNotation) }
            .also {
                if (includeCommonMainMetadata) {
                    add("kspCommonMainMetadata", dependencyNotation)
                }
            }
    }
}

fun String.capitalized() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString()
}
