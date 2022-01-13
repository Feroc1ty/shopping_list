package ru.zolotenkov.shopping_list.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.zolotenkov.shopping_list.R
import ru.zolotenkov.shopping_list.databinding.ActivityShopListBinding
import ru.zolotenkov.shopping_list.db.MainViewModel
import ru.zolotenkov.shopping_list.db.ShopListItemAdapter
import ru.zolotenkov.shopping_list.entities.ShopListItem
import ru.zolotenkov.shopping_list.entities.ShopListNameItem

class ShopListActivity : AppCompatActivity(), ShopListItemAdapter.Listener {
    private lateinit var binding: ActivityShopListBinding
    private var shopListNameItem: ShopListNameItem? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShopListItemAdapter? = null

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
        return true
    }
        /*
        Слушатель нажатий кнопок в экшнбаре
         */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_item){
            addNewShopItem()
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
             null,
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
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                saveItem.isVisible = false
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

    override fun onClickItem(shopListItem: ShopListItem) {
        mainViewModel.updateListItem(shopListItem)

    }
}