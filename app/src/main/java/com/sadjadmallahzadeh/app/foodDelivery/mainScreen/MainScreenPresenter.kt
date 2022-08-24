package com.sadjadmallahzadeh.app.foodDelivery.mainScreen

import android.text.Editable
import com.sadjadmallahzadeh.app.foodDelivery.model.FoodDao
import com.sadjadmallahzadeh.app.foodDelivery.model.FoodItem
import com.sadjadmallahzadeh.app.foodDelivery.model.InitFoodList

class MainScreenPresenter(private val foodDao: FoodDao) : MainScreenContract.Presenter {
    var mainView: MainScreenContract.View? = null
    var isAdmin: Boolean = false


    override fun onAttach(view: MainScreenContract.View) {
        mainView = view
        val data = foodDao.getAllFoodItems()
        mainView!!.showFoods(data, null)

    }

    override fun onDetach() {
        mainView = null
    }

    override fun onDeleteFood(oldFoodItem: FoodItem, position: Int) {

        foodDao.deleteFoodItem(oldFoodItem)
        mainView!!.deleteFoodItem(oldFoodItem, position)

    }

    override fun onAddFood() {
        mainView!!.addFoodItem()
    }

    override fun OnInsertFoodItem(newFoodItem: FoodItem) {
        foodDao.insertFoodItem(newFoodItem)
    }

    override fun onUpdateFood(updatedFoodItem: FoodItem, position: Int) {

        foodDao.updateFoodItem(updatedFoodItem)
        mainView!!.updateFoodItem(updatedFoodItem, position)

    }

    override fun onSearchFooditems(editTextSearch: Editable?) {
        mainView!!.searchFoodItems(editTextSearch)
    }

    override fun onFirstRun(username: String?) {
          foodDao.insertAllFoodItems(InitFoodList.getInitFoodlist())
          
        //it is weird but as long as we have not call the fooddao.get method we can not save or update the new dataset in that food dao
        // to verify that we can just uncomment and comment two next line respectively!
   
        //        mainView!!.showFoods(InitFoodList.getInitFoodlist(), username)
        mainView!!.showFoods(foodDao.getAllFoodItems(), username)

    }

    override fun onGotoBasketShop() {
        mainView!!.gotoBasketShop()
    }

}
