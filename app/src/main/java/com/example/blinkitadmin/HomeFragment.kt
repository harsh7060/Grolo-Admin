package com.example.blinkitadmin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.blinkitadmin.adapters.AdapterProduct
import com.example.blinkitadmin.adapters.CategoriesAdapter
import com.example.blinkitadmin.databinding.EditProductLayoutBinding
import com.example.blinkitadmin.databinding.FragmentHomeBinding
import com.example.blinkitadmin.model.Category
import com.example.blinkitadmin.model.Product
import com.example.blinkitadmin.viewModels.AdminViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    val viewModel: AdminViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        setCategories()

        getAllProducts("All")

        return binding.root
    }

    private fun getAllProducts(category: String) {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchAllProducts(category).collect{
                if(it.isEmpty()){
                    binding.rvProducts.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                }else{
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                }
                val adapterProduct = AdapterProduct(::onEditBtnClick)
                binding.rvProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)

                binding.shimmerViewContainer.visibility = View.GONE
            }
        }
    }

    private fun onCategoryClick(categories: Category){
        getAllProducts(categories.category)
    }

    private fun onEditBtnClick(product: Product){
        val editProduct = EditProductLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        editProduct.apply {
            etProductName.setText(product.productName)
            etProductQuantity.setText(product.productQuantity.toString())
            etProductUnit.setText(product.productUnit)
            etProductCategory.setText(product.productCategory)
            etProductType.setText(product.productType)
            etProductStocks.setText(product.productStock.toString())
            etProductPrice.setText(product.productPrice.toString())
        }
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(editProduct.root)
            .create()
        alertDialog.show()

        editProduct.btnEdit.setOnClickListener{
            editProduct.etProductName.isEnabled = true
            editProduct.etProductQuantity.isEnabled = true
            editProduct.etProductUnit.isEnabled = true
            editProduct.etProductPrice.isEnabled = true
            editProduct.etProductStocks.isEnabled = true
            editProduct.etProductCategory.isEnabled = true
            editProduct.etProductType.isEnabled = true
        }
        setAutoCompleteTextView(editProduct)

        editProduct.btnSave.setOnClickListener{
            lifecycleScope.launch {
                product.productName = editProduct.etProductName.text.toString()
                product.productQuantity = editProduct.etProductQuantity.text.toString().toInt()
                product.productType = editProduct.etProductType.text.toString()
                product.productStock = editProduct.etProductStocks.text.toString().toInt()
                product.productPrice = editProduct.etProductPrice.text.toString().toInt()
                product.productCategory = editProduct.etProductCategory.text.toString()
                product.productUnit = editProduct.etProductUnit.text.toString()
                viewModel.savingUpdatedProduct(product)
            }
            Utils.showToast(requireContext(),"Product Updated Successfully ✅")
            alertDialog.dismiss()
        }
    }

    private fun setAutoCompleteTextView(editProduct: EditProductLayoutBinding) {
        val units = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitsOfProduct)
        val category = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductCategory)
        val productType = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductType)

        editProduct.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(productType)
        }
    }

    private fun setCategories() {
        val categoryList = ArrayList<Category>()
        for(i in 0 until Constants.allProductCategory.size){
            categoryList.add(Category(Constants.allProductCategory[i],Constants.allProductCategoryIcon[i]))
        }

        binding.rvCategories.adapter = CategoriesAdapter(categoryList,::onCategoryClick)
    }
}