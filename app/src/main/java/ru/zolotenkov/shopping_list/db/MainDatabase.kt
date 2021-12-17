package ru.zolotenkov.shopping_list.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.zolotenkov.shopping_list.entities.LibraryItem
import ru.zolotenkov.shopping_list.entities.NoteItem
import ru.zolotenkov.shopping_list.entities.ShoppingListItem
import ru.zolotenkov.shopping_list.entities.ShoppingListNames

@Database (entities = [LibraryItem::class,      //Указываем что это основной класс для БД. Что это и есть БД. Room создаст базу с нашим названием и создаст все таблицы которые я создал.
    NoteItem::class,                        //В entities =  перечисляем все таблицы которые будут в базе данных.
    ShoppingListItem::class,
    ShoppingListNames::class],
    version = 1)                        //Версия нужна для миграции. Если потом вдруг в обнове я в базу добавлю какие то новые столбцы в базу, то чтобы они обновились без потери данных нужно указывать версию.

abstract class MainDatabase : RoomDatabase() {
    companion object{
        //Volatile работает в потоке не сохраняя данные переменной в кеш и будет атомарно читаться и записываться. Доступен во всех потоках!
        @Volatile
        private var INSTANCE: MainDatabase? = null
        fun getDataBase(context: Context): MainDatabase{
            return INSTANCE ?: synchronized(this) {                   //Если три потока работают с нашей инстанцией создают его, synchronized блокирует их одновременную работу. Если один поток уже осздает базу то synchronized не даст другим сделать тоже самое.
            val instance = Room.databaseBuilder(                //Элвис оператор указывает что делать если инстанция ещё не создана и нашей БД ещё нет. В переменную instance создаем нашу БД.
                context.applicationContext,                     //Указываем контекст. Контекстом будет все наше приложение чтобы мы могли использовать эту инстанцию в любом активити.
                MainDatabase::class.java,                       //Передаём назваание нашего класса.
                "shopping_list.db").build()                 //Указываем название нашей базы данных. Делаем build() чтобы уже создать её.
                instance                                         //return ждёт возвращаемой инстанции. Вызываем instance чтобы её создать и вернуть через элвис оператор.
            }                                                 //В первый раз INSTANCE будет null по этому запустится весь этот код. Во второй раз когда вызовется INSTANCE эта переменная уже будет заполнена по этому вернет нормально INSTANCE.
        }
    }
}