package com.tewelde.rijksmuseum.core.permissions

import androidx.activity.ComponentActivity

fun PermissionsController.bind(activity: ComponentActivity) {
    if (this is MokoPermissionControllerWrapper) {
        mokoPermissionController.bind(activity)
    } else {
        error("PermissionsController does not wrap Moko Permissions")
    }
}
