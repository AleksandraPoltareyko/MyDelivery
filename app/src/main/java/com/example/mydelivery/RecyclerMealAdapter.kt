package com.example.mydelivery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerMealAdapter(private val foods: MutableList<Food>) : RecyclerView.Adapter<RecyclerMealHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerMealHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(viewType, parent, false)
        return RecyclerMealHolder(view, this)
    }

    override fun getItemViewType(position: Int)=  R.layout.item

    override fun onBindViewHolder(holder: RecyclerMealHolder, position: Int) = holder.bind(foods[position])

    override fun getItemCount() = foods.size
}