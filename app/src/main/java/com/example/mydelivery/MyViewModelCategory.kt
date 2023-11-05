package com.example.mydelivery

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydelivery.data.RestClientCategory
import com.example.mydelivery.data.categories.Categories
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModelCategory:ViewModel() {

    var mutableLiveDataCategory: MutableLiveData<Categories>?=null

    fun getPosts() : MutableLiveData<Categories>? {
        mutableLiveDataCategory= MutableLiveData()
        loadMore()
        return mutableLiveDataCategory
    }

    private fun loadMore() {

        RestClientCategory.instance().get().enqueue(object : Callback<Categories> {
            override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                if(response.code() < 400) {
                    mutableLiveDataCategory!!.value = response.body()
                }
            }

            override fun onFailure(call: Call<Categories>, t: Throwable) {
                Log.d("happy", "onFailure: ${t.message}")
            }
        }
        )
    }

}