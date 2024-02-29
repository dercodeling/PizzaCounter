package de.dercodeling.pizzacounter.ui.main.viewmodel

import android.content.Intent
import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.dercodeling.pizzacounter.data.local.PizzaCounterDao
import de.dercodeling.pizzacounter.data.remote.RetrofitInstance
import de.dercodeling.pizzacounter.domain.model.LanguageOption
import de.dercodeling.pizzacounter.domain.model.NewestVersionInfo
import de.dercodeling.pizzacounter.domain.model.PizzaType
import de.dercodeling.pizzacounter.domain.model.Setting
import de.dercodeling.pizzacounter.domain.model.SortType
import de.dercodeling.pizzacounter.domain.model.ThemeOption
import de.dercodeling.pizzacounter.domain.model.defaultTypesSettingFrom
import de.dercodeling.pizzacounter.domain.model.defaultTypesSettingKey
import de.dercodeling.pizzacounter.domain.model.languageSettingKey
import de.dercodeling.pizzacounter.domain.model.showResetQuantitiesWarningSettingFrom
import de.dercodeling.pizzacounter.domain.model.showResetTypesWarningSettingFrom
import de.dercodeling.pizzacounter.domain.model.settingFrom
import de.dercodeling.pizzacounter.domain.model.showResetQuantitiesWarningSettingKey
import de.dercodeling.pizzacounter.domain.model.showResetTypesWarningSettingKey
import de.dercodeling.pizzacounter.domain.model.themeSettingKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.internal.http2.Header
import okhttp3.internal.toHeaderList
import retrofit2.HttpException
import java.io.IOException
import java.nio.charset.Charset
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalCoroutinesApi::class)
class PizzaCounterViewModel(
    private val dao: PizzaCounterDao
): ViewModel() {

    // Stateful Settings

    private var _language = dao.getSettingByKey(languageSettingKey)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), settingFrom(LanguageOption.SYSTEM))

    private var _theme = dao.getSettingByKey(themeSettingKey)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), settingFrom(ThemeOption.SYSTEM))

    private var _defaultTypes = dao.getSettingByKey(defaultTypesSettingKey)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            defaultTypesSettingFrom(listOf("Margherita", "Prosciutto", "Salami"))
        )

    private val _showResetQuantitiesWarning = dao.getSettingByKey(
        showResetQuantitiesWarningSettingKey)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), showResetQuantitiesWarningSettingFrom(false))

    private val _showResetTypesWarning = dao.getSettingByKey(
        showResetTypesWarningSettingKey)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), showResetTypesWarningSettingFrom(false))

    // Stateless "settings"

    private var _windowSizeClass = MutableStateFlow<WindowSizeClass?>(null)

    private var _newestVersionInfo = MutableStateFlow(NewestVersionInfo())

    private val _sortType = MutableStateFlow(SortType.NAME)

    // List

    private val _pizzaTypes = _sortType
        .flatMapLatest {sortType ->
            when(sortType) {
                SortType.NAME -> dao.getPizzaTypesOrderedByName()
                SortType.QUANTITY_ASC -> dao.getPizzaTypesOrderedByQuantityAscending()
                SortType.QUANTITY_DESC -> dao.getPizzaTypesOrderedByQuantityDescending()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // State

    private val _state = MutableStateFlow(PizzaCounterState())

    val state = combine(
        _state,
        _windowSizeClass,
        _language,
        _theme,
        _defaultTypes,
        _showResetQuantitiesWarning,
        _showResetTypesWarning,
        _sortType,
        _newestVersionInfo,
        _pizzaTypes
    ) { flows: Array<*> ->
        val state = flows[0] as PizzaCounterState
        val windowSizeClass = flows[1] as WindowSizeClass

        val languageSetting = flows[2] as Setting?
        val language = if (languageSetting?.value == null) LanguageOption.SYSTEM else LanguageOption.fromSettingsValue(languageSetting.value) ?: LanguageOption.SYSTEM

        val themeSetting = flows[3] as Setting?
        val theme = if (themeSetting?.value == null) ThemeOption.SYSTEM else ThemeOption.fromSettingsValue(themeSetting.value) ?: ThemeOption.SYSTEM

        val defaultTypesSetting = flows[4] as Setting?
        val defaultTypes = defaultTypesSetting?.value?.split(",") ?: listOf("Margherita", "Prosciutto", "Salami")

        val showResetQuantitiesWarningSetting = flows[5] as Setting?
        val showResetQuantitiesWarning = showResetQuantitiesWarningSetting?.value == "true" // Indirect default: false

        val showResetTypesWarningSetting = flows[6] as Setting?
        val showResetTypesWarning = showResetTypesWarningSetting?.value != "false" // Indirect default: true

        val sortType = flows[7] as SortType

        val newestVersionInfo = flows[8] as NewestVersionInfo

        @Suppress("UNCHECKED_CAST")
        val pizzaTypes = flows[9] as List<PizzaType>

        state.copy(
            windowSizeClass = windowSizeClass,
            language = language,
            theme = theme,
            defaultTypes = defaultTypes,
            showResetQuantitiesWarning = showResetQuantitiesWarning,
            showResetTypesWarning = showResetTypesWarning,
            sortType = sortType,
            newestVersionInfo = newestVersionInfo,
            pizzaTypes = pizzaTypes,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PizzaCounterState())


    fun onEvent(event: PizzaCounterEvent) {
        when(event) {
            is PizzaCounterEvent.ReloadNewestVersionInfo -> {
                if (event.currentVersionNumber == null) return

                val dontCheckBefore = state.value.newestVersionInfo.dontCheckBefore
                if (dontCheckBefore != null) {
                    Log.i(
                        "PizzaCounterViewModel",
                        "Checked GitHub API recently, will retry in ${ChronoUnit.MINUTES.between(LocalDateTime.now(),dontCheckBefore)} minutes."
                    )
                    if (LocalDateTime.now().isBefore(dontCheckBefore)) return
                }

                viewModelScope.launch {
                    val timeoutMinutes: Long = 15

                    try {
                        val latestReleaseResponse = RetrofitInstance.api.getLatestRelease()
                        val currentTag = "v${event.currentVersionNumber}"
                        val currentReleaseResponse = RetrofitInstance.api.getReleaseByTag(currentTag)

                        if (!latestReleaseResponse.isSuccessful || !currentReleaseResponse.isSuccessful) {
                            val headers = currentReleaseResponse.headers().toHeaderList()
                            val isBlockedByRateLimit = headers.contains(Header("x-ratelimit-remaining","0"))

                            if (isBlockedByRateLimit) {
                                Log.w("PizzaCounterViewModel","GitHub API rate limit (60 calls per hour) has been reached")

                                val rateLimitResetHeader = headers.find { it.name.string(Charset.defaultCharset()).contentEquals("x-ratelimit-reset") }
                                val resetEpochMillis = rateLimitResetHeader?.value?.string(Charset.defaultCharset())?.toLong() ?: 0
                                val resetDateTime = Instant.ofEpochMilli(resetEpochMillis).atZone(ZoneId.systemDefault()).toLocalTime()
                                Log.i("PizzaCounterViewModel","The API limit will reset at: $resetDateTime")

                                _newestVersionInfo.value = _newestVersionInfo.value.copy(dontCheckBefore = LocalDateTime.now().plusMinutes(timeoutMinutes))
                            } else {
                                Log.w("PizzaCounterViewModel","GitHub API Call Response not successful")
                            }
                            return@launch
                        }

                        val latestRelease = latestReleaseResponse.body()
                        val currentRelease = currentReleaseResponse.body()

                        if (latestRelease == null || currentRelease == null) return@launch

                        val latestCreatedAt = LocalDateTime.parse(latestRelease.created_at.trimEnd('Z'))
                        val currentCreatedAt = LocalDateTime.parse(currentRelease.created_at.trimEnd('Z'))

                        _newestVersionInfo.value = NewestVersionInfo(
                            isNewestVersion = !currentCreatedAt.isBefore(latestCreatedAt),
                            newestVersionNumber = latestRelease.tag_name.trimStart('v'),
                            newestReleaseUrl = latestRelease.html_url,
                            dontCheckBefore = LocalDateTime.now().plusMinutes(timeoutMinutes)
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: HttpException) {
                        e.printStackTrace()
                    }
                }
            }

            // Settings
            is PizzaCounterEvent.SetWindowSizeClass -> {
                _windowSizeClass.value = event.windowSizeClass
            }

            is PizzaCounterEvent.SetLanguage -> {
                viewModelScope.launch {
                    dao.upsertSetting(settingFrom(event.languageOption))
                }
            }

            is PizzaCounterEvent.SetTheme -> {
                viewModelScope.launch {
                    dao.upsertSetting(settingFrom(event.themeOption))
                }
            }

            is PizzaCounterEvent.SetDefaultTypes -> {
                viewModelScope.launch {
                    dao.upsertSetting(defaultTypesSettingFrom(event.names))
                }
            }

            is PizzaCounterEvent.SetShowResetQuantitiesWarning -> {
                viewModelScope.launch {
                    dao.upsertSetting(showResetQuantitiesWarningSettingFrom(event.showWarning))
                }
            }

            is PizzaCounterEvent.SetShowResetTypesWarning -> {
                viewModelScope.launch {
                    dao.upsertSetting(showResetTypesWarningSettingFrom(event.showWarning))
                }
            }

            // PizzaList

            PizzaCounterEvent.LoadDefaultPizzaTypes -> {
                viewModelScope.launch {
                    for (defaultType in state.value.defaultTypes) {
                        dao.insertPizzaType(PizzaType(defaultType,0))
                    }
                }
            }

            is PizzaCounterEvent.AddPizzaType -> {
                val pizzaType = event.pizzaType

                viewModelScope.launch {
                    dao.insertPizzaType(pizzaType.copy(name = pizzaType.name.trim()))
                }
            }

            is PizzaCounterEvent.DeletePizzaType -> {
                viewModelScope.launch {
                    dao.deletePizzaType(event.pizzaType)
                }
            }

            is PizzaCounterEvent.UpdatePizzaType -> {
                viewModelScope.launch {
                    dao.updatePizzaType(event.pizzaType)
                }
            }

            is PizzaCounterEvent.IncreaseQuantity -> {
                viewModelScope.launch {
                    val current = event.pizzaType
                    dao.updatePizzaType(current.copy(quantity = current.quantity+1))
                }
            }

            is PizzaCounterEvent.DecreaseQuantity -> {
                viewModelScope.launch {
                    val current = event.pizzaType
                    if (current.quantity > 0) {
                        dao.updatePizzaType(current.copy(quantity = current.quantity-1))
                    }
                }
            }

            is PizzaCounterEvent.SetSortType -> {
                _sortType.value = event.sortType
            }

            PizzaCounterEvent.ResetQuantities -> {
                viewModelScope.launch {
                    dao.clearQuantities()
                }
            }

            PizzaCounterEvent.ResetTypes -> {
                viewModelScope.launch {
                    dao.clearTypes()
                    onEvent(PizzaCounterEvent.LoadDefaultPizzaTypes)
                }
            }

            is PizzaCounterEvent.ShareList -> {
                viewModelScope.launch {
                    var outString = event.shareStringPrefix

                    for (pizzaType in _pizzaTypes.value) {
                        if(pizzaType.quantity>0) {
                            outString += "\n${pizzaType.quantity}× ${pizzaType.name}"

                            if(pizzaType.notes.isNotEmpty()) {
                                outString += " (${pizzaType.notes})"
                            }
                        }
                    }

                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, outString)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(event.context, shareIntent, null)
                }
            }

        }
    }
}
