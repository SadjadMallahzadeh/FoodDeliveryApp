package com.sadjadmallahzadeh.app.foodDelivery.model

import androidx.room.*

@Dao
interface FoodDao {

    @Insert
    fun insertFoodItem(foodItem: FoodItem)

    @Insert
    fun insertAllFoodItems(foodList:ArrayList<FoodItem>)

    @Update
    fun updateFoodItem(food: FoodItem)

    @Query("SELECT * FROM Table_Food")
    fun getAllFoodItems():List<FoodItem>

    @Delete
    fun deleteFoodItem(food: FoodItem)

    @Query("DELETE FROM TABLE_FOOD")
    fun deleteAllFoodItems()


    @Query("SELECT * FROM Table_Food WHERE txtFoodName LIKE '%' || :searchedString || '%'")
    fun SearchFoodItems(searchedString:String):List<FoodItem>

}