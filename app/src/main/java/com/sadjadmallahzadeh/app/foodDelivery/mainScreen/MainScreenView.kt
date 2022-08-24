package com.sadjadmallahzadeh.app.foodDelivery.mainScreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sadjadmallahzadeh.app.foodDelivery.R
import com.sadjadmallahzadeh.app.foodDelivery.databinding.*
import com.sadjadmallahzadeh.app.foodDelivery.model.FoodDao
import com.sadjadmallahzadeh.app.foodDelivery.model.FoodItem
import com.sadjadmallahzadeh.app.foodDelivery.model.MyDataBase
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainScreenView : AppCompatActivity(), FoodMenuAdapter.FoodEvents, MainScreenContract.View {

    lateinit var binding: ActivityMainBinding
    lateinit var filteredFoodList: ArrayList<FoodItem>
    lateinit var foodDao: FoodDao
    lateinit var mainScreenPresenter: MainScreenPresenter
    lateinit var myAdapter: FoodMenuAdapter
    lateinit var foodList: ArrayList<FoodItem>
    lateinit var foodList2: ArrayList<FoodItem>
    lateinit var paymentAdapter: FoodMenuAdapter
    lateinit var sharedPreferences: SharedPreferences


    var dialogueBinding: DialogueShopBasketBinding? = null
    var shopItemList = ArrayList<FoodItem>()
    var shopItemList2 = ArrayList<FoodItem>().clone() as ArrayList<FoodItem>
    var orderedItemIndexes = arrayListOf<Int>()
    var selectedItemsIndexes = arrayListOf(0)
    var totalPrice: Float = 0f
    var isInBasketShop: Boolean = false
    var isAdmin: Boolean = false


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodDao = MyDataBase.getDatabase(this).foodDao
        mainScreenPresenter = MainScreenPresenter(foodDao)
        sharedPreferences = getSharedPreferences("SharedData", Context.MODE_PRIVATE)

        mainScreenPresenter.onAttach(this)

        paymentAdapter =
            FoodMenuAdapter(shopItemList, shopItemList2, this, FoodMenuAdapter.IS_NOT_MAIN_ADAPTER)

        for (i in 0 until foodList.size) {
            if (foodList[i].orderQuantity!! > 0) {
                shopItemList.add(foodList[i])
                orderedItemIndexes.add(i)
            }
        }
        paymentAdapter.setAdaptOrderedItemInd(orderedItemIndexes)
        myAdapter.setAdaptOrderedItemInd(orderedItemIndexes)

        totalPrice = sharedPreferences.getFloat("TotalPrice", 0f)
        if (sharedPreferences.getBoolean("SharedData", true)) {

            val loginDialogue: AlertDialog = AlertDialog.Builder(this).create()
            val loginDialogueBinding = DialogueLoginBinding.inflate(layoutInflater)

            loginDialogue.setView(loginDialogueBinding.root)
            loginDialogue.setCancelable(true)
            loginDialogue.show()


            loginDialogueBinding.dialogueBtnLogin.setOnClickListener {
                val txtUserName = loginDialogueBinding.username.text.toString()
                mainScreenPresenter.onFirstRun(txtUserName)
                sharedPreferences.edit().putBoolean("isAdmin", isAdmin).apply()
                if (isAdmin) {
                    binding.mainScreenTitle.text = "Admin Panel"
                    binding.btnGotoBasketShop.visibility = View.GONE
                }
                loginDialogue.dismiss()

            }

            sharedPreferences.edit().putBoolean("SharedData", false).apply()
        } else {
            isAdmin = sharedPreferences.getBoolean("isAdmin", false)

            if (isAdmin) {
                binding.myImageView.visibility = View.GONE
                binding.mainLinearLayout.visibility = View.VISIBLE
                binding.btnAddFoodItem.visibility = View.VISIBLE
                "Admin Panel".also { binding.mainScreenTitle.text = it }

            } else {
                binding.myImageView.visibility = View.GONE
                binding.mainLinearLayout.visibility = View.VISIBLE
                binding.btnAddFoodItem.visibility = View.GONE
            }
        }




        isAdmin = sharedPreferences.getBoolean("isAdmin", false)
        if (!isAdmin) {
            binding.btnAddFoodItem.visibility = View.GONE
        } else {
            binding.btnGotoBasketShop.visibility = View.GONE
        }

        binding.btnAddFoodItem.setOnClickListener {
            mainScreenPresenter.onAddFood()
        }

        binding.btnGotoBasketShop.setOnClickListener {

            mainScreenPresenter.onGotoBasketShop()
        }

        filteredFoodList = foodList.clone() as ArrayList<FoodItem>

        binding.editTextSearch.addTextChangedListener { editTextSearch ->
            mainScreenPresenter.onSearchFooditems(editTextSearch)
        }


    }


    @SuppressLint("ResourceAsColor")
    override fun gotoBasketShop() {
        if (shopItemList.size > 0) {
            var dialogue: AlertDialog? = AlertDialog.Builder(this).create()
            dialogueBinding = DialogueShopBasketBinding.inflate(layoutInflater)

            dialogue!!.setView(dialogueBinding!!.root)
            dialogue.setCancelable(true)

            paymentAdapter.setIsInBasketShop(true)
            isInBasketShop = true
            dialogueBinding!!.BasketShopRecycler.adapter = paymentAdapter
            dialogueBinding!!.BasketShopRecycler.layoutManager =
                LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            paymentAdapter.notifyDataSetChanged()
            totalPrice = 0f
            var deliveryTime: Int = 0
            for (i in 0 until shopItemList.size) {
                val numberFormat = NumberFormat.getInstance(Locale.getDefault())
                val parsedStringNumber = numberFormat.parse(shopItemList[i].txtFoodprice)
                if (shopItemList[i].txtDistance.toInt() > deliveryTime) {
                    deliveryTime = shopItemList[i].txtDistance.toInt()
                }
                totalPrice += (shopItemList[i].orderQuantity)!!.toFloat() * parsedStringNumber.toFloat()
            }
            dialogueBinding!!.dialogueBtnConfirm.text = "PROCEED $totalPrice €"
            dialogue.show()

            dialogueBinding!!.dialogueBtnConfirm.setOnClickListener {
                Toast.makeText(
                    this, "your order will be delivered in $deliveryTime Minutes",
                    Toast.LENGTH_LONG
                ).show()
                dialogue!!.cancel()

            }
            dialogueBinding!!.dialogueBtnCancel.setOnClickListener {
                dialogue!!.cancel()
            }


            dialogue.setOnCancelListener {
                paymentAdapter.setIsInBasketShop(false)
                isInBasketShop = false
                dialogue = null
                dialogueBinding = null
            }

        } else {
            Toast.makeText(
                this, "You have not placed any order yet!",
                Toast.LENGTH_LONG
            ).show()
        }


    }

    override fun onFoodItemClicked(food: FoodItem, position: Int) {

        if (isAdmin) {
            val dialogueEdit = AlertDialog.Builder(this).create()
            val dialogueEditBinding = DialogueEditFoodItemBinding.inflate(layoutInflater)

            dialogueEditBinding.dialogueEdtFoodName.setText(food.txtFoodName)
            dialogueEditBinding.dialogueEdtFoodType.setText(food.txtFoodType)
            dialogueEditBinding.dialogueEdtFoodPrice.setText(food.txtFoodprice)
            dialogueEditBinding.dialogueEdtFoodDistance.setText(food.txtDistance)


            dialogueEdit.setView(dialogueEditBinding.root)
            dialogueEdit.setCancelable(true)
            dialogueEdit.show()





            dialogueEditBinding.dialogueBtnDone.setOnClickListener {

                var txtFoodName: String = food.txtFoodName
                var txtFoodType: String = food.txtFoodType
                var txtFoodprice: String = food.txtFoodprice
                var txtFoodDistance: String = food.txtDistance

                if (dialogueEditBinding.dialogueEdtFoodName.length() > 0) {
                    txtFoodName = dialogueEditBinding.dialogueEdtFoodName.text.toString()
                }
                if (dialogueEditBinding.dialogueEdtFoodType.length() > 0) {
                    txtFoodType = dialogueEditBinding.dialogueEdtFoodType.text.toString()
                }
                if (dialogueEditBinding.dialogueEdtFoodPrice.length() > 0) {
                    txtFoodprice = dialogueEditBinding.dialogueEdtFoodPrice.text.toString()
                }
                if (dialogueEditBinding.dialogueEdtFoodDistance.length() > 0) {
                    txtFoodDistance = dialogueEditBinding.dialogueEdtFoodDistance.text.toString()
                }

                val updatedFood = FoodItem(
                    food.id, txtFoodName, txtFoodType, txtFoodprice, txtFoodDistance,
                    food.urlImage, food.numOfRating, food.rating, food.orderQuantity
                )

                mainScreenPresenter.onUpdateFood(updatedFood, position)

                dialogueEdit.dismiss()

            }
            dialogueEditBinding.dialogueBtnEditcancel.setOnClickListener {
                dialogueEdit.dismiss()
            }
        }


    }

    override fun OnFoodItemLongClicked(food: FoodItem, position: Int) {

        if (isAdmin) {
            val dialogueDelete = AlertDialog.Builder(this).create()
            val dialogueDeleteBinding = DialogueDeleteFoodItemBinding.inflate(layoutInflater)
            dialogueDelete.setView(dialogueDeleteBinding.root)
            dialogueDelete.setCancelable(true)
            dialogueDelete.show()

            dialogueDeleteBinding.dialogueBtnCancel.setOnClickListener {
                dialogueDelete.dismiss()
            }
            dialogueDeleteBinding.dialogueBtnConfirm.setOnClickListener {
                mainScreenPresenter.onDeleteFood(food, position)
                dialogueDelete.dismiss()
            }
        }

    }

    override fun onIncreaseOrderQuantity(food: FoodItem, position: Int) {


        if (!isInBasketShop) {
            var isAlreadyOrdered: Boolean = false

            for (i in 0 until shopItemList.size) {
                if (shopItemList[i] == food) {
                    isAlreadyOrdered = true
                    food.orderQuantity = food.orderQuantity!! + 1
                    paymentAdapter.updateFoodItem(food, i)
                    mainScreenPresenter.onUpdateFood(food, position)
                }
            }

            if (!isAlreadyOrdered) {
                food.orderQuantity = food.orderQuantity!! + 1
                mainScreenPresenter.onUpdateFood(food, position)
                paymentAdapter.addFoodItem(food)
                orderedItemIndexes.add(0, position)
                paymentAdapter.setAdaptOrderedItemInd(orderedItemIndexes)
                myAdapter.setAdaptOrderedItemInd(orderedItemIndexes)
            }


        } else if (isInBasketShop) {
            for (i in 0 until shopItemList.size) {
                if (shopItemList[i] == food) {

                    food.orderQuantity = food.orderQuantity!! + 1
                    paymentAdapter.updateFoodItem(food, i)
                    mainScreenPresenter.onUpdateFood(food, orderedItemIndexes[position])
                }
            }

        }

        val numberFormat = NumberFormat.getInstance(Locale.getDefault())
        val parsedStringNumber = numberFormat.parse(food.txtFoodprice)

        if (totalPrice.toInt() == 0) {
            binding.btnGotoBasketShop.setImageResource(R.drawable.ic_checkout)
        }
        totalPrice += parsedStringNumber.toFloat()
        dialogueBinding?.dialogueBtnConfirm?.text = "PROCEED $totalPrice €"
    }

    override fun onDecreaseOrderQuantity(food: FoodItem, position: Int) {

        if (!isInBasketShop) {
            var isRemovedFromItemList: Boolean = false
            if (food.orderQuantity!! > 0) {
                for (i in 0 until shopItemList.size) {
                    if (!isRemovedFromItemList) {
                        if (shopItemList[i] == food) {
                            food.orderQuantity = food.orderQuantity!! - 1
                            mainScreenPresenter.onUpdateFood(food, position)
                            if (food.orderQuantity!! > 0) {
                                paymentAdapter.updateFoodItem(food, i)
                            } else if (food.orderQuantity == 0) {
                                paymentAdapter.removeFoodItem(food, i)
                                orderedItemIndexes.removeAt(i)
                                paymentAdapter.setAdaptOrderedItemInd(orderedItemIndexes)
                                myAdapter.setAdaptOrderedItemInd(orderedItemIndexes)
                                isRemovedFromItemList = true
                            }
                        }
                    }
                }


            }

        } else if (isInBasketShop) {
            var isRemovedFromItemList: Boolean = false
            if (food.orderQuantity!! > 0) {
                for (i in 0 until shopItemList.size) {
                    if (!isRemovedFromItemList) {
                        if (shopItemList[i] == food) {
                            food.orderQuantity = food.orderQuantity!! - 1
                            mainScreenPresenter.onUpdateFood(food, orderedItemIndexes[position])
                            if (food.orderQuantity!! > 0) {
                                paymentAdapter.updateFoodItem(food, i)
                            } else if (food.orderQuantity == 0) {
                                paymentAdapter.removeFoodItem(food, i)
                                orderedItemIndexes.removeAt(i)
                                paymentAdapter.setAdaptOrderedItemInd(orderedItemIndexes)
                                myAdapter.setAdaptOrderedItemInd(orderedItemIndexes)
                                isRemovedFromItemList = true
                            }
                        }
                    }
                }


            }

        }
        val numberFormat = NumberFormat.getInstance(Locale.getDefault())
        val parsedStringNumber = numberFormat.parse(food.txtFoodprice)
        totalPrice -= parsedStringNumber.toFloat()


        if (shopItemList.size == 0) {
            binding.btnGotoBasketShop.setImageResource(R.drawable.ic_checkout_icon_empty)
        }
        dialogueBinding?.dialogueBtnConfirm?.text = "PROCEED $totalPrice €"
    }


    @SuppressLint("CommitPrefEdits")
    override fun showFoods(dataBaseFoodList: List<FoodItem>, userName: String?) {
        if (userName != null) {
            if (userName == "admin") {
                isAdmin = true
                binding.myImageView.visibility = View.GONE
                binding.mainLinearLayout.visibility = View.VISIBLE
                binding.btnAddFoodItem.visibility = View.VISIBLE
                sharedPreferences.edit().putBoolean("isAdmin", isAdmin)
            } else {
                isAdmin = false
                binding.myImageView.visibility = View.GONE
                binding.mainLinearLayout.visibility = View.VISIBLE
                binding.btnAddFoodItem.visibility = View.GONE
                sharedPreferences.edit().putBoolean("isAdmin", isAdmin)
            }
        }



        foodList = ArrayList(dataBaseFoodList)
        foodList2 = foodList.clone() as ArrayList<FoodItem>

        myAdapter = FoodMenuAdapter(foodList, foodList2, this, FoodMenuAdapter.ISMAIN_ADAPTER)
        binding.foodMenuRecycler.adapter = myAdapter
        binding.foodMenuRecycler.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun addFoodItem() {

        if (!myAdapter.getIsTyping()) {

            val dialogue = AlertDialog.Builder(this).create()
            val dialogueBinding = DialogueAddFootItemBinding.inflate(layoutInflater)
            dialogue.setView(dialogueBinding.root)
            dialogue.setCancelable(true)

            dialogue.show()

            dialogueBinding.dialogueBtnDone.setOnClickListener {

                val txtFoodName = dialogueBinding.dialogueEdtFoodName.text.toString()
                val txtFoodType = dialogueBinding.dialogueEdtFoodType.text.toString()

                val txtFoodPrice = dialogueBinding.dialogueEdtFoodPrice.text.toString()
                val txtFoodDistance = dialogueBinding.dialogueEdtFoodPrice.text.toString()

                val txtRatinCount: Int = (80..124).random()
                val ratingBarStar: Float = 3f + Random.nextFloat() * 2f

                val foodImageUrl = "https://i.ibb.co/SPsVF44/penne-Alferedo2.jpg"
//                    foodList[(0 until foodList.size).random()].urlImage

                val newFoodItem = FoodItem(
                    txtFoodName, txtFoodType, txtFoodPrice, txtFoodDistance, foodImageUrl,
                    txtRatinCount, ratingBarStar
                )
                myAdapter.addFoodItem(newFoodItem)
                mainScreenPresenter.OnInsertFoodItem(newFoodItem)
                dialogue.dismiss()
                binding.foodMenuRecycler.scrollToPosition(0)
            }
        } else {
            Toast.makeText(
                this, "You may not add new item while searching for existing ones!",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    override fun deleteFoodItem(oldFoodItem: FoodItem, position: Int) {
        myAdapter.removeFoodItem(oldFoodItem, position)
        myAdapter.notifyItemChanged(position)

        if (myAdapter.getIsTyping()) {
            selectedItemsIndexes.clear()

            for (i in 0 until foodList.size) {

                if (foodList[i].txtFoodName.lowercase()
                        .contains(binding.editTextSearch.text.toString().lowercase())
                ) {

                    selectedItemsIndexes.add(i)
                }
            }
            myAdapter.setSelectedItemIndexList(selectedItemsIndexes)


        }
    }

    override fun updateFoodItem(updatedFoodItem: FoodItem, position: Int) {
        myAdapter.updateFoodItem(updatedFoodItem, position)
    }

    override fun searchFoodItems(editTextSearch: Editable?) {
        if (editTextSearch!!.isNotEmpty()) {

            myAdapter.setIsTyping(true)
            filteredFoodList.clear()
            selectedItemsIndexes.clear()

            for (i in 0 until foodList.size) {

                if (foodList[i].txtFoodName.lowercase()
                        .contains(editTextSearch.toString().lowercase())
                ) {
                    filteredFoodList.add(foodList[i])
                    selectedItemsIndexes.add(i)
                }
            }
            myAdapter.setData(filteredFoodList)
            myAdapter.setSelectedItemIndexList(selectedItemsIndexes)
        } else {
            myAdapter.setIsTyping(false)
            myAdapter.setData(foodList)
        }
    }

    override fun onDestroy() {
        sharedPreferences.edit().putFloat("TotalPrice", totalPrice).apply()
        super.onDestroy()
        mainScreenPresenter.onDetach()
    }

}