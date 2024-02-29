package de.dercodeling.pizzacounter.domain.model

import java.time.LocalDateTime

data class NewestVersionInfo(
    val isNewestVersion: Boolean? = null,
    val newestVersionNumber: String? = null,
    val newestReleaseUrl: String? = null,
    val dontCheckBefore: LocalDateTime? = null
)
