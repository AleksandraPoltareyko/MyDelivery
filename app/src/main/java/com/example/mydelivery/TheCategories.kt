package com.example.mydelivery

import com.example.mydelivery.data.categories.Categories
import com.example.mydelivery.data.categories.Category
import retrofit2.Call
import retrofit2.http.GET

//https://www.themealdb.com
// /api/json/v1/1/categories.php

interface TheCategories {

    @GET("/api/json/v1/1/categories.php")
    fun get():Call<Categories>

}