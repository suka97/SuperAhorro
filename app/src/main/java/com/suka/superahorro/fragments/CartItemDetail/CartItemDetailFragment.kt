package com.suka.superahorro.fragments.CartItemDetail

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.R
import com.suka.superahorro.databinding.FragmentCartItemDetailBinding
import com.suka.superahorro.packages.REQUEST_IMAGE_CAPTURE
import com.suka.superahorro.packages.number
import com.suka.superahorro.packages.requestImage
import com.suka.superahorro.packages.round
import com.suka.superahorro.packages.text
import com.suka.superahorro.packages.toStringNull

class CartItemDetailFragment : Fragment() {
    private val viewModel: CartItemDetailViewModel by viewModels()
    private  lateinit var b: FragmentCartItemDetailBinding

    private var autoCallbacksEnabled = true

    companion object {
        const val DIALOG_ADDMODEL_BYSKU = "Por SKU"
        const val DIALOG_ADDMODEL_BYNAME = "Por nombre"
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setFragmentResult("savedItem", bundleOf("savedItem" to viewModel.cartItem))
                findNavController().navigateUp()
            }
        })

        initHiddens()
        initViewModelObservers()
        initTexts()
        initButtons()
    }


    // get image from camera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            viewModel.uploadImage(imageBitmap) { url ->
                setPicture(url)
            }
        }
    }


    fun initHiddens() {
        b.modelCard.visibility = if (viewModel.cartItem.data.model!=null) View.VISIBLE else View.GONE
    }


    fun initViewModelObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            b.loading.visibility = if (it) View.VISIBLE else View.GONE
        }
    }


    fun initTexts() {
        val cartItem = viewModel.cartItem
        b.nameTxt.editText?.setText(cartItem.data.name)
        b.amountTxt.editText?.setText(cartItem.data.amount.toStringNull())
        b.unitPriceTxt.editText?.setText(cartItem.data.unit_price.toStringNull())
        b.totalPriceTxt.editText?.setText(cartItem.getTotalPrice().toStringNull())
        b.modelNameTxt.editText?.setText(cartItem.data.model?.name)
        b.modelSkuTxt.editText?.setText(cartItem.data.model?.sku)
        setPicture(cartItem.data.model?.img)

        // update callbacks
        b.amountTxt.editText?.addTextChangedListener(getTextWatcher(::onUpdatePriceAmount))
        b.unitPriceTxt.editText?.addTextChangedListener(getTextWatcher(::onUpdatePriceAmount))
        b.totalPriceTxt.editText?.addTextChangedListener(getTextWatcher(::onUpdateTotal))
    }


    fun initButtons() {
        b.modelNameTxt.setEndIconOnClickListener {
            Snackbar.make(b.root, "Icon clicked", Snackbar.LENGTH_SHORT).show()
        }

        b.modelImg.setOnClickListener{
            if (viewModel.isLoading.value == true) return@setOnClickListener
            if (viewModel.cartItem.data.model == null) {
                Snackbar.make(b.root, "Debe haber un modelo seleccionado", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (viewModel.cartItem.data.model!!.img == null) {
                requestImage()
            }
            else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Eliminar imagen")
                    .setMessage("¿Está seguro que desea eliminar la imagen?")
                    .setPositiveButton("Eliminar") { dialog, which ->
                        viewModel.deleteImage() {
                            setPicture(null)
                        }
                    }
                    .show()
            }
        }

        b.addModelBtn.setOnClickListener {
            handleAddModel()
        }
    }


    fun handleAddModel() {
        val items = arrayOf(DIALOG_ADDMODEL_BYSKU, DIALOG_ADDMODEL_BYNAME)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Vincular Modelo")
            .setItems(items) { dialog, which ->
                when(items[which]) {
                    DIALOG_ADDMODEL_BYSKU -> {

                    }

                    DIALOG_ADDMODEL_BYNAME -> {

                    }
                }
            }
            .show()
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