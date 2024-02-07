package de.dercodeling.pizzacounter

import de.dercodeling.pizzacounter.domain.model.LanguageOption
import de.dercodeling.pizzacounter.domain.model.Setting
import de.dercodeling.pizzacounter.domain.model.ThemeOption
import de.dercodeling.pizzacounter.domain.model.defaultTypesSettingFrom
import de.dercodeling.pizzacounter.domain.model.defaultTypesSettingKey
import de.dercodeling.pizzacounter.domain.model.languageSettingKey
import de.dercodeling.pizzacounter.domain.model.resetQuantitiesWarningSettingFrom
import de.dercodeling.pizzacounter.domain.model.resetQuantitiesWarningSettingKey
import de.dercodeling.pizzacounter.domain.model.resetTypesWarningSettingFrom
import de.dercodeling.pizzacounter.domain.model.resetTypesWarningSettingKey
import de.dercodeling.pizzacounter.domain.model.settingFrom
import de.dercodeling.pizzacounter.domain.model.themeSettingKey
import org.junit.Test
import org.junit.Assert.*


class SettingTest {
    @Test
    fun languageOptionTest() {
        for (option in LanguageOption.entries) {
            val testedOption = LanguageOption.fromSettingsValue(option.toSettingsValue())
            assertEquals(option, testedOption)
        }
    }

    @Test
    fun themeOptionTest() {
        for (option in ThemeOption.entries) {
            val testedOption = ThemeOption.fromSettingsValue(option.toSettingsValue())
            assertEquals(option, testedOption)
        }
    }

    @Test
    fun languageSettingTest() {
        for (option in LanguageOption.entries) {
            val testedSetting = settingFrom(option)
            assertEquals(Setting(languageSettingKey, option.toSettingsValue()), testedSetting)
        }
    }

    @Test
    fun themeSettingTest() {
        for (option in ThemeOption.entries) {
            val testedSetting = settingFrom(option)
            assertEquals(Setting(themeSettingKey, option.toSettingsValue()), testedSetting)
        }
    }

    @Test
    fun defaultTypesSettingTest() {
        val names = listOf("Margherita","Tonno","Salami")
        assertEquals(Setting(defaultTypesSettingKey, "Margherita,Tonno,Salami"), defaultTypesSettingFrom(names))
    }

    @Test
    fun resetWarningSettingsTest() {
        for (value in listOf(true, false)) {
            assertEquals(
                Setting(resetQuantitiesWarningSettingKey, value.toString()),
                resetQuantitiesWarningSettingFrom(value)
            )

            assertEquals(
                Setting(resetTypesWarningSettingKey, value.toString()),
                resetTypesWarningSettingFrom(value)
            )
        }
    }
}