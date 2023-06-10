package com.suka.superahorro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.suka.superahorro.R
import com.suka.superahorro.packages.*

class ItemDetailFragment : Fragment() {
    lateinit var v : View
    private val viewModel: ItemDetailViewModel by viewModels()

    lateinit var name : TextInputLayout
    lateinit var amount : TextInputLayout
    lateinit var price : TextInputLayout
    lateinit var total : TextInputLayout
    lateinit var model : TextInputLayout
    lateinit var sku : TextInputLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_item_detail, container, false)
        val args = ItemDetailFragmentArgs.fromBundle(requireArguments())
        viewModel.init(requireContext(), args.itemID)

        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = v.findViewById(R.id.txtName_det)
        amount = v.findViewById(R.id.txtAmount_det)
        price = v.findViewById(R.id.txtPrice_det)
        total = v.findViewById(R.id.txtTot_det)
        model = v.findViewById(R.id.txtModel_det)
        sku = v.findViewById(R.id.txtSku_det)

//        name = LayoutedInput(this, "Nombre", ::saveChanges, R.id.txtName_det, R.id.layName_det)
//        amount = LayoutedInput(this, "Cantidad", ::onUpdatePriceAmount, R.id.txtAmount_det, R.id.layAmout_det)
//        price = LayoutedInput(this, "Precio", ::onUpdatePriceAmount, R.id.txtPrice_det, R.id.layPrice_det)
//        total = LayoutedInput(this, "Total", ::onUpdateTotal, R.id.txtTot_det, R.id.layTot_det)
//        brand = LayoutedInput(this, "Marca", ::saveChanges, R.id.txtBrand_det, R.id.layBrand_det)
//        sku = LayoutedInput(this, "SKU", ::saveChanges, R.id.txtSku_det, R.id.laySku_det)
//        setPicture(viewModel.getCartItem().picture)
    }


    override fun onStart() {
        super.onStart()

        val cartItem = viewModel.getCartItem()
        name.editText?.setText(cartItem.name)
        amount.editText?.setText(cartItem.amount.toString())
        price.editText?.setText(cartItem.unit_price.toString())
        total.editText?.setText(cartItem.getTotalPrice().toString())
        //        brand.setText(cartItem.brand ?: "-")
        //        sku.setText(cartItem.sku ?: "-")

        setPicture(cartItem.picture)
    }

    override fun onResume() {
        super.onResume()

//        name.updateListener()
//        amount.updateListener()
//        price.updateListener()
    }


    fun onUpdateTotal(){
//        val amount = amount.getValue()?.value
//        val total = total.getValue()?.value
//        if(amount != null && total != null){
//            price.setValue(UnitValue(total / amount, GLOBAL_UNIT_PRICE))
//        }

        saveChanges()
    }

    fun onUpdatePriceAmount(){
//        val amount = amount.getValue()?.value
//        val price = price.getValue()?.value
//        if(amount != null && price != null){
//            total.setValue(UnitValue(amount * price, GLOBAL_UNIT_PRICE))
//        }

        saveChanges()
    }

    fun saveChanges(){
        val cartItem = viewModel.getCartItem()
        cartItem.name = name.editText?.text.toString()
        cartItem.amount = name.editText?.text.toString().toFloatOrNull()
        cartItem.unit_price = name.editText?.text.toString().toFloatOrNull()
//        cartItem.brand = brand.getText()
//        cartItem.sku = sku.getText()

        viewModel.updateCartItem(cartItem)
    }

    fun setPicture (picture_url: String?) {
        var img: ImageView = v.findViewById(R.id.img_det)
        if ( picture_url == null )
            img.setImageResource(R.drawable.default_item)
        else {
            Glide.with(img.context)
                .load(picture_url)
                .placeholder(R.drawable.default_item)
                .into(img)
        }
    }

}