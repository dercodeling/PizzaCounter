package de.dercodeling.pizzacounter.domain.model

//@Entity
data class Setting(
    //@PrimaryKey
    val key: String,
    val value: String
)

const val languageSettingKey = "language"
const val themeSettingKey = "theme"
const val defaultTypesSettingKey = "defaultTypes"
const val resetQuantitiesWarningSettingKey = "resetQuantitiesWarning"
const val resetTypesWarningSettingKey = "resetTypesWarning"

fun settingFrom(languageOption: LanguageOption):Setting {
    return Setting(key = languageSettingKey, value = languageOption.toSettingsValue())
}

fun settingFrom(themeOption: ThemeOption):Setting {
    return Setting(key = themeSettingKey, value = themeOption.toSettingsValue())
}

fun defaultTypesSettingFrom(names: List<String>):Setting {
    return Setting(key = defaultTypesSettingKey, value = names.joinToString(","))
}

fun resetQuantitiesWarningSettingFrom(enabled: Boolean):Setting {
    return Setting(key = resetQuantitiesWarningSettingKey, value = enabled.toString())
}

fun resetTypesWarningSettingFrom(enabled: Boolean):Setting {
    return Setting(key = resetTypesWarningSettingKey, value = enabled.toString())
}