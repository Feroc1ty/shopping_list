package ru.rykunov.shopping_list.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import ru.rykunov.shopping_list.R
import ru.rykunov.shopping_list.billing.BillingManager
import ru.rykunov.shopping_list.databinding.ActivityShopListBinding
import ru.rykunov.shopping_list.db.MainViewModel
import ru.rykunov.shopping_list.db.ShopListItemAdapter
import ru.rykunov.shopping_list.dialogs.EditListItemDialog
import ru.rykunov.shopping_list.entities.LibraryItem
import ru.rykunov.shopping_list.entities.ShopListItem
import ru.rykunov.shopping_list.entities.ShopListNameItem
import ru.rykunov.shopping_list.utils.ShareHelper

class ShopListActivity : AppCompatActivity(), ShopListItemAdapter.Listener {
    private lateinit var binding: ActivityShopListBinding
    private var shopListNameItem: ShopListNameItem? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShopListItemAdapter? = null
    private lateinit var textWatcher: TextWatcher
    private lateinit var defPref: SharedPreferences
    lateinit var mAdView : AdView
    private lateinit var pref: SharedPreferences

    private val mainViewModel: MainViewModel by viewModels{
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(BillingManager.MAIN_PREF, MODE_PRIVATE)
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initRcView()
        listItemObserver()
        actionBarSettings()
        setTitle(shopListNameItem?.name)
        if (!pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false)){
            binding.adFrame.visibility = View.VISIBLE
            loadBannerAd()
        }
        else binding.adFrame.visibility = View.GONE
    }

    private fun loadBannerAd(){
        if(!pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false)) {
            MobileAds.initialize(this) {}
            mAdView = findViewById(R.id.adView)
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
            binding.adFrame.visibility = View.VISIBLE
        }

    }

    /*
    Подключаем созданное меню к этому активити
    Находим кнопку save, записываем её в переменную и скрываем для отображения
    Сохраняем в переменную newItem все что написали в editText который находится в экшн баре
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shop_list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item)!!
        val newItem = menu?.findItem(R.id.new_item)
        edItem = newItem.actionView.findViewById(R.id.edNewShopItem) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        textWatcher = textWatcher()
        return true
    }

    private fun textWatcher(): TextWatcher{
        return object  : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mainViewModel.getAllLibraryItems("%$s%")
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
    }

        /*
        Слушатель нажатий кнопок в экшнбаре
         */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.save_item -> {
                    addNewShopItem(edItem?.text.toString())
                }
                R.id.delete_list -> {
                    mainViewModel.deleteShopList(shopListNameItem?.id!!, true)
                    finish()
                }
                android.R.id.home -> {
                    saveItemCount()
                    finish()
                }
                R.id.clear_list -> {
                    mainViewModel.deleteShopList(shopListNameItem?.id!!, false)
                }
                /*
                С помощью chooser передаём интент и выбираем приложение с которым хотим поделиться
                 */
                R.id.share_list -> {
                    startActivity(Intent.createChooser(ShareHelper.shareShopList(adapter?.currentList!!, shopListNameItem?.name!!),
                        "Share by"
                    ))
                }
            }

        return super.onOptionsItemSelected(item)
    }
    /*
    Функция записывает в базу итем
     */
     private fun addNewShopItem(name: String){
         if(name.toString().isEmpty())return
         val item = ShopListItem(
             null,
             name,
             "",
             false,
             shopListNameItem?.id!!,
             0
         )
        edItem?.text = null
        mainViewModel.insertShopItem(item)
     }
    private fun listItemObserver(){
        mainViewModel.getAllItemsFromList(shopListNameItem?.id!!).observe(this, {
            adapter?.submitList(it)
            binding.tvEmpty.visibility =
                if(it.isEmpty())
                    View.VISIBLE
                else
                    View.GONE
        })
    }

    private fun libraryItemObserver(){
        mainViewModel.libraryItems.observe(this, {
            val tempShopList = ArrayList<ShopListItem>()
            it.forEach{item ->
                val shopItem = ShopListItem(
                    item.id,
                    item.name,
                    "",
                    false,
                    0,
                    1
                )
                tempShopList.add(shopItem)
            }
            adapter?.submitList(tempShopList)
            binding.tvEmpty.visibility =
                if(it.isEmpty())
                    View.VISIBLE
                else
                    View.GONE
        })
    }
    /*
    Инициализация Recycler View
     */
    private fun initRcView() = with(binding){
        rcView.layoutManager = LinearLayoutManager(this@ShopListActivity)
        adapter = ShopListItemAdapter(this@ShopListActivity)
        rcView.adapter = adapter
    }

    /*
    Функция которая следит за тем когда раскрывается/схлопывается меню в экшн баре
    Тут мы скрываем/показываем кнопку сохранить и перерисовываем меню при его коллапсе
     */
    private fun expandActionView(): MenuItem.OnActionExpandListener{
        return object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                saveItem.isVisible = true
                edItem?.addTextChangedListener(textWatcher)
                libraryItemObserver()
                mainViewModel.getAllItemsFromList(shopListNameItem?.id!!).removeObservers(this@ShopListActivity)
                mainViewModel.getAllLibraryItems("%%")
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher)
                invalidateOptionsMenu()
                mainViewModel.libraryItems.removeObservers(this@ShopListActivity)
                edItem?.setText("")
                listItemObserver()
                return true
            }

        }
    }

    private fun init() {
        shopListNameItem = intent.getSerializableExtra(SHOP_LIST_NAME) as ShopListNameItem
    }

    companion object{
        const val SHOP_LIST_NAME = "shop_list_name"
    }

    override fun onClickItem(shopListItem: ShopListItem, state: Int) {
        when(state){
            ShopListItemAdapter.CHECK_BOX -> mainViewModel.updateListItem(shopListItem)
            ShopListItemAdapter.EDIT -> editListItem(shopListItem)
            ShopListItemAdapter.EDIT_LIBRARY_ITEM -> editLibraryItem(shopListItem)
            ShopListItemAdapter.ADD -> addNewShopItem(shopListItem.name)
            ShopListItemAdapter.DELETE_LIBRARY_ITEM -> {
                mainViewModel.deleteLibraryItem(shopListItem.id!!)
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }
        }

    }

    private fun editListItem(item: ShopListItem){
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener{
            override fun onClick(item: ShopListItem) {
                mainViewModel.updateListItem(item)
            }

        })
    }

    private fun editLibraryItem(item: ShopListItem){
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener{
            override fun onClick(item: ShopListItem) {
                mainViewModel.updateLibraryItem(LibraryItem(item.id, item.name))
                mainViewModel.getAllLibraryItems("%%${edItem?.text.toString()}")
            }

        })
    }

    private fun saveItemCount(){
        var checkedItemCounter = 0
        adapter?.currentList?.forEach{
            if(it.itemChecked) checkedItemCounter++
        }
        val tempShopListNameItem = shopListNameItem?.copy(
            allItemCounter = adapter?.itemCount!!,
            checkedItemsCounter = checkedItemCounter
        )
        mainViewModel.updateListName(tempShopListNameItem!!)
    }

    /*
    Подключаем отображение кнопки назад в экшн баре в этом активити
     */
    private fun actionBarSettings(){
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        saveItemCount()
        super.onBackPressed()
    }


    /*
    Функция которая возвращает тему которая указана в настройках
     */
    private fun getSelectedTheme(): Int{
        return if(defPref.getString("theme_key", "blue") == "blue"){
            R.style.Theme_NewNoteBlue
        }
        else R.style.Theme_NewNoteRed
    }
}