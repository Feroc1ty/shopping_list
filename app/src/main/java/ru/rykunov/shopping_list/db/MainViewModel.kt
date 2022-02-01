package ru.rykunov.shopping_list.db

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.rykunov.shopping_list.entities.LibraryItem
import ru.rykunov.shopping_list.entities.NoteItem
import ru.rykunov.shopping_list.entities.ShopListItem
import ru.rykunov.shopping_list.entities.ShopListNameItem
import java.lang.IllegalArgumentException

class MainViewModel(database: MainDatabase): ViewModel() {
    val dao = database.getDao()                                             //Инициализирует DAO
    val libraryItems = MutableLiveData<List<LibraryItem>>()

    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()     //Получаем как список типа NoteItem все наши заметки из базы
    val allShopListNamesItem: LiveData<List<ShopListNameItem>> = dao.getAllShopListNames().asLiveData()     //Получаем как список типа NoteItem все наши заметки из базы

    fun getAllItemsFromList(listId: Int): LiveData<List<ShopListItem>>{
        return dao.getAllShopListItems(listId).asLiveData()
    }
    fun getAllLibraryItems(name: String) = viewModelScope.launch {
        libraryItems.postValue(dao.getAllLibraryItems(name))

        }

    fun insertNote(note: NoteItem) = viewModelScope.launch {            //Функция которая через корутину записывает в базу новую заметку
        dao.insertNote(note)
    }


    fun insertShopListName(listNameItem: ShopListNameItem) = viewModelScope.launch {            //Функция которая через корутину записывает в базу новую заметку
        dao.insertShopListName(listNameItem)
    }
    fun insertShopItem(shopListItem: ShopListItem) = viewModelScope.launch {            //Функция которая через корутину записывает в библиотеку имена
        dao.insertItem(shopListItem)
        if(!isLibraryItemExists(shopListItem.name)) {
            dao.insertLibraryItem(LibraryItem(null, shopListItem.name))
        }
    }

    fun updateListItem(item: ShopListItem) = viewModelScope.launch {            //Функция которая через корутину обновляет список
        dao.updateListItem(item)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {            //Функция которая через корутину обновляет запись в бд
        dao.updateNote(note)
    }

    fun updateLibraryItem(item: LibraryItem) = viewModelScope.launch {            //Функция которая через корутину обновляет запись в библиотеке сохранённых итемов
        dao.updateLibraryItem(item)
    }
    fun updateListName(shopListNameItem: ShopListNameItem) = viewModelScope.launch {            //Функция которая через корутину обновляет запись в бд
        dao.updateListName(shopListNameItem)
    }
    fun deleteNote(id: Int) = viewModelScope.launch {            //Функция которая через корутину удаляет заметку
        dao.deleteNote(id)
    }
    fun deleteLibraryItem(id: Int) = viewModelScope.launch {            //Функция которая через корутину удаляет подсказку из библиотеки
        dao.deleteLibraryItem(id)
    }
    fun deleteShopList(id: Int, deleteList: Boolean) = viewModelScope.launch {            //Функция которая через корутину удаляет список с покупками
        if(deleteList) dao.deleteShopListName(id)
        dao.deleteShopItemsByListId(id)
    }
    private suspend fun isLibraryItemExists(name: String): Boolean{
        return dao.getAllLibraryItems(name).isNotEmpty()
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