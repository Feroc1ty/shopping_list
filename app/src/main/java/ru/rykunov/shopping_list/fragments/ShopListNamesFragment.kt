package ru.rykunov.shopping_list.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import ru.rykunov.shopping_list.activities.MainApp
import ru.rykunov.shopping_list.activities.ShopListActivity
import ru.rykunov.shopping_list.databinding.FragmentShopListNamesBinding
import ru.rykunov.shopping_list.db.MainViewModel
import ru.rykunov.shopping_list.db.ShopListNameAdapter
import ru.rykunov.shopping_list.dialogs.DeleteDialog
import ru.rykunov.shopping_list.dialogs.NewListDialog
import ru.rykunov.shopping_list.entities.ShopListNameItem
import ru.rykunov.shopping_list.utils.TimeManager


class ShopListNamesFragment : BaseFragment(), ShopListNameAdapter.Listener {
    private lateinit var binding: FragmentShopListNamesBinding
    private lateinit var adapter: ShopListNameAdapter
    private lateinit var defPref: SharedPreferences

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String){
                val shopListName = ShopListNameItem(
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
        defPref = PreferenceManager.getDefaultSharedPreferences(activity)
        rvView.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListNameAdapter(this@ShopListNamesFragment, defPref)
        rvView.adapter = adapter

    }
    /*
    Функция которая следит за изменениями в базе данных и будет выдавать обновлённый список
     */
    private fun observer(){
        mainViewModel.allShopListNamesItem.observe(viewLifecycleOwner, {
            binding.tvEmpty.visibility =
                if(it.isEmpty())
                    View.VISIBLE
                else
                    View.GONE
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
                mainViewModel.deleteShopList(id, true)
            }
        })
    }

    override fun editItem(shopListNameItem: ShopListNameItem) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String){

                mainViewModel.updateListName(shopListNameItem.copy(name = name))
            }
        }, name = shopListNameItem.name)
    }

    override fun onClickItem(shopListNameItem: ShopListNameItem) {
        val i = Intent(activity, ShopListActivity:: class.java).apply {
            putExtra(ShopListActivity.SHOP_LIST_NAME, shopListNameItem)
        }
        startActivity(i)
    }
}