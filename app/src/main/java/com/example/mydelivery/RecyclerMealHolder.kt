package com.example.mydelivery

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecyclerMealHolder(itemView:View, adapter: RecyclerMealAdapter): RecyclerView.ViewHolder(itemView) {

    private val name:TextView = itemView.findViewById(R.id.name)
    private val image:ImageView = itemView.findViewById(R.id.image)
    private val description:TextView = itemView.findViewById(R.id.description)
    private val price:TextView = itemView.findViewById(R.id.price)

    public fun bind(food: Food){
        name.text = food.name

        Picasso
            .get()
            .load(food.image)
            .into(image)

        description.text = food.description
        price.text = "${food.price}"
    }

}