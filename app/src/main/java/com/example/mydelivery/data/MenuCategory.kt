package com.example.mydelivery.data
import com.example.mydelivery.data.Meal
import com.google.gson.annotations.SerializedName

data class MenuCategory(
    @SerializedName("meals")
    val meals: List<Meal>
)