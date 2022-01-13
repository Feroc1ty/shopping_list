package ru.zolotenkov.shopping_list.db

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.zolotenkov.shopping_list.entities.NoteItem
import ru.zolotenkov.shopping_list.entities.ShopListItem
import ru.zolotenkov.shopping_list.entities.ShopListNameItem
import java.lang.IllegalArgumentException

class MainViewModel(database: MainDatabase): ViewModel() {
    val dao = database.getDao()                                             //Инициализирует DAO


    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()     //Получаем как список типа NoteItem все наши заметки из базы
    val allShopListNamesItem: LiveData<List<ShopListNameItem>> = dao.getAllShopListNames().asLiveData()     //Получаем как список типа NoteItem все наши заметки из базы

    fun getAllItemsFromList(listId: Int): LiveData<List<ShopListItem>>{
        return dao.getAllShopListItems(listId).asLiveData()
    }

    fun insertNote(note: NoteItem) = viewModelScope.launch {            //Функция которая через корутину записывает в базу новую заметку
        dao.insertNote(note)
    }

    fun insertShopListName(listNameItem: ShopListNameItem) = viewModelScope.launch {            //Функция которая через корутину записывает в базу новую заметку
        dao.insertShopListName(listNameItem)
    }
    fun insertShopItem(shopListItem: ShopListItem) = viewModelScope.launch {            //Функция которая через корутину записывает в базу новую заметку
        dao.insertItem(shopListItem)
    }

    fun updateListItem(item: ShopListItem) = viewModelScope.launch {            //Функция которая через корутину обновляет список
        dao.updateListItem(item)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {            //Функция которая через корутину обновляет запись в бд
        dao.updateNote(note)
    }
    fun updateListName(shopListNameItem: ShopListNameItem) = viewModelScope.launch {            //Функция которая через корутину обновляет запись в бд
        dao.updateListName(shopListNameItem)
    }
    fun deleteNote(id: Int) = viewModelScope.launch {            //Функция которая через корутину удаляет заметку
        dao.deleteNote(id)
    }
    fun deleteShopListName(id: Int) = viewModelScope.launch {            //Функция которая через корутину удаляет список с покупками
        dao.deleteShopListName(id)
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