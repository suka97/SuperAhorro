package com.suka.superahorro.fragments.ModelDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.R
import com.suka.superahorro.databinding.FragmentCartItemDetailBinding
import com.suka.superahorro.databinding.FragmentModelDetailBinding
import com.suka.superahorro.fragments.CartItemDetail.CartItemDetailFragmentArgs
import com.suka.superahorro.fragments.CartItemDetail.CartItemDetailViewModel
import com.suka.superahorro.packages.round
import com.suka.superahorro.packages.setGlidePicture
import com.suka.superahorro.packages.setLoading
import com.suka.superahorro.packages.setText
import com.suka.superahorro.packages.toStringNull

class ModelDetailFragment : Fragment() {
    private val viewModel: ModelDetailViewModel by viewModels()
    private  lateinit var b: FragmentModelDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentModelDetailBinding.inflate(inflater, container, false)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = ModelDetailFragmentArgs.fromBundle(requireArguments())
        viewModel.init(args.modelRef, args.parentItem) {
            initTexts()
        }

        initViewModelObservers()
    }


    fun initTexts() {
        b.nameTxt.setText(viewModel.model.data.name)
        b.modelSkuTxt.setText(viewModel.model.data.sku)
        b.parentNameTxt.setText(viewModel.model.data.item.name)
        b.brandTxt.setText(viewModel.model.data.brand)
        b.contentTxt.setText(viewModel.model.data.content.toStringNull())
        b.baseUnitTxt.setText(viewModel.user.getUnit(viewModel.model.data.base_unit)?.name_long)
        b.saleModeTxt.setText(viewModel.model.data.sale_mode)
        b.noteTxt.setText(viewModel.model.data.note)
        setPicture(viewModel.model.data.img)
    }


    fun setPicture (picture_url: String?) {
        setGlidePicture(url=picture_url, imgView=b.modelImg, placeholder=R.drawable.default_item,
            onSuccess = {
                viewModel.isImgLoading.value = false
            },
            onError = {
                Snackbar.make(b.root, "Error al cargar la imagen", Snackbar.LENGTH_SHORT).show()
                viewModel.isImgLoading.value = false
            }
        )
    }


    fun initViewModelObservers() {
//        viewModel.isImgLoading.observe(viewLifecycleOwner) { isLoading ->
//            b.imgLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
//        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }
    }

}