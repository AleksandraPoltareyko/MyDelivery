package com.example.mydelivery

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydelivery.data.MenuCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    val cities = arrayOf("Москва","Тула","Казань")
    val listOfFood = mutableListOf<Food>()
    private var loading = false
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/") // базовый url
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    private val service = retrofit.create(TheMealDB::class.java)

    private val serviceCategory = retrofit.create(TheCategories::class.java)

    private val list: RecyclerView by lazy { findViewById(R.id.list) }
    private val categories: LinearLayout by lazy { findViewById(R.id.categories) }
    private lateinit var adapter : RecyclerMealAdapter

    private val textViews = mutableListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.spinner)

        сreateCategories()

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter;

    }

    private fun сreateCategories() {
        val call = serviceCategory.get()
        val mainActivity = this
        val oMyCategory = OnClickListener {
            for (tv in textViews){
                tv.setBackgroundColor(Color.LTGRAY)
            }
            it.setBackgroundColor(Color.GREEN)
            val tv = it as TextView
            loadMore(tv.text.toString().replace(" ",""))
        }
        lifecycleScope.launch(Dispatchers.Default)
        {
            try {
                val response = call.execute()
                withContext(Dispatchers.Main)
                {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null ) {
                            for (category in result.categories.subList(0,5)) {
                                val textView = TextView(mainActivity)
                                textView.layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                textView.text = "  ${category.strCategory}  "
                                textView.isClickable = true
                                textView.setBackgroundColor(Color.LTGRAY)
                                textView.setOnClickListener(oMyCategory)
                                categories.addView(textView)
                                textViews.add(textView)
                            }
                        }
                    }
                }
                if (textViews.size > 0){
                    val tv = textViews[0]
                    loadMore(tv.text.toString().replace(" ",""))
                    tv.setBackgroundColor(Color.GREEN)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main)
                {
                    Log.d("happy", "onFailure: ${e.message}")
                }
            }
        }
    }

    private fun loadMore(search: String) {
        loading = true
        val call = service.search(search)

        lifecycleScope.launch(Dispatchers.Default)
        {
            try {
                val response = call.execute()
                withContext(Dispatchers.Main)
                {
                    onResponse(call, response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main)
                {
                    onFailure(call, e)
                }
            }
        }
    }

    private fun onFailure(call: Call<MenuCategory>, e: Exception) {

        loading = false
        Log.d("happy", "onFailure: ${e.message}")

    }

    private fun onResponse(call: Call<MenuCategory>, response: Response<MenuCategory>) {

        if (response.isSuccessful) {
            val result = response.body()
            var price = 0
            if (result != null ) {
                listOfFood.clear()
                for (meal in result.meals){
                   try {
                       price = meal.idMeal.substring(2).toInt()
                   } catch (e: Exception){price = 0}
                   listOfFood.add(Food(meal.strMealThumb,meal.strMeal,"", price, id = meal.idMeal))
                }
                adapter = RecyclerMealAdapter(listOfFood)
                list.adapter = adapter
                list.layoutManager = LinearLayoutManager(this)
                list.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
                val itemTouchHelper = ItemTouchHelper(
                    object : ItemTouchHelper.SimpleCallback (
                        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    )
                    {
                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ) = false

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            val position = viewHolder.adapterPosition
                            if(position != RecyclerView.NO_POSITION)
                            {
                                listOfFood.removeAt(position)
                                adapter.notifyItemRemoved(position)
                            }
                        }
                    }
                )
                itemTouchHelper.attachToRecyclerView(list)
            }
            adapter.notifyDataSetChanged()
        }
        loading = false

    }
}