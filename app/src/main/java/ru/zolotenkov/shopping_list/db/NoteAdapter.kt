package ru.zolotenkov.shopping_list.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.zolotenkov.shopping_list.R
import ru.zolotenkov.shopping_list.databinding.NoteListItemBinding
import ru.zolotenkov.shopping_list.entities.NoteItem
import ru.zolotenkov.shopping_list.utils.HtmlManager

class NoteAdapter(private val listner: Listner): ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listner)
    }


    class ItemHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = NoteListItemBinding.bind(view)

        fun setData(note: NoteItem, listner: Listner) = with(binding){        //Функция которая заполняет наш шаблон данными из таблицы NoteItem

            tvTitle.text = note.title
            tvDescription.text = HtmlManager.getFromHtml(note.content).trim()
            tvTime.text = note.time
            itemView.setOnClickListener {
                listner.onClickItem(note)
            }
            imDelete.setOnClickListener{
                listner.deleteItem(note.id!!)
            }
        }
        companion object{
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false))      //В этой функции мы создаем нашу карточку и заполняем её
            }
        }
    }
    class ItemComparator : DiffUtil.ItemCallback<NoteItem>(){                               //Класс в котором сравниваются обьекты чтобы DiffUtil знал что обновлять в листе а что уже стоит убрать
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Listner{
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }

}