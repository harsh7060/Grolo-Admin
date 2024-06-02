package com.example.blinkitadmin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.blinkitadmin.adapters.AdapterSelectedImage
import com.example.blinkitadmin.databinding.FragmentAddProductBinding
import com.example.blinkitadmin.model.Product
import com.example.blinkitadmin.viewModels.AdminViewModel
import kotlinx.coroutines.launch

class AddProductFragment : Fragment() {

    private val viewModel: AdminViewModel by viewModels()
    private lateinit var binding: FragmentAddProductBinding
    private val imageUris : ArrayList<Uri> = arrayListOf()
    val selectedImage = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){listOfUri->
        val fiveImages = listOfUri.take(5)
        imageUris.clear()
        imageUris.addAll(fiveImages)
        binding.rvProductImage.adapter = AdapterSelectedImage(imageUris)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater)

        setStatusBarColor()

        setAutoCompleteTextView()

        onImageSelectClick()

        onAddButtonClicked()

        return binding.root
    }

    private fun onAddButtonClicked() {
        binding.btnAddProduct.setOnClickListener{
            Utils.showDialog(requireContext(),"Adding Product...")
            val productName = binding.etProductName.text.toString()
            val productUnit = binding.etProductUnit.text.toString()
            val productCategory = binding.etProductCategory.text.toString()
            val productType = binding.etProductType.text.toString()
            val productPrice = binding.etProductPrice.text.toString()
            val productQuantity = binding.etProductQuantity.text.toString()
            val productStock = binding.etProductStocks.text.toString();

            if(productName.isEmpty() || productUnit.isEmpty() || productCategory.isEmpty() ||
                productType.isEmpty() || productPrice.isEmpty() || productQuantity.isEmpty()){
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(), "Please fill all fields")
                }
            }else if(imageUris.isEmpty()){
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(), "Please select at least one image")
                }
            }else{
                val product = Product(
                    productName = productName,
                    productQuantity = productQuantity.toInt(),
                    productUnit = productUnit,
                    productPrice = productPrice.toInt(),
                    productStock = productStock.toInt(),
                    productCategory = productCategory,
                    productType = productType,
                    itemCount = 0,
                    adminUid = Utils.getCurrentUserId(),
                    productId = Utils.getRandomId()
                )
                
                saveImage(product)
            }
        }
    }

    private fun saveImage(product: Product) {
        viewModel.saveImageInDB(imageUris)
        lifecycleScope.launch {
            viewModel.isImageUploaded.collect{
                if(it){
                    Utils.apply {
                        hideDialog()
                        showToast(requireContext(), "Image uploaded successfully")
                    }
                    getUrls(product)
                }
            }
        }
    }

    private fun getUrls(product: Product) {
        Utils.showDialog(requireContext(),"Uploading Product...")
        lifecycleScope.launch {
            viewModel.downloadedUrls.collect{
                val urls = it
                product.productImageUris = urls
                saveProduct(product)
            }
        }
    }

    private fun saveProduct(product: Product){
        viewModel.saveProduct(product)
        lifecycleScope.launch {
            viewModel.isImageUploaded.collect{
                if(it){
                    Utils.hideDialog()
                    startActivity(Intent(requireContext(), AdminMainActivity::class.java))
                    Utils.showToast(requireContext(), "Product added successfully")
                }
            }
        }
    }

    private fun onImageSelectClick() {
        binding.btnSelectImage.setOnClickListener {
            selectedImage.launch("image/*")
        }
    }

    private fun setAutoCompleteTextView() {
        val units = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitsOfProduct)
        val category = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductCategory)
        val productType = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductType)

        binding.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(productType)
        }
    }

    private fun setStatusBarColor() {
        activity?.window?.statusBarColor = resources.getColor(R.color.yellow)
    }
}