package ru.zolotenkov.shopping_list.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PurchaseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPurchase(purchase: Purchase)

    @Query("SELECT * FROM purchase")
    fun readAllData(): LiveData<List<Purchase>>
}