package com.example.blinkitadmin.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.blinkitadmin.Utils
import com.example.blinkitadmin.model.Product
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class AdminViewModel: ViewModel() {

    private val _isImageUploaded = MutableStateFlow(false)
    var isImageUploaded = _isImageUploaded

    private val _downloadedUrls = MutableStateFlow<ArrayList<String?>>(arrayListOf())
    var downloadedUrls: StateFlow<ArrayList<String?>> = _downloadedUrls

    private val _isProductSaved = MutableStateFlow(false)
    var isProductSaved: StateFlow<Boolean> = _isProductSaved


    fun saveImageInDB(imageUri: ArrayList<Uri>){
        val downloadUrls = ArrayList<String?>()

        imageUri.forEach{uri->
            val imageRef = FirebaseStorage.getInstance().reference.child(Utils.getCurrentUserId()).child("images")
                .child(UUID.randomUUID().toString())
            imageRef.putFile(uri).continueWithTask{
                imageRef.downloadUrl
            }.addOnCompleteListener{
                val url = it.result
                downloadUrls.add(url.toString())

                if(downloadUrls.size == imageUri.size){
                    _isImageUploaded.value = true
                    _downloadedUrls.value = downloadUrls
                }
            }
        }
    }

    fun saveProduct(product: Product){
        FirebaseDatabase.getInstance().getReference("Admins").child("All Products/${product.productId}").setValue(product)
            .addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("Admins").child("ProductCategory/${product.productId}").setValue(product)
                    .addOnSuccessListener {
                        FirebaseDatabase.getInstance().getReference("Admins").child("ProductType/${product.productId}").setValue(product)
                            .addOnSuccessListener {
                                _isProductSaved.value = true
                            }
                    }
            }
    }
}