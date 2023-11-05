package com.example.mydelivery

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydelivery.data.MenuCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModel:ViewModel() {
     var mutableLiveDataMeal: MutableLiveData<MenuCategory>?=null

    fun getPosts(search: String) : MutableLiveData<MenuCategory>? {
            mutableLiveDataMeal = MutableLiveData()
            loadMore(search)
        return mutableLiveDataMeal
    }

     private fun loadMore(search: String) {

        RestClient.instance().search(search).enqueue(object : Callback<MenuCategory> {
            override fun onResponse(call: Call<MenuCategory>, response: Response<MenuCategory>) {
                if(response.code() < 400) {
                    mutableLiveDataMeal!!.value = response.body()
                }
            }

            override fun onFailure(call: Call<MenuCategory>, t: Throwable) {
                Log.d("happy", "onFailure: ${t.message}")
            }
        })
    }
}