package ru.zolotenkov.shopping_list.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import ru.zolotenkov.shopping_list.R
import ru.zolotenkov.shopping_list.databinding.ActivityMainBinding
import ru.zolotenkov.shopping_list.dialogs.NewListDialog
import ru.zolotenkov.shopping_list.fragments.FragmentManager
import ru.zolotenkov.shopping_list.fragments.NoteFragment
import ru.zolotenkov.shopping_list.fragments.ShopListNamesFragment
import ru.zolotenkov.shopping_list.settings.SettingsActivity
import java.util.*

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding               //Все элементы гл. экрана activity_main
    private var currentMenuItemId = R.id.shop_list
    private lateinit var defPref: SharedPreferences
    private var currentTheme = ""
    lateinit var mAdView : AdView
    /*
    private var iAd: InterstitialAd? = null
    private var adShowCounter = 0
    private var adShowCounterMax = 4
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = defPref.getString("theme_key", "blue").toString()
        setTheme(getSelectedTheme())

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
            /*
            При старте приложения чтобы по дефолту открывался список покупок
             */
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setBottomNavListner()                                           //Кидаем её в цикл активити, слушаем нажатия.
        loadBannerAd()
        Log.d("MyLog", "${Locale.getDefault().getDisplayLanguage()}")
    }
    /*
    Функция которая загружает рекламу
    нужно передать контекст, айди рекламы, колбек функции когда загружается реклама
     */

    private fun loadBannerAd(){
        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }
/*
    private fun loadInterAd(){
        val request = AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.inter_ad_id), request,
            object : InterstitialAdLoadCallback(){
                override fun onAdLoaded(ad: InterstitialAd) {
                    iAd = ad
                    Log.d("MyLog", "iAd = ad: реклама успешно загрузилась")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    iAd = null
                    Log.d("MyLog", "iAd = null: не удалось загрузить рекламу")
                }
        })
    }

 */
    /*
    Функция для показа рекламы

    private fun showInterAd(adListener: AdListener){
        if(iAd != null && adShowCounter > adShowCounterMax){
            iAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
                /*
                Выполняется когда закрыли рекламу
                 */
                override fun onAdDismissedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                    adListener.onFinish()

                }
                /*
                функция выполняется когда произошла какая то ошибка и реклама не показалась
                 */
                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    iAd = null
                    loadInterAd()
                }
                /*
                функция выполняется когда обьявление полностью показалось
                 */
                override fun onAdShowedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                }
            }
            /*
            Этой функцией показываем обьявление и сбрасываем счётчик
             */
            adShowCounter = 0
            iAd?.show(this)
        } else {
            adShowCounter++
            Log.d("MyLog", adShowCounter.toString())

            adListener.onFinish()
        }
    }
    */

    private fun setBottomNavListner(){                                  //Функция слушатель нажатий в bottomMenu
        binding.bNav.setOnItemSelectedListener {
            when(it.itemId){

                /*
                Запускается реклама при переходе в настройки и открывается активити только при выполнении условий для запуска функции onFinish
                 */
                R.id.settings ->{
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                }
                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this@MainActivity)
                }
                R.id.shop_list -> {
                    currentMenuItemId = R.id.shop_list
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bNav.selectedItemId = currentMenuItemId
        if (currentTheme != defPref.getString("theme_key", "blue")){
            recreate()
        }
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
    override fun onClick(name: String) {
        Toast.makeText(this, "Its OK", Toast.LENGTH_SHORT).show()
    }
/*
interface AdListener{
   fun onFinish(){

   }
}
 */

}