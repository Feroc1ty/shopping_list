package ru.zolotenkov.shopping_list.activities

import android.app.Application
import ru.zolotenkov.shopping_list.db.MainDatabase

/*
 * Наследуемся от Application чтобы быть на уровне всего приложения.
 */
class MainApp : Application() {
    /*
     * by lazy запускается если у нас в database пусто, база ещё не создана.
     * Он запускает код и запихивает нашу базу в переменную и больше не запускается при следующих обращениях,
     * потому что переменная уже содержит в себе INSTANCE. Только когда в переменной пусто!
     * Каждый раз как только запускается приложение запускается этот код
     * и инициализируется ДБ через функцию getDataBase в MainDatabase. (но нужно задекларировать этот класс в манифесте)
     * Теперь у нас есть доступ в ЛЮБОМ активити к database и мы можем обратиться к ней отовсюду.
     */
    val database by lazy { MainDatabase.getDataBase(this) }
}