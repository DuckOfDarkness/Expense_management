package pja.edu.pl.expense_management.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pja.edu.pl.expense_management.data.model.ItemEntity

@Dao
interface ItemDao {

    @Query( "SELECT * FROM ItemEntity" )
    fun getAll(): List<ItemEntity>

    @Query("SELECT name FROM ItemEntity")
    fun getNames(): List<String>

    @Query ("SELECT * FROM ItemEntity WHERE id = :id")
    fun getSelected(id: Int): ItemEntity

    @Query( "SELECT * FROM ItemEntity ORDER BY name" )
    fun getAllSortedByName(): List<ItemEntity>

    @Query( "SELECT * FROM ItemEntity ORDER BY price" )
    fun getAllSortedByPrice(): List<ItemEntity>

    @Query( "SELECT id FROM ItemEntity")
    fun selectAllId(): List<Int>

    @Query("SELECT SUM(price * amount) FROM ItemEntity")
    fun selectTotalPrice(): String


    @Insert
    fun addItem(newItem: ItemEntity)

    @Update
    fun updateItem(newItem: ItemEntity)

    @Query("DELETE FROM ItemEntity WHERE id =:idItem")
    fun deleteItem(idItem: Int)
}