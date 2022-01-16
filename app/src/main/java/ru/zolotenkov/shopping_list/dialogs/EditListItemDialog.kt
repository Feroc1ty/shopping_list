package ru.zolotenkov.shopping_list.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import ru.zolotenkov.shopping_list.R
import ru.zolotenkov.shopping_list.databinding.EditListItemDialogBinding
import ru.zolotenkov.shopping_list.databinding.NewListDialogBinding
import ru.zolotenkov.shopping_list.entities.ShopListItem


object EditListItemDialog {
    fun showDialog(context: Context, item: ShopListItem, listener: Listener){
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = EditListItemDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            edName.setText(item.name)
            edInfo.setText(item.itemInfo)
                /*
                Прячем элемент с инфо если тип итема 1 т.е библиотека
                 */
            if(item.itemType == 1) edInfo.visibility = View.GONE
            bUpdate.setOnClickListener {
                if(edName.text.toString().isNotEmpty()){
                    listener.onClick(item.copy(name = edName.text.toString(), itemInfo = edInfo.text.toString()))
                }
                dialog?.dismiss()
            }


                dialog?.dismiss()
            }

        dialog = builder.create()
        /*
        setBackgroundDrawable - убираем фон положив туда null
         */
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }
    /*
    слушатель нажатий. передаём текст из поля для последующей реализации записи
     */
    interface Listener{
        fun onClick(item: ShopListItem){
        }
    }
}