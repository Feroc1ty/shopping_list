package ru.zolotenkov.shopping_list.db

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.zolotenkov.shopping_list.entities.NoteItem
import java.lang.IllegalArgumentException

class MainViewModel(database: MainDatabase): ViewModel() {
    val dao = database.getDao()                                             //Инициализирует DAO


    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()     //Получаем как список типа NoteItem все наши заметки из базы

    fun insertNote(note: NoteItem) = viewModelScope.launch {            //Функция которая через корутину записывает в базу новую заметку
        dao.insertNote(note)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {            //Функция которая через корутину удаляет заметку
        dao.deleteNote(id)
    }

    class MainViewModelFactory(val database: MainDatabase): ViewModelProvider.Factory{      //Нужен для инициализации класса MainViewModel чтобы на прямую не пользоваться им.
        override fun <T : ViewModel> create(modelClass: Class<T>): T {                      //Инициализируем через ViewModelProvider.Factory
            if (modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }

    }
}