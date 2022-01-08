package ru.zolotenkov.shopping_list.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.zolotenkov.shopping_list.entities.NoteItem
import ru.zolotenkov.shopping_list.entities.ShoppingListName

// Класс DAO для доступа к базе данных. Тут описываются действия которые мы будем делать в БД, запросы. Для считывания или записи данных.

@Dao
interface Dao {                                            //Dao должен быть интерфейсом
    @Query ("SELECT * FROM note_list")                 //Получаем все заметки из бд note_list
    fun getAllNotes(): Flow<List<NoteItem>>                 //Flow элемент корутин. Этот поток каждый раз берёт данные из БД NoteItem из запроса и запихивает их в список. Список обновляется постоянно при наличии изменений. Нам эту логику прописывать не нужно когда есть Flow.

    @Query ("SELECT * FROM shopping_list_names")                 //Получаем список всех шоп листов из бд shopping_list_names
    fun getAllShopListNames(): Flow<List<ShoppingListName>>                 //Flow элемент корутин. Этот поток каждый раз берёт данные из БД NoteItem из запроса и запихивает их в список. Список обновляется постоянно при наличии изменений. Нам эту логику прописывать не нужно когда есть Flow.

    @Query ("DELETE FROM note_list WHERE id IS :id")            //Удаляем из базы заметку по ID
    suspend fun deleteNote(id: Int)

    @Query ("DELETE FROM shopping_list_names WHERE id IS :id")            //Удаляем из базы список
    suspend fun deleteShopListName(id: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)                //обязательно suspend потому что эти функции мы будем запускать внутри корутин. Внутри корутин потому что выполнение этих функций может занять некоторое время.

    @Insert
    suspend fun insertShopListName(name: ShoppingListName)                //обязательно suspend потому что эти функции мы будем запускать внутри корутин. Внутри корутин потому что выполнение этих функций может занять некоторое время.

    @Update
    suspend fun updateNote(note: NoteItem)                //обязательно suspend потому что эти функции мы будем запускать внутри корутин. Внутри корутин потому что выполнение этих функций может занять некоторое время.

    @Update
    suspend fun updateListName(shopListName: ShoppingListName)                //обязательно suspend потому что эти функции мы будем запускать внутри корутин. Внутри корутин потому что выполнение этих функций может занять некоторое время.


}