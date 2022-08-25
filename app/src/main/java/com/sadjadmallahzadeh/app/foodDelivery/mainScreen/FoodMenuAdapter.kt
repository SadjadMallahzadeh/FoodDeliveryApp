package com.sadjadmallahzadeh.app.foodDelivery.mainScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.sadjadmallahzadeh.app.foodDelivery.databinding.FoodItemBasketShopBinding
import com.sadjadmallahzadeh.app.foodDelivery.databinding.FoodItemBinding
import com.sadjadmallahzadeh.app.foodDelivery.model.FoodItem

class FoodMenuAdapter(
    val data: ArrayList<FoodItem>,
    private var data2: ArrayList<FoodItem>,
    private val foodEvents: FoodEvents,
    private val isMainAdapter: Boolean
) : RecyclerView.Adapter<FoodMenuAdapter.FoodMenuViewHolder>() {

    lateinit var binding: ViewBinding
    private var isTyping: Boolean = false
    private var isInBasketShop: Boolean = false
    private var adaptOrderedItemInd = arrayListOf<Int>()

    companion object {
        const val ISMAIN_ADAPTER: Boolean = true
        const val IS_NOT_MAIN_ADAPTER: Boolean = false
    }

    fun setIsInBasketShop(isInbasketShop: Boolean) {
        this.isInBasketShop = isInbasketShop
    }

    fun getAdaptOrderedItemInd(): ArrayList<Int> {
        return adaptOrderedItemInd
    }

    fun setAdaptOrderedItemInd(newOrderedItemIndex: ArrayList<Int>) {
        this.adaptOrderedItemInd = newOrderedItemIndex
    }

    fun getIsTyping(): Boolean {
        return isTyping
    }

    fun setIsTyping(isTyping: Boolean) {
        this.isTyping = isTyping
    }

    private var selectedItemsIndexeslist = arrayListOf<Int>(0, 1)


    fun setSelectedItemIndexList(newSelectedItemsindexsList: ArrayList<Int>) {
        selectedItemsIndexeslist.clear()
        this.selectedItemsIndexeslist.addAll(newSelectedItemsindexsList)
    }


    open inner class FoodMenuViewHolder(val foodItemBinding: ViewBinding) :
        RecyclerView.ViewHolder(foodItemBinding.root) {

        //Since two different layouts are used for mainAdapter and paymentAdapter
        //it should be decided which view binding to be considered in bindViews Function
        //of the FoodMenuAdapter as the BaseClass

        open fun bindViews(food: FoodItem) {

            if (isMainAdapter) {
                foodItemBinding as FoodItemBinding
                foodItemBinding.foodItemName.text = food.txtFoodName
                foodItemBinding.foodItemType.text = food.txtFoodType
                foodItemBinding.foodItemPrice.text = food.txtFoodprice+" €"
                foodItemBinding.foodItemDistance.text = food.txtDistance + " min delivery time"
                foodItemBinding.foodItemRatingBar.rating = food.rating
                foodItemBinding.foodItemRatingCount.text = food.numOfRating.toString()
                foodItemBinding.foodItemOrderQuanitty.text = food.orderQuantity.toString()



                Glide.with(itemView.context).load(food.urlImage).into(foodItemBinding.foodItemImage)


                foodItemBinding.root.setOnClickListener {
                    foodEvents.onFoodItemClicked(food, adapterPosition)
                }
                foodItemBinding.root.setOnLongClickListener {
                    foodEvents.OnFoodItemLongClicked(food, adapterPosition)
                    true
                }
                foodItemBinding.increaseOrder.setOnClickListener {

                    foodEvents.onIncreaseOrderQuantity(food, adapterPosition)
                }
                foodItemBinding.decreaseOrder.setOnClickListener {
                    foodEvents.onDecreaseOrderQuantity(food, adapterPosition)
                }
            } else {
                foodItemBinding as FoodItemBasketShopBinding
                foodItemBinding.foodItemName.text = food.txtFoodName
                foodItemBinding.foodItemType.text = food.txtFoodType
                foodItemBinding.foodItemPrice.text = food.txtFoodprice+" €"
                foodItemBinding.foodItemDistance.text = food.txtDistance + " min delivery time"
                foodItemBinding.foodItemRatingBar.rating = food.rating
                foodItemBinding.foodItemRatingCount.text = food.numOfRating.toString()
                foodItemBinding.foodItemOrderQuanitty.text = food.orderQuantity.toString()



                Glide.with(itemView.context).load(food.urlImage).into(foodItemBinding.foodItemImage)


                foodItemBinding.root.setOnClickListener {
                    foodEvents.onFoodItemClicked(food, adapterPosition)
                }
                foodItemBinding.root.setOnLongClickListener {
                    foodEvents.OnFoodItemLongClicked(food, adapterPosition)
                    true
                }
                foodItemBinding.increaseOrder.setOnClickListener {

                    foodEvents.onIncreaseOrderQuantity(food, adapterPosition)
                }
                foodItemBinding.decreaseOrder.setOnClickListener {
                    foodEvents.onDecreaseOrderQuantity(food, adapterPosition)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodMenuViewHolder {

        //Since two different layouts are used for mainAdapter and paymentAdapter
        //it should be decided which view binding to be passed to the constructor
        // of FoodMenuViewHolder


        if (isMainAdapter) {
            binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            binding = FoodItemBasketShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }

        return FoodMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodMenuViewHolder, position: Int) {


        if (!isTyping) {
            holder.bindViews(data[position])
        } else {
            holder.bindViews(data2[position])
        }


    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        if (!isTyping) {
            return data.size
        } else {
            return data2.size
        }
    }

    fun addFoodItem(newFoodItem: FoodItem) {


        data.add(0, newFoodItem)
        notifyItemInserted(0)
        notifyDataSetChanged()
        data2.clear()
        data2.addAll(data)


    }

    fun removeFoodItem(oldFood: FoodItem, position: Int) {

        if (!isTyping) {
            data.removeAt(position)
            data2.clear()
            data2.addAll(data)
            notifyItemRemoved(position)

        } else {

            data2.removeAt(position)
            notifyItemRemoved(position)
            data.removeAt(selectedItemsIndexeslist[position])
            notifyItemRemoved(selectedItemsIndexeslist[position])
        }


    }

    fun updateFoodItem(updatedFood: FoodItem, position: Int) {

        data[position] = updatedFood
        notifyItemChanged(position)
        data2.clear()
        data2.addAll(data)

    }

    fun setData(newFoodList: ArrayList<FoodItem>) {


        if (!isTyping) {
            data2.clear()
            data2.addAll(newFoodList)
            notifyDataSetChanged()
        } else {
            data2.clear()
            data2.addAll(newFoodList)
            notifyDataSetChanged()
        }
    }


    interface FoodEvents {
        fun onFoodItemClicked(food: FoodItem, position: Int)
        fun OnFoodItemLongClicked(food: FoodItem, position: Int)
        fun onIncreaseOrderQuantity(food: FoodItem, position: Int)
        fun onDecreaseOrderQuantity(food: FoodItem, position: Int)

    }

}
