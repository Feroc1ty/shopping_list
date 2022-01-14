package ru.zolotenkov.shopping_list.utils

import android.content.Intent
import ru.zolotenkov.shopping_list.entities.ShopListItem
import java.lang.StringBuilder

object ShareHelper {

    /*
    Формирует через интент передачу данных через кнопку поделиться и закидывает текст из функции makeShareText в виде string
     */
    fun shareShopList(shopList: List<ShopListItem>, listName: String): Intent{
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareText(shopList, listName))
        }
        return intent
    }

    /*
    Функция формирует расшариваемый текст для кнопки поделиться
     */
    private fun makeShareText(shopList: List<ShopListItem>, listName: String): String {
        val sBuilder = StringBuilder()
        var counter = 0
        sBuilder.append("<<$listName>>")
        sBuilder.append("\n")
        shopList.forEach{
            sBuilder.append("${++counter} - ${it.name} (${it.itemInfo})")
            sBuilder.append("\n")
        }
        return sBuilder.toString()
    }
}