package com.example.mydelivery.data


import com.example.mydelivery.TheCategories
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RestClientCategory {

    private val serviceCategory: TheCategories

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        serviceCategory = retrofit.create(TheCategories::class.java)
    }

    companion object {
        const val BASE_URL = "https://www.themealdb.com/"

        private val instance = RestClientCategory()

        fun instance(): TheCategories {
            return instance.serviceCategory
        }
    }

}