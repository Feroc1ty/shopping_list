package ru.zolotenkov.shopping_list.db

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.zolotenkov.shopping_list.R
import ru.zolotenkov.shopping_list.databinding.ListNameItemBinding
import ru.zolotenkov.shopping_list.databinding.ShopListItemBinding
import ru.zolotenkov.shopping_list.entities.ShopListNameItem
import ru.zolotenkov.shopping_list.entities.ShopListItem

class ShopListItemAdapter(private val listener: Listener): ListAdapter<ShopListItem, ShopListItemAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {

        return if(viewType == 0)
                ItemHolder.createShopItem(parent)
            else
                ItemHolder.createLibraryItem(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if(getItem(position).itemType == 0)
            holder.setItemData(getItem(position), listener)
        else
            holder.setLibraryData(getItem(position), listener)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    class ItemHolder(val view: View) : RecyclerView.ViewHolder(view){

/*
Функция которая заполняет наш шаблон данными из списка продуктов для неё свой биндинг из своего шаблона
 */
        fun setItemData(shopListItem: ShopListItem, listener: Listener){
            val binding = ShopListItemBinding.bind(view)
            binding.apply {
                tvName.text = shopListItem.name
                if(shopListItem.itemInfo.isNullOrEmpty())
                    tvInfo.isVisible = false
                else
                    tvInfo.setText(shopListItem.itemInfo)

                /*
                Обновляем в БД отмечен ли итем в списке или нет изменяя в поле isChecked с false на true
                Так же присваиваем статус чекбокса из состояния из базы
                И окрашиваем текст в соотв с стилями
                 */
                checkBox.isChecked = shopListItem.itemChecked
                setPaintFlagAndColor(binding)
                checkBox.setOnClickListener {
                    listener.onClickItem(shopListItem.copy(itemChecked = checkBox.isChecked))
                }
            }
        }
        /*
        Проверяем на нажатие чекбокса, перечёркиваем текст, меняем цвет
         */
        private fun setPaintFlagAndColor(binding: ShopListItemBinding){
            binding.apply {
                if(checkBox.isChecked){
                    tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvInfo.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
                    tvInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
                }
                else{
                    tvName.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvInfo.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                    tvInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                }

            }
        }
/*
Функция которая заполняет наш список с подсказками, для неё свой биндинг
*/
        fun setLibraryData(shopListItem: ShopListItem, listener: Listener){        //Функция которая заполняет наш шаблон c подсказками

        }
        companion object{
            fun createShopItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context).
                    inflate(R.layout.shop_list_item, parent, false))      //В этой функции мы создаем нашу карточку и заполняем её
            }
            fun createLibraryItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context).
                    inflate(R.layout.shop_library_list_item, parent, false))      //В этой функции мы создаем нашу карточку и заполняем её
            }
        }
    }
    class ItemComparator : DiffUtil.ItemCallback<ShopListItem>(){                               //Класс в котором сравниваются обьекты чтобы DiffUtil знал что обновлять в листе а что уже стоит убрать
        override fun areItemsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener{
        fun onClickItem(shopListNameItem: ShopListItem)
    }

}