package com.suka.superahorro.fragments.ModelDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.R
import com.suka.superahorro.databinding.FragmentModelDetailBinding
import com.suka.superahorro.packages.setGlidePicture
import com.suka.superahorro.packages.setLoading
import com.suka.superahorro.packages.setText
import com.suka.superahorro.packages.text
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
            initButtons()

            // save changes on parent fragment
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    getTexts()
                    viewModel.saveModel {
                        findNavController().navigateUp()
                    }
                }
            })
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


    fun getTexts() {
        viewModel.model.data.name = b.nameTxt.text()
        viewModel.model.data.sku = b.modelSkuTxt.text()
        viewModel.model.data.brand = b.brandTxt.text()
        viewModel.model.data.content = b.contentTxt.text().toFloatOrNull()
        viewModel.model.data.sale_mode = b.saleModeTxt.text()
        viewModel.model.data.note = b.noteTxt.text()
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


    fun initButtons() {
        val units = viewModel.user.data.units
        var selecIndex = units.indexOfFirst { it.id == viewModel.model.data.base_unit }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, units)

        b.baseUnitTxt.editText?.setOnClickListener() {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Seleccione la unidad")
                .setNeutralButton("Cancelar") { dialog, which ->
                    // Respond to neutral button press
                }
                .setPositiveButton("Ok") { dialog, which ->
                    viewModel.model.data.base_unit = units[selecIndex].id
                    initTexts()
                }
                // Single-choice items (initialized with checked item)
                .setSingleChoiceItems(adapter, selecIndex) { dialog, which ->
                    selecIndex = which
                }
                .show()
        }
    }

}