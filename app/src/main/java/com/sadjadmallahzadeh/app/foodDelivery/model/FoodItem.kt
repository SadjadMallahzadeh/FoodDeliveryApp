package com.sadjadmallahzadeh.app.foodDelivery.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Table_Food")
 data class FoodItem (

    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,

    var txtFoodName:String,
    var txtFoodType:String,
    var txtFoodprice:String,
    var txtDistance:String,
    var urlImage:String,
    var numOfRating: Int,
    var rating:Float,
    var orderQuantity:Int?=null

        ){

    constructor(secondtxtFoodname:String, secondtxtFoodType:String,
                secondtxtFoodprice:String,secondtxtDistance:String,
                secondurlImage:String,secondnumOfRating: Int,
                secondrating:Float)
            : this(
        null,secondtxtFoodname,secondtxtFoodType,secondtxtFoodprice,secondtxtDistance,
        secondurlImage,secondnumOfRating,secondrating,0){
                    this.txtFoodName=secondtxtFoodname
        this.txtFoodType=secondtxtFoodType
        this.txtFoodprice=secondtxtFoodprice
        this.txtDistance=secondtxtDistance
        this.urlImage=secondurlImage
        this.numOfRating=secondnumOfRating
        this.rating=secondrating

                }


}