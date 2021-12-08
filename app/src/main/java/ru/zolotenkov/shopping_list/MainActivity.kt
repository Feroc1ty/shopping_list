package ru.zolotenkov.shopping_list

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import ru.zolotenkov.shopping_list.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding //Все элементы гл. экрана activity_main
    val db = FirebaseDatabase.getInstance()
    val auth = Firebase.auth
    lateinit var myRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()

        myRef = db.getReference(auth.currentUser!!.uid ).child(auth.currentUser?.displayName.toString()).child("products")
        myRef.setValue("TextMessage")



    }
    //Блок тулбара
    private fun setUpActionBar(){
        val actionBar = supportActionBar
        Thread{
            val bMap = Picasso.get().load(auth.currentUser?.photoUrl).get() //Получаем картинку профиля с помощью библиотеки Picasso. Get выдает тип Bitmap
            val dIcon = BitmapDrawable(resources, bMap)                     //Превращаем наш битмап в Drawable
            runOnUiThread {                                                 //Переводим на основной поток операции с View
                actionBar?.setDisplayHomeAsUpEnabled(true)                      //Активируем появление кнопки в тулбаре
                actionBar?.setHomeAsUpIndicator(dIcon)                          //Передаём вместо кнопки назад нашу картинку
                actionBar?.title = auth.currentUser?.displayName                //Подставляем в тайтл экшнбара имя профиля

            }
        }.start()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {  ///Функция выполняется при создании тулбара.
        menuInflater.inflate(R.menu.exit, menu)          //Мы передаём в menu нашу разметку main_menu и инфлатор ее раздувает
        return true                                         //Возвращаем тру, ибо функция возвращает Bool
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {       //Слушатель нажатий в меню
        if(item.itemId == R.id.sign_out){                           //Проверка на нажатие
            auth.signOut()                                          //Выходим из аккаунта
            finish()                                                //Закрываем активити
        }
        return super.onOptionsItemSelected(item)
    }


}