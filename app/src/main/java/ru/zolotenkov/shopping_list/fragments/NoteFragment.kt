package ru.zolotenkov.shopping_list.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.zolotenkov.shopping_list.activities.MainApp
import ru.zolotenkov.shopping_list.activities.NewNoteActivity
import ru.zolotenkov.shopping_list.databinding.FragmentNoteBinding
import ru.zolotenkov.shopping_list.db.MainViewModel
import ru.zolotenkov.shopping_list.db.NoteAdapter
import ru.zolotenkov.shopping_list.entities.NoteItem


class NoteFragment : BaseFragment(), NoteAdapter.Listner {
    private lateinit var binding: FragmentNoteBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: NoteAdapter

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }
    /*
    Дефолтная функция которая запускается когда все view уже созданы.
    В ней мы можем инициализировать наш RecyclerView
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }
    /*
    Функция для инициализации Recycler View
     */
    private fun initRcView() = with(binding) {
        rcViewNote.layoutManager = LinearLayoutManager(activity)
        adapter = NoteAdapter(this@NoteFragment)
        rcViewNote.adapter = adapter
    }
    /*
    Функция которая следит за изменениями в базе данных и будет выдавать обновлённый список
     */
    private fun observer(){
        mainViewModel.allNotes.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun onEditResult(){
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){
                mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
            }
        }
    }

    companion object {
        const val NEW_NOTE_KEY = "new_note_key"
        @JvmStatic
        fun newInstance() = NoteFragment()
    }

    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)
    }
}