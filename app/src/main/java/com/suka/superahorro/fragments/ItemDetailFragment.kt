package com.suka.superahorro.fragments

import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.suka.superahorro.R
import com.suka.superahorro.databinding.FragmentItemDetailBinding

class ItemDetailFragment : Fragment() {
    lateinit var v : View
    private val viewModel: ItemDetailViewModel by viewModels()
    private  lateinit var b: FragmentItemDetailBinding

    private var autoCallbacksEnabled = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = ItemDetailFragmentArgs.fromBundle(requireArguments())
        viewModel.init(requireContext(), args.itemID)
        b = FragmentItemDetailBinding.inflate(inflater, container, false)

        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()

        val cartItem = viewModel.getCartItem()
        b.nameTxt.editText?.setText(cartItem.name)
        b.amountTxt.editText?.setText(cartItem.amount.toString())
        b.unitPriceTxt.editText?.setText(cartItem.unit_price.toString())
        b.totalPriceTxt.editText?.setText(cartItem.getTotalPrice().toString())
//        brand.setText(cartItem.brand ?: "-")
//        sku.setText(cartItem.sku ?: "-")
        setPicture(viewModel.getCartItem().picture)

        // update callbacks
        b.amountTxt.editText?.addTextChangedListener(getTextWatcher(::onUpdatePriceAmount))
        b.unitPriceTxt.editText?.addTextChangedListener(getTextWatcher(::onUpdatePriceAmount))
        b.totalPriceTxt.editText?.addTextChangedListener(getTextWatcher(::onUpdateTotal))
    }

    override fun onResume() {
        super.onResume()

//        name.updateListener()
//        amount.updateListener()
//        price.updateListener()
    }


    fun onUpdateTotal(){
        val amount: Float? = b.amountTxt.editText?.text.toString().toFloatOrNull()
        val total: Float? = b.totalPriceTxt.editText?.text.toString().toFloatOrNull()
        if(amount != null && total != null){
            val price = total / amount
            autoCallbacksEnabled = false
            b.unitPriceTxt.editText?.setText(price.toString())
            autoCallbacksEnabled = true
        }

        saveChanges()
    }

    fun onUpdatePriceAmount(){
        val amount: Float? = b.amountTxt.editText?.text.toString().toFloatOrNull()
        val price: Float? = b.unitPriceTxt.editText?.text.toString().toFloatOrNull()
        if(amount != null && price != null){
            val total = amount * price
            autoCallbacksEnabled = false
            b.totalPriceTxt.editText?.setText(total.toString())
            autoCallbacksEnabled = true
        }

        saveChanges()
    }

    fun saveChanges(){
        val cartItem = viewModel.getCartItem()
        cartItem.name = b.nameTxt.editText?.text.toString()
        cartItem.amount = b.amountTxt.editText?.text.toString().toFloatOrNull()
        cartItem.unit_price = b.unitPriceTxt.editText?.text.toString().toFloatOrNull()
//        cartItem.brand = brand.getText()
//        cartItem.sku = sku.getText()

        viewModel.updateCartItem(cartItem)
    }

    fun setPicture (picture_url: String?) {
        var img: ImageView = b.modelImg
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