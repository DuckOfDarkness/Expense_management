package pja.edu.pl.expense_management.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pja.edu.pl.expense_management.data.model.ItemEntity

@Database(
    entities = [ItemEntity::class],
    version = 5
)

abstract class  ItemDatabase : RoomDatabase(){
    abstract val items: ItemDao

    companion object{
        fun open(context: Context): ItemDatabase = Room.databaseBuilder(
            context, ItemDatabase::class.java, "items5.db"
        ).build()
    }
}