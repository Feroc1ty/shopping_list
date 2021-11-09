package ru.zolotenkov.shopping_list

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import ru.zolotenkov.shopping_list.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var shopMain: ActivityMainBinding //Все элементы гл. экрана activity_main



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(shopMain.root)

        shopMain.button.setOnClickListener {
            shopMain.textBlock.text = "Комит готов"
            shopMain.layoutBox.setBackgroundColor(Color.YELLOW)
        }


    }
}