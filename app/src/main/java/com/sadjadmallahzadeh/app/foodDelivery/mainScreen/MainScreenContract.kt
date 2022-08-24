package com.sadjadmallahzadeh.app.foodDelivery.mainScreen

import android.text.Editable
import com.sadjadmallahzadeh.app.foodDelivery.model.FoodItem

interface MainScreenContract {

    interface Presenter {

        fun onAttach(view: MainScreenContract.View)
        fun onDetach()
        fun onDeleteFood(oldFoodItem: FoodItem, position: Int)
        fun onAddFood()
        fun OnInsertFoodItem(newFoodItem: FoodItem)
        fun onUpdateFood(updatedFoodItem: FoodItem, position: Int)
        fun onSearchFooditems(editTextSearch: Editable?)
        fun onFirstRun(userName: String?)
        fun onGotoBasketShop()


    }

    interface View {
        fun showFoods(dataBasefoodList: List<FoodItem>, userName: String?)
        fun addFoodItem()
        fun deleteFoodItem(oldFoodItem: FoodItem, position: Int)
        fun updateFoodItem(updatedFoodItem: FoodItem, position: Int)
        fun searchFoodItems(editTextSearch: Editable?)
        fun gotoBasketShop()

    }


}