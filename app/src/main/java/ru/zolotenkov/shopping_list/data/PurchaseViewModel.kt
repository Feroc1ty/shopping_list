package ru.zolotenkov.shopping_list.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class PurchaseViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<Purchase>>
    private val repository: PurchaseRepository

    init {
        val purchaseDao = PurchaseDatabase.getDatabase(application).purchaseDao()
        repository = PurchaseRepository(purchaseDao)
        readAllData = repository.readAllData
    }

    suspend fun addPurchase(purchase: Purchase){
        repository.addPurchase(purchase)
    }
}