package ru.zolotenkov.shopping_list.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.zolotenkov.shopping_list.R
import ru.zolotenkov.shopping_list.databinding.ListNameItemBinding
import ru.zolotenkov.shopping_list.entities.ShopListNameItem

class ShopListNameAdapter(private val listener: Listener): ListAdapter<ShopListNameItem, ShopListNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }


    class ItemHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = ListNameItemBinding.bind(view)

        fun setData(shopListNameItem: ShopListNameItem, listener: Listener) = with(binding){        //Функция которая заполняет наш шаблон данными из таблицы NoteItem

            tvListName.text = shopListNameItem.name
            tvTime.text = shopListNameItem.time
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