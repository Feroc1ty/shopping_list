package ru.zolotenkov.shopping_list.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import ru.zolotenkov.shopping_list.R
import ru.zolotenkov.shopping_list.databinding.ActivityNewNoteBinding
import ru.zolotenkov.shopping_list.entities.NoteItem
import ru.zolotenkov.shopping_list.fragments.NoteFragment
import ru.zolotenkov.shopping_list.utils.HtmlManager
import ru.zolotenkov.shopping_list.utils.MyTouchListner
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    private var note: NoteItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBarSettings()
        getNote()
        init()
        onClickColorPicker()
        actionMenuCallback()
    }
    /*
    Слушатель нажатий в color picker и вызов функции setColorForSelectedText
     */
    private fun onClickColorPicker() = with(binding){
        imRed.setOnClickListener {
            setColorForSelectedText(R.color.picker_red) }
        imBlack.setOnClickListener {
            setColorForSelectedText(R.color.picker_black) }
        imBlue.setOnClickListener {
            setColorForSelectedText(R.color.picker_blue) }
        imYellow.setOnClickListener {
            setColorForSelectedText(R.color.picker_yellow)}
        imGreen.setOnClickListener {
            setColorForSelectedText(R.color.picker_green)}
        imOrange.setOnClickListener {
            setColorForSelectedText(R.color.picker_orange)}
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(){
        binding.colorPicker.setOnTouchListener(MyTouchListner())
    }

    private fun getNote(){
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY)
        if(sNote != null) {
            note = sNote as NoteItem
            fillNote()
        }

    }
    /*
    По скольку теперь в тексте могут быть стили перегоняем текст из HTML формата в строку
     */
    private fun fillNote() = with(binding){
        edTitle.setText(note?.title)
        edDescription.setText(HtmlManager.getFromHtml(note?.content!!).trim())

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
        else if (item.itemId == R.id.id_bold) {
            setBoldForSelectedText()
        }
        else if (item.itemId == R.id.id_color) {
            if(binding.colorPicker.isShown){
                closeColorPicker()
            }
            else openColorPicker()
        }
        return super.onOptionsItemSelected(item)
    }
    /*
    Функция которая смотрит какой текст выделен, делает проверку есть ли там стиль, если да то убирает, если нет то создает
     */
    private fun setBoldForSelectedText() = with(binding){
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos,endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if(styles.isNotEmpty()){
            edDescription.text.removeSpan(styles[0])
        }
        else{
            boldStyle = StyleSpan(Typeface.BOLD)
        }
        edDescription.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        /*
        Функция trim удаляет все пробелы их html текста
         */
        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }
        /*
        Функция красит выбранный текст либо удаляет стиль с текста
         */
    private fun setColorForSelectedText(colorId: Int) = with(binding){
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos,endPos, ForegroundColorSpan::class.java)
        if(styles.isNotEmpty()) edDescription.text.removeSpan(styles[0])

        edDescription.text.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(this@NewNoteActivity, colorId)),
            startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        /*
        Функция trim удаляет все пробелы из html текста
         */
        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }
    /*
    Делаем проверку на наличие данных. Если пусто то создаем новую, если есть в базе то апдейтим.
     */
    private fun setMainResult(){
        var editState = "new"
        val tempNote: NoteItem?
        if(note == null){
            tempNote = createNewNote()
        }
        else {
            editState = "update"
            tempNote = updateNote()
        }

        val i = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        }
        setResult(RESULT_OK, i)
        finish()
    }
    /*
    Передаём в заметку данные из базы для редактирования
    Записываем в виде HTML (стили) через HTML менеджер
     */
    private fun updateNote() : NoteItem?  = with(binding){
        return note?.copy(
            title = edTitle.text.toString(),
            content = HtmlManager.toHtml(edDescription.text) )
    }
    /*
    Заполняем наш класс NoteItem
    Записываем в виде HTML (стили) через HTML менеджер
     */
    private fun createNewNote(): NoteItem{
        return NoteItem(
            null,
            binding.edTitle.text.toString(),
            HtmlManager.toHtml(binding.edDescription.text),
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

    private fun openColorPicker(){
        binding.colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        binding.colorPicker.startAnimation(openAnim)
    }
    private fun closeColorPicker(){
        val closeAnim = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        closeAnim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        binding.colorPicker.startAnimation(closeAnim)
    }

    /*
    Пишем колбек который удаляет меню действий которое возникает при выборе текста
    По факту он просто его очищает сразу после вызова
     */
    private fun actionMenuCallback(){
        val actionCallback = object : ActionMode.Callback{
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
            }
        }

        /*
        Присваиваем ранее созданный колбек дефолтному
         */
        binding.edDescription.customSelectionActionModeCallback = actionCallback
    }
}