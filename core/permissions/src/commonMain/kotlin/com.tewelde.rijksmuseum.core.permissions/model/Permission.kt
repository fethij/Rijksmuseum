package com.tewelde.rijksmuseum.core.permissions.model

/**
 * This enum represents the permissions used in the application.
 * It provides constant values for various permissions related to system services and features.
 */
enum class Permission {
    /**
     * Indicates that the system setting bluetooth service is on.
     */
    BLUETOOTH_SERVICE_ON,

    /**
     * App bluetooth permission.
     */
    BLUETOOTH,

    /**
     * Indicates that the system setting location service is on.
     */
    LOCATION_SERVICE_ON,

    /**
     * App location fine permission.
     */
    LOCATION_FOREGROUND,

    /**
     * App location background permission.
     */
    LOCATION_BACKGROUND,

    /**
     * App storage write permission.
     */
    WRITE_STORAGE
}
