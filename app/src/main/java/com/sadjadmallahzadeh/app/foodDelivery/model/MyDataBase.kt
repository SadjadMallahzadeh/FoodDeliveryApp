package com.sadjadmallahzadeh.app.foodDelivery.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FoodItem::class], version = 1, exportSchema = false)
abstract class MyDataBase : RoomDatabase() {

    abstract val foodDao: FoodDao

    companion object {

        @Volatile
        private var database: MyDataBase? = null

        fun getDatabase(context: Context): MyDataBase {
            synchronized(this) {
                if (database == null) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        MyDataBase::class.java,
                        "myDatabase.db"
                    ).allowMainThreadQueries().build()

                }
            }

            return database!!
        }
    }

}