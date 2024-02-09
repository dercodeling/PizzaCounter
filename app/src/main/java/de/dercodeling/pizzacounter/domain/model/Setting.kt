package de.dercodeling.pizzacounter.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Setting(
    @PrimaryKey
    val key: String,
    val value: String
)

const val languageSettingKey = "language"
const val themeSettingKey = "theme"
const val defaultTypesSettingKey = "defaultTypes"
const val showResetQuantitiesWarningSettingKey = "showResetQuantitiesWarning"
const val showResetTypesWarningSettingKey = "showResetTypesWarning"

fun settingFrom(languageOption: LanguageOption):Setting {
    return Setting(key = languageSettingKey, value = languageOption.toSettingsValue())
}

fun settingFrom(themeOption: ThemeOption):Setting {
    return Setting(key = themeSettingKey, value = themeOption.toSettingsValue())
}

fun defaultTypesSettingFrom(names: List<String>):Setting {
    return Setting(key = defaultTypesSettingKey, value = names.joinToString(","))
}

fun showResetQuantitiesWarningSettingFrom(enabled: Boolean):Setting {
    return Setting(key = showResetQuantitiesWarningSettingKey, value = enabled.toString())
}

fun showResetTypesWarningSettingFrom(enabled: Boolean):Setting {
    return Setting(key = showResetTypesWarningSettingKey, value = enabled.toString())
}