package com.example.blinkitadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blinkitadmin.databinding.ItemViewProductCategoryBinding
import com.example.blinkitadmin.model.Category

class CategoriesAdapter(
    private val categoryArrayList: ArrayList<Category>,
    val onCategoryClick: (Category) -> Unit
):
    RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    class CategoriesViewHolder(val binding: ItemViewProductCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(ItemViewProductCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = categoryArrayList[position]
        holder.binding.apply {
            ivCategoryImage.setImageResource(category.image)
            tvCategoryTitle.text = category.category
        }
        holder.itemView.setOnClickListener{
            onCategoryClick(category)
        }
    }
}