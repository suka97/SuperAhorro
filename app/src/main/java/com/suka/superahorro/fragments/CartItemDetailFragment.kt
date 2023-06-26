package com.suka.superahorro.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.R
import com.suka.superahorro.databinding.FragmentCartItemDetailBinding
import com.suka.superahorro.packages.number
import com.suka.superahorro.packages.round
import com.suka.superahorro.packages.text
import com.suka.superahorro.packages.toStringNull

class CartItemDetailFragment : Fragment() {
    private val viewModel: CartItemDetailViewModel by viewModels()
    private  lateinit var b: FragmentCartItemDetailBinding

    private var autoCallbacksEnabled = true

    // Constantes
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentCartItemDetailBinding.inflate(inflater, container, false)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = CartItemDetailFragmentArgs.fromBundle(requireArguments())
        viewModel.init(args.cartItem)

        // save changes on parent fragment
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setFragmentResult("savedItem", bundleOf("savedItem" to viewModel.cartItem))
                findNavController().navigateUp()
            }
        })

        val cartItem = viewModel.cartItem
        b.nameTxt.editText?.setText(cartItem.data.name)
        b.amountTxt.editText?.setText(cartItem.data.amount.toStringNull())
        b.unitPriceTxt.editText?.setText(cartItem.data.unit_price.toStringNull())
        b.totalPriceTxt.editText?.setText(cartItem.getTotalPrice().toStringNull())
        b.modelNameTxt.editText?.setText(cartItem.data.model?.name)
        b.modelSkuTxt.editText?.setText(cartItem.data.model?.id)
        setPicture(cartItem.data.model?.img)

        // update callbacks
        b.amountTxt.editText?.addTextChangedListener(getTextWatcher(::onUpdatePriceAmount))
        b.unitPriceTxt.editText?.addTextChangedListener(getTextWatcher(::onUpdatePriceAmount))
        b.totalPriceTxt.editText?.addTextChangedListener(getTextWatcher(::onUpdateTotal))
        b.modelImg.setOnClickListener{
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            b.modelImg.setImageBitmap(imageBitmap)
        }
    }


    fun onUpdateTotal(){
        val amount: Float? = b.amountTxt.number()
        val total: Float? = b.totalPriceTxt.number()
        if(amount != null && total != null){
            val price = (total / amount).round(2)
            autoCallbacksEnabled = false
            b.unitPriceTxt.editText?.setText(price.toString())
            autoCallbacksEnabled = true
        }

        applyChanges()
    }

    fun onUpdatePriceAmount(){
        val amount: Float? = b.amountTxt.number()
        val price: Float? = b.unitPriceTxt.number()
        if(amount != null && price != null){
            val total = (amount * price).round(2)
            autoCallbacksEnabled = false
            b.totalPriceTxt.editText?.setText(total.toString())
            autoCallbacksEnabled = true
        }

        applyChanges()
    }

    fun applyChanges(){
        viewModel.cartItem.data.name = b.nameTxt.text()
        viewModel.cartItem.data.amount = b.amountTxt.number()
        viewModel.cartItem.data.unit_price = b.unitPriceTxt.number()
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