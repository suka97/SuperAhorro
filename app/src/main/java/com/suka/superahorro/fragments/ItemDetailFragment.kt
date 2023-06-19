package com.suka.superahorro.fragments

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.suka.superahorro.R
import com.suka.superahorro.databinding.FragmentItemDetailBinding
import com.suka.superahorro.dbclasses.CartItem
import com.suka.superahorro.packages.round

class ItemDetailFragment : Fragment() {
    private val viewModel: ItemDetailViewModel by viewModels()
    private  lateinit var b: FragmentItemDetailBinding

    private var autoCallbacksEnabled = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = ItemDetailFragmentArgs.fromBundle(requireArguments())
        viewModel.init(requireContext(), args.cartItem)
        b = FragmentItemDetailBinding.inflate(inflater, container, false)

        // save changes on parent fragment
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setFragmentResult("savedItem", bundleOf("savedItem" to viewModel.cartItem))
                findNavController().navigateUp()
            }
        })

        return b.root
    }


    override fun onStart() {
        super.onStart()

        val cartItem = viewModel.cartItem
        b.nameTxt.editText?.setText(cartItem.data.name)
        b.amountTxt.editText?.setText(cartItem.data.amount.toString())
        b.unitPriceTxt.editText?.setText(cartItem.data.unit_price.toString())
        b.totalPriceTxt.editText?.setText(cartItem.getTotalPrice().toString())
        b.modelNameTxt.editText?.setText(cartItem.data.model?.name)
        b.modelSkuTxt.editText?.setText(cartItem.data.model?.id)
//        setPicture(viewModel.getCartItem().picture)

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
            val price = (total / amount).round(2)
            autoCallbacksEnabled = false
            b.unitPriceTxt.editText?.setText(price.toString())
            autoCallbacksEnabled = true
        }

        applyChanges()
    }

    fun onUpdatePriceAmount(){
        val amount: Float? = b.amountTxt.editText?.text.toString().toFloatOrNull()
        val price: Float? = b.unitPriceTxt.editText?.text.toString().toFloatOrNull()
        if(amount != null && price != null){
            val total = (amount * price).round(2)
            autoCallbacksEnabled = false
            b.totalPriceTxt.editText?.setText(total.toString())
            autoCallbacksEnabled = true
        }

        applyChanges()
    }

    fun applyChanges(){
        viewModel.cartItem.data.name = b.nameTxt.editText?.text.toString()
        viewModel.cartItem.data.amount = b.amountTxt.editText?.text.toString().toFloatOrNull()
        viewModel.cartItem.data.unit_price = b.unitPriceTxt.editText?.text.toString().toFloatOrNull()
//        viewModel.cartItem.data.model. = brand.getText()
//        viewModel.cartItem.data.sku = sku.getText()

//        viewModel.updateCartItem(cartItem)
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