package ru.zolotenkov.shopping_list.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeManager {
    /*
Берём текущую дату и время с телефона и возвращаем в виде String
 */
    fun getCurrentTime(): String{
        val formatter = SimpleDateFormat("hh:mm:ss - dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
}