package de.dercodeling.pizzacounter.domain.model

const val languageTagEn = "en"
const val languageTagDe = "de"

const val languageSettingsValueEn = "lang_en"
const val languageSettingsValueDe = "lang_de"
const val languageSettingsValueSystem = "lang_system"

enum class LanguageOption: BottomSheetOption {
    EN,
    DE,
    SYSTEM;

    companion object {
        fun fromSettingsValue(fromString: String): LanguageOption? {
            return when(fromString) {
                languageSettingsValueEn -> EN
                languageSettingsValueDe -> DE
                languageSettingsValueSystem -> SYSTEM
                else -> null
            }
        }
    }

    fun toSettingsValue(): String {
        return when(this) {
            EN -> languageSettingsValueEn
            DE -> languageSettingsValueDe
            SYSTEM -> languageSettingsValueSystem
        }
    }

    fun toLanguageTag(): String? {
        return when(this) {
            EN -> languageTagEn
            DE -> languageTagDe
            else -> null
        }
    }
}