package de.dercodeling.pizzacounter.domain.model

data class NewestVersionInfo(
    val isNewestVersion: Boolean? = null,
    val newestVersionNumber: String? = null,
    val newestReleaseUrl: String? = null,
    val dontCheckAgain: Boolean = false
)
