package com.example.blinkitadmin.model

import androidx.annotation.NonNull
import javax.annotation.Nonnull

data class CartProducts(
    var productId: String = "random",

    var productName: String? = null,
    var productPrice: String? = null,
    var productQuantity: String? = null,
    var productStock: Int? = null,
    var productImage: String? = null,
    var productCategory: String? = null,
    var adminUid: String? = null,
    var productCount: Int? = null,
    var productType: String? = null
)