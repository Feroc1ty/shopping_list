package ru.zolotenkov.shopping_list.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.zolotenkov.shopping_list.R
import ru.zolotenkov.shopping_list.databinding.ActivityMainBinding
import ru.zolotenkov.shopping_list.dialogs.NewListDialog
import ru.zolotenkov.shopping_list.entities.ShopListItem
import ru.zolotenkov.shopping_list.fragments.FragmentManager
import ru.zolotenkov.shopping_list.fragments.NoteFragment
import ru.zolotenkov.shopping_list.fragments.ShopListNamesFragment

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding               //Все элементы гл. экрана activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
            /*
            При старте приложения чтобы по дефолту открывался список покупок
             */
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setBottomNavListner()                                           //Кидаем её в цикл активити, слушаем нажатия.
    }

    private fun setBottomNavListner(){                                  //Функция слушатель нажатий в bottomMenu
        binding.bNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.settings ->{
                    Toast.makeText(this, "Pressed Settings", Toast.LENGTH_SHORT).show()
                }
                R.id.notes -> {
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shop_list -> {
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onClick(name: String) {
        Toast.makeText(this, "Its OK", Toast.LENGTH_SHORT).show()
    }

}