package com.example.blinkitadmin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.blinkitadmin.R
import com.example.blinkitadmin.adapters.AdapterCartProducts
import com.example.blinkitadmin.databinding.FragmentOrderDetailsBinding
import com.example.blinkitadmin.utils.Utils
import com.example.blinkitadmin.viewModels.AdminViewModel
import kotlinx.coroutines.launch

class OrderDetailsFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetailsBinding
    private val viewModel: AdminViewModel by viewModels()
    private lateinit var adapterCartProducts: AdapterCartProducts
    private var status = 0
    private var currentStatus = 0
    private var orderId = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailsBinding.inflate(layoutInflater)

        getValues()

        settingStatus(status)

        onChangeStatusBtnClick()

        onBackBtnClick()

        lifecycleScope.launch {
            getOrderedProducts()
        }

        return binding.root
    }

    private fun onChangeStatusBtnClick() {
        binding.btnChangeStatus.setOnClickListener{
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.menuInflater.inflate(R.menu.menu_popup, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { menu->
                when(menu.itemId){
                    R.id.menuReceived->{
                        currentStatus = 1
                        if(currentStatus > status){
                            status = 1
                            settingStatus(1)
                            viewModel.updateOrderStatus(orderId, 1)
                        }else{
                            Utils.showToast(requireContext(), "Order already received.")
                        }
                        true
                    }R.id.menuDispatched->{
                        currentStatus = 2
                        if(currentStatus > status){
                            status = 2
                            settingStatus(2)
                            viewModel.updateOrderStatus(orderId, 2)
                        }else{
                            Utils.showToast(requireContext(), "Order already dispatched.")
                        }
                        true
                    }R.id.menuDelivered->{
                        currentStatus = 3
                        if(currentStatus > status){
                            status = 3
                            settingStatus(3)
                            viewModel.updateOrderStatus(orderId, 3)
                        }else{
                            Utils.showToast(requireContext(), "Order already delivered.")
                        }
                        true
                    }else->{
                        false
                    }
                }
            }
        }
    }

    private fun onBackBtnClick() {
        binding.tbOrderDetailFragment.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_orderDetailsFragment_to_orderFragment)
        }
    }

    private suspend fun getOrderedProducts() {
        viewModel.getOrderedProducts(orderId).collect{cartList->
            adapterCartProducts = AdapterCartProducts()
            binding.rvProductsItems.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartList)
        }
    }

    private fun settingStatus(status: Int) {
        val greenColor = ContextCompat.getColorStateList(requireContext(), R.color.green)
        val statusViewsMap = mapOf(
            0 to arrayOf(binding.iv1),
            1 to arrayOf(binding.iv1, binding.iv2, binding.view1),
            2 to arrayOf(binding.iv1, binding.iv2, binding.iv3, binding.view1, binding.view2),
            3 to arrayOf(binding.iv1, binding.iv2, binding.iv3, binding.iv4, binding.view1, binding.view2, binding.view3)
        )

        statusViewsMap[status]?.forEach { it.backgroundTintList = greenColor }


    }

    private fun getValues() {
        val bundle = arguments
        status = bundle?.getInt("status")!!
        orderId = bundle.getString("orderId").toString()
        binding.tvUserAddress.text = bundle.getString("userAddress").toString()
    }
}