package ru.zolotenkov.shopping_list.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import ru.zolotenkov.shopping_list.R
import ru.zolotenkov.shopping_list.databinding.ActivityNewNoteBinding
import ru.zolotenkov.shopping_list.entities.NoteItem
import ru.zolotenkov.shopping_list.fragments.NoteFragment
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBarSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /*
    Слушаем нажатия по кнопкам в меню экшн бара
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_save) {
            setMainResult()
        } else if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    /*
    Создаём интент который принимает данные из активити и закрывает его
     */
    private fun setMainResult(){
        val i = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, createNewNote())
        }
        setResult(RESULT_OK, i)
        finish()
    }
    /*
    Заполняем наш класс NoteItem
     */
    private fun createNewNote(): NoteItem{
        return NoteItem(
            null,
            binding.edTitle.text.toString(),
            binding.edDescription.text.toString(),
            getCurrentTime(),
            ""
        )
    }

    /*
    Берём текущую дату и время с телефона и возвращаем в виде String
     */
    private fun getCurrentTime(): String{
        val formatter = SimpleDateFormat("hh:mm:ss - dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
    /*
    Подключаем отображение кнопки назад в экшн баре в этом активити
     */
    private fun actionBarSettings(){
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }
}