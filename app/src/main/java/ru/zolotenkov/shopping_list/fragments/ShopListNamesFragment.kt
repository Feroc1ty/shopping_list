package ru.zolotenkov.shopping_list.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.zolotenkov.shopping_list.activities.MainApp
import ru.zolotenkov.shopping_list.databinding.DeleteDialogBinding
import ru.zolotenkov.shopping_list.databinding.FragmentShopListNamesBinding
import ru.zolotenkov.shopping_list.db.MainViewModel
import ru.zolotenkov.shopping_list.db.ShopListNameAdapter
import ru.zolotenkov.shopping_list.dialogs.DeleteDialog
import ru.zolotenkov.shopping_list.dialogs.NewListDialog
import ru.zolotenkov.shopping_list.entities.NoteItem
import ru.zolotenkov.shopping_list.entities.ShoppingListName
import ru.zolotenkov.shopping_list.utils.TimeManager


class ShopListNamesFragment : BaseFragment(), ShopListNameAdapter.Listener {
    private lateinit var binding: FragmentShopListNamesBinding
    private lateinit var adapter: ShopListNameAdapter

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String){
                val shopListName = ShoppingListName(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    "")
                mainViewModel.insertShopListName(shopListName)
            }
        }, "")
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
        rvView.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListNameAdapter(this@ShopListNamesFragment)
        rvView.adapter = adapter

    }
    /*
    Функция которая следит за изменениями в базе данных и будет выдавать обновлённый список
     */
    private fun observer(){
        mainViewModel.allShopListNames.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }



    companion object {

        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener{
            override fun onClick(){
                mainViewModel.deleteShopListName(id)
            }
        })
    }

    override fun editItem(shopListName: ShoppingListName) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String){

                mainViewModel.updateListName(shopListName.copy(name = name))
            }
        }, name = shopListName.name)
    }

    override fun onClickItem(shopListName: ShoppingListName) {
        TODO("Not yet implemented")
    }
}