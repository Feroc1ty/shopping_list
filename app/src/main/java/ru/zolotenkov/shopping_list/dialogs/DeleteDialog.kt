package ru.zolotenkov.shopping_list.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import ru.zolotenkov.shopping_list.databinding.DeleteDialogBinding
import ru.zolotenkov.shopping_list.databinding.NewListDialogBinding

/*
Создаём AlertDialog из шаблона new_list_dialog
 */
object DeleteDialog {
    fun showDialog(context: Context, listener: Listener){
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = DeleteDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            bDelete.setOnClickListener {
                listener.onClick()
                dialog?.dismiss()
            }
            bCancel.setOnClickListener {
                dialog?.dismiss()
            }
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
        fun onClick(){
        }
    }
}