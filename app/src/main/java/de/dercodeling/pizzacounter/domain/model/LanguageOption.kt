package de.dercodeling.pizzacounter.domain.model

const val languageTagEn = "en"
const val languageTagDe = "de"
const val languageTagEs = "es"

const val languageSettingsValueEn = "lang_en"
const val languageSettingsValueDe = "lang_de"
const val languageSettingsValueEs = "lang_es"
const val languageSettingsValueSystem = "lang_system"

const val languageNameEn = "English"
const val languageNameDe = "Deutsch"
const val languageNameEs = "Español"

enum class LanguageOption: BottomSheetOption {
    EN,
    DE,
    ES,
    SYSTEM;

    companion object {
        fun fromSettingsValue(fromString: String): LanguageOption? {
            return when(fromString) {
                languageSettingsValueEn -> EN
                languageSettingsValueDe -> DE
                languageSettingsValueEs -> ES
                languageSettingsValueSystem -> SYSTEM
                else -> null
            }
        }
    }

    fun toSettingsValue(): String {
        return when(this) {
            EN -> languageSettingsValueEn
            DE -> languageSettingsValueDe
            ES -> languageSettingsValueEs
            SYSTEM -> languageSettingsValueSystem
        }
    }

    fun toLanguageTag(): String? {
        return when(this) {
            EN -> languageTagEn
            DE -> languageTagDe
            ES -> languageTagEs
            else -> null
        }
    }
}