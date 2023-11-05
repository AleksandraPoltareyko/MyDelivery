package com.example.mydelivery

import com.example.mydelivery.data.MenuCategory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
//https://www.themealdb.com
// /api/json/v1/1/filter.php?
// c=Chicken

interface TheMealDB {

    @GET("/api/json/v1/1/filter.php?")
    fun search(
       @Query("c") c:String
    ):Call<MenuCategory>


}