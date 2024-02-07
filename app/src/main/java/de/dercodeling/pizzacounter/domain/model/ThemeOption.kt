package de.dercodeling.pizzacounter.domain.model

const val themeSettingsValueLight = "theme_light"
const val themeSettingsValueDark = "theme_dark"
const val themeSettingsValueSystem = "theme_system"

enum class ThemeOption: BottomSheetOption {
    LIGHT,
    DARK,
    SYSTEM;

    companion object {
        fun fromSettingsValue(fromString: String): ThemeOption? {
            return when(fromString) {
                themeSettingsValueLight -> LIGHT
                themeSettingsValueDark -> DARK
                themeSettingsValueSystem -> SYSTEM
                else -> null
            }
        }
    }

    fun toSettingsValue(): String {
        return when(this) {
            LIGHT -> themeSettingsValueLight
            DARK -> themeSettingsValueDark
            SYSTEM -> themeSettingsValueSystem
        }
    }
}