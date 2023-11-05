package com.example.mydelivery

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RestClient {

    private val service: TheMealDB

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            //.addConverterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        service = retrofit.create(TheMealDB::class.java)
    }

    companion object {
        const val BASE_URL = "https://www.themealdb.com/"

        private val instance = RestClient()

        fun instance(): TheMealDB {
            return instance.service
        }
    }

}