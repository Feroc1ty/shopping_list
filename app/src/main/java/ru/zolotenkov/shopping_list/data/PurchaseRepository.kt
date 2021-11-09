package ru.zolotenkov.shopping_list.data

import androidx.lifecycle.LiveData

class PurchaseRepository(private val purchaseDAO: PurchaseDAO) {

    val readAllData: LiveData<List<Purchase>> = purchaseDAO.readAllData()

    suspend fun addPurchase(purchase: Purchase) {
        purchaseDAO.addPurchase(purchase)
    }

}