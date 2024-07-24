package com.example.blinkitadmin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.blinkitadmin.R
import com.example.blinkitadmin.adapters.AdapterOrders
import com.example.blinkitadmin.databinding.FragmentOrderBinding
import com.example.blinkitadmin.model.OrderedItems
import com.example.blinkitadmin.utils.Utils
import com.example.blinkitadmin.viewModels.AdminViewModel
import kotlinx.coroutines.launch

class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding
    private val viewModel: AdminViewModel by viewModels()
    private lateinit var adapterOrders: AdapterOrders

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater)

        getAllOrders()

        return binding.root
    }

    private fun getAllOrders() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.getAllOrders().collect{orderList->
                if(orderList.isNotEmpty()){
                    val orderedList = ArrayList<OrderedItems>()
                    for(orders in orderList){
                        val title = StringBuilder()
                        var totalPrice = 0
                        for(products in orders.orderList!!){
                            val itemPrice = products.productPrice!!.substring(1).toInt()
                            val itemCount = products.productCount
                            totalPrice += (itemPrice* itemCount!!)

                            title.append("${products.productName}, ")
                        }
                        val orederedItems = OrderedItems(orders.orderId, orders.orderDate, orders.orderStatus, title.toString(), totalPrice, orders.userAddress)
                        orderedList.add(orederedItems)
                    }
                    adapterOrders = AdapterOrders(requireContext(), ::onOrderItemViewClick)
                    binding.rvOrders.adapter = adapterOrders
                    adapterOrders.differ.submitList(orderedList)
                    binding.shimmerViewContainer.visibility = View.GONE
                }
                else{
                    Utils.showToast(requireContext(), "No Orders Found.")
                    binding.shimmerViewContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun onOrderItemViewClick(orderedItems: OrderedItems){
        val bundle = Bundle()
        bundle.putInt("status", orderedItems.itemStatus!!)
        bundle.putString("orderId", orderedItems.orderId)
        bundle.putString("userAddress", orderedItems.userAddress)

        findNavController().navigate(R.id.action_orderFragment_to_orderDetailsFragment, bundle)
    }
}