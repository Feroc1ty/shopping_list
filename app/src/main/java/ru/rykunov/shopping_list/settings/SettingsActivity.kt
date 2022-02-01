package ru.rykunov.shopping_list.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.preference.PreferenceManager
import ru.rykunov.shopping_list.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var defPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setTitle(getString(R.string.settings_activity_title))
        setContentView(R.layout.activity_settings)
        /*
        Если savedInstanceState не пустой, то заменяем наш плейсхолдер в нашем активити с настройками, на фрагмент с настройками созданный в xml
         */
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(R.id.placeHolder, SettingsFragment()).commit()
        }
        /*
        Активируем кнопку назад в экшн баре
         */
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    /*
    Функция слушатель нажатия на кнопку назад в экшн баре который мы отобразили выше
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    /*
    Функция которая возвращает тему которая указана в настройках
     */
    private fun getSelectedTheme(): Int{
        return if(defPref.getString("theme_key", "blue") == "blue"){
            R.style.Theme_ShoppingListBlue
        }
        else R.style.Theme_ShoppingListRed
    }
}