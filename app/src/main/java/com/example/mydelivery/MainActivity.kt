package com.example.mydelivery


import android.graphics.Color
import android.os.Bundle
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydelivery.data.MenuCategory
import com.example.mydelivery.data.categories.Categories




class MainActivity : AppCompatActivity() {
    val cities = arrayOf("Москва","Тула","Казань")
    val listOfFood = mutableListOf<Food>()

    private val list: RecyclerView by lazy { findViewById(R.id.list) }
    private val categories: LinearLayout by lazy { findViewById(R.id.categories) }
    private lateinit var adapter : RecyclerMealAdapter

    private val textViews = mutableListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.spinner)

        createCategories()

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

    }

    private fun createCategories() {
        val mainActivity = this
        val oMyCategory = OnClickListener {
            for (tv in textViews){
                tv.setBackgroundColor(Color.LTGRAY)
            }
            it.setBackgroundColor(Color.GREEN)
            val tv = it as TextView
            loadMore(tv.text.toString().replace(" ",""))
        }

        val model = ViewModelProvider(this)[MyViewModelCategory::class.java]
        model.getPosts()?.observe(this,
            Observer<Categories> {
                        val result = it
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
                if (textViews.size > 0){
                    val tv = textViews[0]
                    loadMore(tv.text.toString().replace(" ",""))
                    tv.setBackgroundColor(Color.GREEN)
                }
    })
    }

    private fun loadMore(search: String) {

        val model = ViewModelProvider(this)[MyViewModel::class.java]
        model.getPosts(search)?.observe(this,
            Observer<MenuCategory> {
                listOfFood.clear()
                var price: Int
                if (it != null) {
                    for (meal in it.meals) {
                        try {
                            price = meal.idMeal.substring(2).toInt()
                        } catch (e: Exception) {
                            price = 0
                        }
                        listOfFood.add(
                            Food(
                                meal.strMealThumb,
                                meal.strMeal,
                                "",
                                price,
                                id = meal.idMeal
                            )
                        )
                    }

                    adapter = RecyclerMealAdapter(listOfFood)
                    list.adapter = adapter
                    list.layoutManager = LinearLayoutManager(this)
                    list.addItemDecoration(
                        DividerItemDecoration(
                            this,
                            LinearLayoutManager.VERTICAL
                        )
                    )
                    val itemTouchHelper = ItemTouchHelper(
                        object : ItemTouchHelper.SimpleCallback(
                            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                        ) {
                            override fun onMove(
                                recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder
                            ) = false

                            override fun onSwiped(
                                viewHolder: RecyclerView.ViewHolder,
                                direction: Int
                            ) {
                                val position = viewHolder.adapterPosition
                                if (position != RecyclerView.NO_POSITION) {
                                    listOfFood.removeAt(position)
                                    adapter.notifyItemRemoved(position)
                                }
                            }
                        }
                    )
                    itemTouchHelper.attachToRecyclerView(list)
                }
                adapter.notifyDataSetChanged()
            })
    }

}


