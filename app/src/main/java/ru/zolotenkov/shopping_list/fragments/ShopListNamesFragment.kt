package ru.zolotenkov.shopping_list.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import ru.zolotenkov.shopping_list.activities.MainApp
import ru.zolotenkov.shopping_list.databinding.FragmentShopListNamesBinding
import ru.zolotenkov.shopping_list.db.MainViewModel
import ru.zolotenkov.shopping_list.dialogs.NewListDialog


class ShopListNamesFragment : BaseFragment() {
    private lateinit var binding: FragmentShopListNamesBinding

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String){
                Toast.makeText(context, "Name: $name", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
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

    }
    /*
    Функция которая следит за изменениями в базе данных и будет выдавать обновлённый список
     */
    private fun observer(){
        mainViewModel.allNotes.observe(viewLifecycleOwner, {

        })
    }



    companion object {

        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }
}