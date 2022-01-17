package ru.zolotenkov.shopping_list.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ru.zolotenkov.shopping_list.R

/*
Фрагмент с нашим экраном настроек из xml
 */
class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
    }
}