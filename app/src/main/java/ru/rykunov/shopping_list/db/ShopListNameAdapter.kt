package ru.rykunov.shopping_list.db

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.rykunov.shopping_list.R
import ru.rykunov.shopping_list.databinding.ListNameItemBinding
import ru.rykunov.shopping_list.entities.ShopListNameItem
import ru.rykunov.shopping_list.utils.TimeManager

class ShopListNameAdapter(private val listener: Listener, private val defPref: SharedPreferences): ListAdapter<ShopListNameItem, ShopListNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener, defPref)
    }


    class ItemHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = ListNameItemBinding.bind(view)

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun setData(shopListNameItem: ShopListNameItem, listener: Listener, defPref: SharedPreferences) = with(binding){        //Функция которая заполняет наш шаблон данными из таблицы NoteItem

            tvListName.text = shopListNameItem.name
            tvTime.text = shopListNameItem.time
            tvTime.text = TimeManager.getTimeFormat(shopListNameItem.time, defPref)
            pBar.max = shopListNameItem.allItemCounter
            pBar.progress = shopListNameItem.checkedItemsCounter
            /*
            Окрашиваем прогрес бар и счётчик в нужные цвета
             */
            val colorState = ColorStateList.valueOf(getProgressColorState(shopListNameItem, binding.root.context))
            pBar.progressTintList = colorState
            counterCard.backgroundTintList = colorState

            val counterText = "${shopListNameItem.checkedItemsCounter}/${shopListNameItem.allItemCounter}"
            tvCounter.text = counterText
            itemView.setOnClickListener {
                listener.onClickItem(shopListNameItem)
            }
            imDelete.setOnClickListener{
                listener.deleteItem(shopListNameItem.id!!)
            }
            imEdit.setOnClickListener{
                listener.editItem(shopListNameItem)
            }
        }
        /*
        Функция которая возвращает цвет для прогресбара
         */
        private fun getProgressColorState(item: ShopListNameItem, context: Context): Int{
            return if(item.checkedItemsCounter == item.allItemCounter){
                ContextCompat.getColor(context, R.color.green)
            }
            else{
                ContextCompat.getColor(context, R.color.red)
            }
        }

        companion object{
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context).
                    inflate(R.layout.list_name_item, parent, false))      //В этой функции мы создаем нашу карточку и заполняем её
            }
        }
    }
    class ItemComparator : DiffUtil.ItemCallback<ShopListNameItem>(){                               //Класс в котором сравниваются обьекты чтобы DiffUtil знал что обновлять в листе а что уже стоит убрать
        override fun areItemsTheSame(oldItem: ShopListNameItem, newItem: ShopListNameItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopListNameItem, newItem: ShopListNameItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener{
        fun deleteItem(id: Int)
        fun editItem(shopListNameItem: ShopListNameItem)
        fun onClickItem(shopListNameItem: ShopListNameItem)
    }

}