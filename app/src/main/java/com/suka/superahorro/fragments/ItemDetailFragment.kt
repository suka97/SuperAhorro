package com.suka.superahorro.fragments

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.suka.superahorro.R
import com.suka.superahorro.databinding.FragmentItemDetailBinding
import com.suka.superahorro.packages.*

class ItemDetailFragment : Fragment() {
    lateinit var v : View
    private val viewModel: ItemDetailViewModel by viewModels()
    private  lateinit var binding: FragmentItemDetailBinding

    private var autoCallbacksEnabled = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = ItemDetailFragmentArgs.fromBundle(requireArguments())
        viewModel.init(requireContext(), args.itemID)
        binding = FragmentItemDetailBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()

        val cartItem = viewModel.getCartItem()
        binding.nameTxt.editText?.setText(cartItem.name)
        binding.amountTxt.editText?.setText(cartItem.amount.toString())
        binding.unitPriceTxt.editText?.setText(cartItem.unit_price.toString())
        binding.totalPriceTxt.editText?.setText(cartItem.getTotalPrice().toString())
//        brand.setText(cartItem.brand ?: "-")
//        sku.setText(cartItem.sku ?: "-")
        setPicture(viewModel.getCartItem().picture)

        binding.amountTxt.editText?.addTextChangedListener(getTextWatcher(::onUpdatePriceAmount))
        binding.unitPriceTxt.editText?.addTextChangedListener(getTextWatcher(::onUpdatePriceAmount))
        binding.totalPriceTxt.editText?.addTextChangedListener(getTextWatcher(::onUpdateTotal))


    }

    override fun onResume() {
        super.onResume()

//        name.updateListener()
//        amount.updateListener()
//        price.updateListener()
    }


    fun onUpdateTotal(){
        val amount: Float? = binding.amountTxt.editText?.text.toString().toFloatOrNull()
        val total: Float? = binding.totalPriceTxt.editText?.text.toString().toFloatOrNull()
        if(amount != null && total != null){
            val price = total / amount
            autoCallbacksEnabled = false
            binding.unitPriceTxt.editText?.setText(price.toString())
            autoCallbacksEnabled = true
        }

        saveChanges()
    }

    fun onUpdatePriceAmount(){
        val amount: Float? = binding.amountTxt.editText?.text.toString().toFloatOrNull()
        val price: Float? = binding.unitPriceTxt.editText?.text.toString().toFloatOrNull()
        if(amount != null && price != null){
            val total = amount * price
            autoCallbacksEnabled = false
            binding.totalPriceTxt.editText?.setText(total.toString())
            autoCallbacksEnabled = true
        }

        saveChanges()
    }

    fun saveChanges(){
        val cartItem = viewModel.getCartItem()
        cartItem.name = binding.nameTxt.editText?.text.toString()
        cartItem.amount = binding.amountTxt.editText?.text.toString().toFloatOrNull()
        cartItem.unit_price = binding.unitPriceTxt.editText?.text.toString().toFloatOrNull()
//        cartItem.brand = brand.getText()
//        cartItem.sku = sku.getText()

        viewModel.updateCartItem(cartItem)
    }

    fun setPicture (picture_url: String?) {
        var img: ImageView = binding.modelImg
        if ( picture_url == null )
            img.setImageResource(R.drawable.default_item)
        else {
            Glide.with(img.context)
                .load(picture_url)
                .placeholder(R.drawable.default_item)
                .into(img)
        }
    }

    private fun getTextWatcher(callback: ()->Unit): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                if (autoCallbacksEnabled)
                    callback()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        }
    }

}