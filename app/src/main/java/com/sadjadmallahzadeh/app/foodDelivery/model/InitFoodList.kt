package com.sadjadmallahzadeh.app.foodDelivery.model

class InitFoodList {
    companion object {


        private val foodListFirstRun = arrayListOf(
            FoodItem(
                "Hambergur",
                "Amercian",
                "16",
                "55",
                "https://i.ibb.co/Ns2DQK4/hamburger.jpg",
                92,
                4.2f
            ),
            FoodItem(
                "Lasagna",
                "Italian",
                "21.90",
                "50",
                "https://i.ibb.co/drNY3j8/lasagna.jpg",
                114,
                4.4f
            ),
            FoodItem(
                "Pizza",
                "American",
                "38.90",
                "60",
                "https://i.ibb.co/Q8WGs5Q/pizza.jpg",
                85,
                3.4f
            ),
            FoodItem(
                "Grilled Fish",
                "Finnish",
                "14,9",
                "65",
                "https://i.ibb.co/0sxh7y7/grilled-fish.jpg",
                116,
                4.6f
            ),
            FoodItem(
                "Sushi",
                "Japanese",
                "15,9",
                "55",
                "https://i.ibb.co/c8nQVqB/sushi.jpg",
                124,
                4f
            ),
            FoodItem(
                "Roasted Fish",
                "Finnish",
                "18",
                "75",
                "https://i.ibb.co/KbDWXFS/roasted-fish.jpg",
                112,
                4.2f
            ),
            FoodItem(
                "Fried Chicken",
                "American",
                "20",
                "60",
                "https://i.ibb.co/WzGp1m1/fried-chicken.jpg",
                102,
                4.4f
            ),
            FoodItem(
                "Pasta",
                "Italian",
                "8",
                "55",
                "https://i.ibb.co/3MTnRSZ/pasta1.jpg",
                118,
                3.8f
            ),
            FoodItem(
                "Grilled Chicken",
                "French",
                "10",
                "60",
                "https://i.ibb.co/820rdm4/grilled-chicken.jpg",
                94,
                4.2f
            ),
            FoodItem(
                "Sweet Bread",
                "French",
                "12",
                "50",
                "https://i.ibb.co/wszJsGF/bread-pide.jpg",
                116,
                4.6f
            ),
            FoodItem(
                "Caesar salad",
                "Greek",
                "14",
                "55",
                "https://i.ibb.co/vJgY8b5/Caesar-salad.jpg",
                124,
                3f
            ),
            FoodItem(
                "Vegetable Salad",
                "Spanish",
                "12",
                "40",
                "https://i.ibb.co/19F8tRb/vegtable-salad.jpg",
                112,
                4.2f
            ),
            FoodItem(
                "Fruit Salad",
                "Swedish",
                "14",
                "65",
                "https://i.ibb.co/BHpNkB6/fruit-salad.jpg",
                92,
                4.2f
            )
        )

        fun getInitFoodlist(): ArrayList<FoodItem> {
            return foodListFirstRun
        }
    }
}