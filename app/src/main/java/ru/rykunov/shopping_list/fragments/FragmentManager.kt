package ru.rykunov.shopping_list.fragments

import androidx.appcompat.app.AppCompatActivity
import ru.rykunov.shopping_list.R

//тут обьект. Чтобы у нас была функция которую мы можем использовать без инициализации этого класса.
object FragmentManager {
    var currentFrag: BaseFragment? = null               //Здесь сохраняем какой именно фрагмент сейчас запущен.

    fun setFragment(newFrag: BaseFragment, activity: AppCompatActivity){        //Сюда передаём новый фрагмент который мы хотим поместить в активити и собственно контекст, куда.
        val transaction = activity.supportFragmentManager.beginTransaction()    //С помощью функции transaction мы можем менять/удалять/устанавливать фрагменты
        transaction.replace(R.id.placeHolder, newFrag)                          //Сначала указываем где заменяем (в frame holder'e) и указываем что туда положим (newFrag - фрагмент который передали)
        transaction.commit()                                                    //Применяем действия которые указали выше.
        currentFrag = newFrag                                                   //И записываем в переменную currentFrag наш фрагмент который только что установили, чтобы знать какой именно фрагмент сейчас запущен.
    }

}