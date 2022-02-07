package ru.rykunov.shopping_list.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.rykunov.shopping_list.entities.LibraryItem
import ru.rykunov.shopping_list.entities.NoteItem
import ru.rykunov.shopping_list.entities.ShopListItem
import ru.rykunov.shopping_list.entities.ShopListNameItem
import java.util.*

@Database (entities = [LibraryItem::class,      //Указываем что это основной класс для БД. Что это и есть БД. Room создаст базу с нашим названием и создаст все таблицы которые я создал.
    NoteItem::class,                        //В entities =  перечисляем все таблицы которые будут в базе данных.
    ShopListItem::class,
    ShopListNameItem::class],
    version = 1)                        //Версия нужна для миграции. Если потом вдруг в обнове я в базу добавлю какие то новые столбцы в базу, то чтобы они обновились без потери данных нужно указывать версию.

abstract class MainDatabase : RoomDatabase() {

    abstract fun getDao(): Dao      //Через класс MainDatabase который инициализируется в MainApp делаем общедоступной эту функцию. Если нужно что то считать или записать используем эту функцию.


    companion object {
        //Volatile работает в потоке не сохраняя данные переменной в кеш и будет атомарно читаться и записываться. Доступен во всех потоках!
        @Volatile
        private var INSTANCE: MainDatabase? = null

            /*
        val library_items = "INSERT INTO library (id, name) VALUES (null, 'Морковь'), " +
                "(null, 'Картофель')"
             */

        /*
        fun getInstance(context: Context): MainDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: getDataBase(context).also { INSTANCE = it }
            }
         */

        fun getDataBase(context: Context): MainDatabase {
            return INSTANCE ?: synchronized(this) {                   //Если три потока работают с нашей инстанцией/cоздают его, synchronized блокирует их одновременную работу. Если один поток уже осздает базу то synchronized не даст другим сделать тоже самое.
                    val instance = Room.databaseBuilder(                //Элвис оператор указывает что делать если инстанция ещё не создана и нашей БД ещё нет. В переменную instance создаем нашу БД.
                            context.applicationContext,                     //Указываем контекст. Контекстом будет все наше приложение чтобы мы могли использовать эту инстанцию в любом активити.
                            MainDatabase::class.java,                       //Передаём назваание нашего класса.
                            "shopping_list.db"
                        )
                        /*
                        .addCallback(CALLBACK_FOR_LIBRARY_DB)
                         */
                            /* Колбек для заполнения базы из списка
                            .addCallback(
                                object : Callback() {
                                    override fun onCreate(db: SupportSQLiteDatabase) {
                                        super.onCreate(db)
                                        ioThread {
                                            getInstance(context).getDao().insertNewLibrary(PREPOPULATE_DATA)
                                        }
                                    }
                                })
                             */
                            .createFromAsset(getLocaleForLibraryDb())
                            .build()                 //Указываем название нашей базы данных. Делаем build() чтобы уже создать её.
                    instance                                         //return ждёт возвращаемой инстанции. Вызываем instance чтобы её создать и вернуть через элвис оператор.

                }                                                 //В первый раз INSTANCE будет null по этому запустится весь этот код. Во второй раз когда вызовется INSTANCE эта переменная уже будет заполнена по этому вернет нормально INSTANCE.
        }
        private fun getLocaleForLibraryDb(): String{
            return if (Locale.getDefault().getDisplayLanguage().equals("русский") || Locale.getDefault().getDisplayLanguage().equals("українська")){
                "database/library_ru.db"
            }
            else "database/library.db"
        }
        /*
        Колбек для заполнения базы из sql запроса
        private val CALLBACK_FOR_LIBRARY_DB = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL(LibraryItemsOnCreateDB.library_items)
            }
        }
        */
        }
    }