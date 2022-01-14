package ru.zolotenkov.shopping_list.activities

import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import org.w3c.dom.Text
import ru.zolotenkov.shopping_list.R
import ru.zolotenkov.shopping_list.databinding.ActivityShopListBinding
import ru.zolotenkov.shopping_list.db.MainViewModel
import ru.zolotenkov.shopping_list.db.ShopListItemAdapter
import ru.zolotenkov.shopping_list.dialogs.EditListItemDialog
import ru.zolotenkov.shopping_list.entities.ShopListItem
import ru.zolotenkov.shopping_list.entities.ShopListNameItem
import ru.zolotenkov.shopping_list.utils.ShareHelper

class ShopListActivity : AppCompatActivity(), ShopListItemAdapter.Listener {
    private lateinit var binding: ActivityShopListBinding
    private var shopListNameItem: ShopListNameItem? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShopListItemAdapter? = null
    private lateinit var textWatcher: TextWatcher

    private val mainViewModel: MainViewModel by viewModels{
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initRcView()
        listItemObserver()
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
                Log.d("MyLog", "$s")
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
                    addNewShopItem()
                }
                R.id.delete_list -> {
                    mainViewModel.deleteShopList(shopListNameItem?.id!!, true)
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
     private fun addNewShopItem(){
         if(edItem?.text.toString().isEmpty())return
         val item = ShopListItem(
             null,
             edItem?.text.toString(),
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
                edItem?.addTextChangedListener(textWatcher())
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher())
                invalidateOptionsMenu()
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
        }

    }

    private fun editListItem(item: ShopListItem){
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener{
            override fun onClick(item: ShopListItem) {
                mainViewModel.updateListItem(item)
            }

        })
    }
}