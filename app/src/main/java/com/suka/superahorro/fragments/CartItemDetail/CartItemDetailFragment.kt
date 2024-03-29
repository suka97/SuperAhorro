package com.suka.superahorro.fragments.CartItemDetail

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.suka.superahorro.R
import com.suka.superahorro.database.DbModelRef
import com.suka.superahorro.databinding.FragmentCartItemDetailBinding
import com.suka.superahorro.packages.REQUEST_IMAGE_CAPTURE
import com.suka.superahorro.packages.SimpleTextWatcher
import com.suka.superahorro.packages.createAutoCompleteDialog
import com.suka.superahorro.packages.createInputDialog
import com.suka.superahorro.packages.number
import com.suka.superahorro.packages.numberOrNull
import com.suka.superahorro.packages.requestImage
import com.suka.superahorro.packages.round
import com.suka.superahorro.packages.setGlidePicture
import com.suka.superahorro.packages.setLoading
import com.suka.superahorro.packages.setNumber
import com.suka.superahorro.packages.setText
import com.suka.superahorro.packages.text
import com.suka.superahorro.packages.toSimpleTextWatcher
import com.suka.superahorro.packages.toStringNull


class CartItemDetailFragment : Fragment(), CartItemDetailViewModel.FragmentNotifier {
    private val viewModel: CartItemDetailViewModel by viewModels()
    private  lateinit var b: FragmentCartItemDetailBinding
    private lateinit var toolbarMenu: Menu

    private var autoCallbacksEnabled = true

