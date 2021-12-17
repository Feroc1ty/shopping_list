package ru.zolotenkov.shopping_list.activities

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import com.squareup.picasso.Picasso
import ru.zolotenkov.shopping_list.R
import ru.zolotenkov.shopping_list.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding               //Все элементы гл. экрана activity_main


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavListner()                                           //Кидаем её в цикл активити, слушаем нажатия.
    }

    private fun setBottomNavListner(){                                  //Функция слушатель нажатий в bottomMenu
        binding.bNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.settings ->{
                    Toast.makeText(this, "Pressed Settings", Toast.LENGTH_SHORT).show()
                }
                R.id.notes -> {
                    Toast.makeText(this, "Pressed Notes", Toast.LENGTH_SHORT).show()
                }
                R.id.shop_list -> {
                    Toast.makeText(this, "Pressed Shop List", Toast.LENGTH_SHORT).show()
                }
                R.id.new_item -> {
                    Toast.makeText(this, "Pressed Add Item", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

}