    companion object {
        const val DIALOG_ADDMODEL_BYSKU = "Por SKU"
        const val DIALOG_ADDMODEL_BYNAME = "Por nombre"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        viewModel.init(args.cart, args.itemPos, this)

        // save changes on parent fragment
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.saveCartChanges {
                    findNavController().navigateUp()
                }
            }
        })

        initViewModelObservers()
        initButtons()
        onItemUpdated()
        initTextCallbacks()
        initChangesListeners()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_cart_item, menu)
        toolbarMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = when(item.itemId) {
            R.id.toolbar_cart_item_open_model -> {
                goToModelDetail()
            }
            R.id.toolbar_cart_item_refresh_model -> {
                viewModel.refreshModel()
            }
            else -> ""
        }
        return super.onOptionsItemSelected(item)
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


    fun goToModelDetail() {
        val model: DbModelRef? = viewModel.cartItem.toModelRef()
        if ( model == null ) {
            Snackbar.make(requireView(), "No hay modelo seleccionado", Snackbar.LENGTH_SHORT).show()
            return
        }

        val action = CartItemDetailFragmentDirections.actionCartItemDetailFragmentToModelDetailFragment(
            modelRef = model,
            parentItem = viewModel.cartItem.toItemRef()
        )
        findNavController().navigate(action)
    }


    fun initHiddens() {
        b.modelCard.visibility = if (viewModel.cartItem.data.model!=null) View.VISIBLE else View.GONE
        b.addModelBtn.visibility = if (viewModel.cartItem.data.model!=null) View.GONE else View.VISIBLE
    }


    fun initViewModelObservers() {
        viewModel.isImgLoading.observe(viewLifecycleOwner) { isLoading ->
            b.imgLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }
    }


    fun initTexts() {
        val cartItem = viewModel.cartItem
        b.nameTxt.setText(cartItem.data.name)
        b.amountTxt.setText(cartItem.data.amount.toStringNull())
        b.unitPriceTxt.setText(cartItem.data.unit_price.toStringNull())
        b.totalPriceTxt.setText(cartItem.getTotalPrice().toStringNull())
        b.modelNameTxt.setText(cartItem.data.model?.name)
        b.modelSkuTxt.setText(cartItem.data.model?.sku)
        b.modelContentTxt.setText(cartItem.data.model?.content.toStringNull())
        b.modelUnitShortTxt.setText(cartItem.getBaseUnit())
        b.modelBrandTxt.setText(cartItem.data.model?.brand)
        b.modelSaleModeTxt.setText(cartItem.data.model?.sale_mode)
        b.modelNoteTxt.setText(cartItem.data.model?.note)
        setPicture(cartItem.data.model?.img)
        b.checkAdded.isChecked = cartItem.data.checked
    }

    fun initSuffixes() {
        val cartItem = viewModel.cartItem
        b.amountTxt.suffixText = cartItem.getMeasureUnit()
        b.unitPriceTxt.suffixText = cartItem.getUnitPrice()
    }


    fun initButtons() {
        b.modelImg.setOnLongClickListener(){
            if (viewModel.isLoading.value == true) return@setOnLongClickListener true
            if (viewModel.cartItem.data.model != null) {
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
            true
        }

        b.addModelBtn.setOnClickListener {
            viewModel.getItemData {
                handleAddModel()
            }
        }

        b.removeModelBtn.setOnClickListener {
            viewModel.unlinkModel()
        }

        b.modelSkuTxt.setEndIconOnClickListener {
            scanBarcode { barcode ->
                viewModel.getModelBySku(barcode) { model ->
                    if ( model == null ) b.modelSkuTxt.setText(barcode)
                    else {
                        Snackbar.make(requireView(), "El SKU ya está en uso", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    fun handleAddModel() {
        val items = arrayOf(DIALOG_ADDMODEL_BYSKU, DIALOG_ADDMODEL_BYNAME)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Vincular Modelo")
            .setItems(items) { dialog, which ->
                when(items[which]) {
                    DIALOG_ADDMODEL_BYSKU -> {
                        scanBarcode{ sku: String ->
                            viewModel.getModelBySku(sku) { model ->
                                if ( model == null ) {
                                    newModelWithSku(sku)
                                }
                                else {
                                    viewModel.cartItem.linkModel(model)
                                    onItemUpdated()
                                }
                            }
                        }
                    }

                    DIALOG_ADDMODEL_BYNAME -> {
                        createAutoCompleteDialog("Nombre", viewModel.itemDetail!!.data.models,
                            onOkClicked = { modelName ->
                                viewModel.linkNewModel(modelName)
                            },
                            onOptionSelected = { model ->
                                viewModel.linkModelById(model.id)
                            }
                        )
                    }
                }
            }
            .show()
    }


    fun newModelWithSku(sku: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("SKU no encontrado")
            .setMessage("No se encontró un modelo con el SKU $sku. ¿Desea crear uno nuevo?")
            .setPositiveButton("Crear") { dialog, which ->
                val dialog = Dialog(requireActivity())
                createInputDialog(dialog, "Nombre del modelo", "") { name ->
                    viewModel.linkNewModel(name, sku)
                }
            }
            .show()
    }


    fun onUpdateTotal(newValue: String){
        if (!autoCallbacksEnabled) return
        autoCallbacksEnabled = false
        viewModel.cartItem.calcUnitPriceFromTotal(b.totalPriceTxt.numberOrNull())
        b.unitPriceTxt.setNumber(viewModel.cartItem.data.unit_price)
        autoCallbacksEnabled = true

        applyChanges()
    }


    fun onUpdatePriceAmount(newValue: String){
        if (!autoCallbacksEnabled) return
        applyChanges()

        autoCallbacksEnabled = false
        b.totalPriceTxt.setNumber(viewModel.cartItem.getTotalPrice())
        autoCallbacksEnabled = true
    }


    fun applyChanges(){
        if (!autoCallbacksEnabled) return

        viewModel.cartItem.data.name = b.nameTxt.text()
        viewModel.cartItem.data.amount = b.amountTxt.numberOrNull()
        viewModel.cartItem.data.unit_price = b.unitPriceTxt.numberOrNull()
        viewModel.cartItem.data.checked = b.checkAdded.isChecked

        viewModel.cartItem.data.model?.name = b.modelNameTxt.text()
        viewModel.cartItem.data.model?.sku = b.modelSkuTxt.text()
        viewModel.cartItem.data.model?.content = b.modelContentTxt.numberOrNull()
        viewModel.cartItem.data.model?.brand = b.modelBrandTxt.text()
        viewModel.cartItem.data.model?.sale_mode = b.modelSaleModeTxt.text()
        viewModel.cartItem.data.model?.note = b.modelNoteTxt.text()
    }


    fun setPicture (picture_url: String?) {
        setGlidePicture(url=picture_url, imgView=b.modelImg, placeholder=R.drawable.default_item,
            onNullClick = {
                if (viewModel.cartItem.data.model == null)
                    Snackbar.make(b.root, "Debe haber un modelo seleccionado", Snackbar.LENGTH_SHORT).show()
                else requestImage()
            },
            onSuccess = {
                viewModel.isImgLoading.value = false
            },
            onError = {
                Snackbar.make(b.root, "Error al cargar la imagen", Snackbar.LENGTH_SHORT).show()
                viewModel.isImgLoading.value = false
            }
        )
    }


    // zxing barcode
    lateinit var onScanBarcode: (result: String)->Unit
    fun scanBarcode(callback: (String)->Unit) {
        onScanBarcode = callback

        val options = ScanOptions()
        options.setBeepEnabled(false)
//        options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES)
//        options.setPrompt("Scan a barcode")
//        options.setCameraId(0) // Use a specific camera of the device
//        options.setBarcodeImageEnabled(true)
        barcodeLauncher.launch(options)
    }
    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents != null) {
            onScanBarcode(result.contents)
        }
    }


    override fun onItemUpdated() {
        autoCallbacksEnabled = false
        initHiddens()
        initTexts()
        initSuffixes()
        autoCallbacksEnabled = true
    }


    fun initTextCallbacks() {
        b.amountTxt.editText?.addTextChangedListener(SimpleTextWatcher(::onUpdatePriceAmount))
        b.unitPriceTxt.editText?.addTextChangedListener(SimpleTextWatcher(::onUpdatePriceAmount))
        b.totalPriceTxt.editText?.addTextChangedListener(SimpleTextWatcher(::onUpdateTotal))
    }


    fun initChangesListeners() {
        // item
        b.amountTxt.editText?.addTextChangedListener(onTextWatcherCartItem())
        b.unitPriceTxt.editText?.addTextChangedListener(onTextWatcherCartItem())
        b.totalPriceTxt.editText?.addTextChangedListener(onTextWatcherCartItem())
        initCheckAdded()
        // model
        b.modelNameTxt.editText?.addTextChangedListener(onTextWatcherModel())
        b.modelSkuTxt.editText?.addTextChangedListener(onTextWatcherModel())
        b.modelContentTxt.editText?.addTextChangedListener(onTextWatcherModel())
        b.modelUnitShortTxt.editText?.addTextChangedListener(onTextWatcherModel())
        b.modelBrandTxt.editText?.addTextChangedListener(onTextWatcherModel())
        b.modelSaleModeTxt.editText?.addTextChangedListener(onTextWatcherModel())
        b.modelNoteTxt.editText?.addTextChangedListener(onTextWatcherModel())
    }

    fun initCheckAdded() {
        b.checkAdded.setOnCheckedChangeListener { _, _ ->
            viewModel.itemChanges = true
            applyChanges()
        }
        b.txtCheckAdded.setOnClickListener {
            b.checkAdded.isChecked = !b.checkAdded.isChecked
        }
    }

    fun onTextWatcherCartItem(): SimpleTextWatcher {
        return SimpleTextWatcher {
            viewModel.itemChanges = true
            applyChanges()
        }
    }
    fun onTextWatcherModel(): SimpleTextWatcher {
        return SimpleTextWatcher {
            viewModel.modelChanges = true
            applyChanges()
            initSuffixes()  // si cambian las cantidades, se muestra distintos sufijos
        }
    }
}