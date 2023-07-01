package com.suka.superahorro.fragments.Cart

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.suka.superahorro.R
import com.suka.superahorro.activities.LoginActivity
import com.suka.superahorro.adapters.CartItemAdapter
import com.suka.superahorro.database.DbItemRef
import com.suka.superahorro.databinding.FragmentCartBinding
import com.suka.superahorro.packages.hideKeyboard
import com.suka.superahorro.packages.showKeyboard


class CartFragment : Fragment() {
    private val viewModel: CartViewModel by viewModels()
    private  lateinit var b: FragmentCartBinding
    private lateinit var toolbarMenu: Menu

    lateinit var adapter : CartItemAdapter
    var isAdding: MutableLiveData<Boolean> = MutableLiveData(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentCartBinding.inflate(inflater, container, false)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = CartFragmentArgs.fromBundle(requireArguments())
        viewModel.init(args.cart)

        initHiddens()
        viewModel.dbAuthError.observe(viewLifecycleOwner) { dbAuthError ->
            if (dbAuthError) {
                Snackbar.make(b.root, "Error de autenticación", Snackbar.LENGTH_SHORT).show()
                startActivity(Intent(context, LoginActivity::class.java))
                requireActivity().finish()
            }
        }

        viewModel.isInitialized.observe(viewLifecycleOwner) { isInitialized ->
            if (isInitialized) {
                initAdapter()
                initTopBar()
                updateCartTotal()
                updateAutoCompletes()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { newValue ->
            b.loading.visibility = if (newValue) View.VISIBLE else View.GONE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_cart, menu)
        toolbarMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = when(item.itemId) {
            R.id.toolbar_cart_add -> {
                isAdding.value = !isAdding.value!!
            }
            R.id.toolbar_cart_scan -> {
                scanBarcode()
            }
            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initAdapter() {
        adapter = CartItemAdapter(viewModel.cart,
            // OnClick
            { position ->
                val action = CartFragmentDirections.actionCartFragmentToItemDetailFragment(
                    cart = viewModel.cart,
                    itemPos = position
                )
                findNavController().navigate(action)
            },
            // OnLongClick
            { position ->
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Borrar item")
                builder.setMessage("¿Está seguro que desea eliminar el item?")
                builder.setPositiveButton("Sí") { _, _ ->
                    viewModel.deleteCartItem(position)
                    adapter.notifyDeleteItem(position)
                    Snackbar.make(b.root, "Item eliminado", Snackbar.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()
            }
        )
        b.recCartItems.layoutManager = LinearLayoutManager(context)
        b.recCartItems.adapter = adapter

        viewModel.onItemsChange = {
//            adapter.updateItems(viewModel.cartItems)
            updateCartTotal()
        }
    }


    private fun initTopBar() {
        isAdding.observe(viewLifecycleOwner) { isAdding ->
            b.newItemTxt.visibility = if (isAdding) View.VISIBLE else View.GONE
            b.cartTotalTxt.visibility = if (isAdding) View.GONE else View.VISIBLE
            toolbarMenu.findItem(R.id.toolbar_cart_add)?.setIcon(
                if (isAdding) R.drawable.icon_add_filled else R.drawable.icon_add
            )
            if ( isAdding == true ) {
                b.newItemTxt.setText("")
                showKeyboard(b.newItemTxt)
            }
            else {
                b.newItemBtOk.visibility = View.INVISIBLE
                hideKeyboard()
            }
        }

        b.newItemTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se utiliza en este caso
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                b.newItemBtOk.visibility = if (s.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                // No se utiliza en este caso
            }
        })

        b.newItemBtOk.setOnClickListener() {
            val itemName = b.newItemTxt.text.toString()
            viewModel.addNewItem(itemName) { newItem ->
                goToItemDetailNew(newItem.toRef())
            }
        }
        b.newItemTxt.setOnItemClickListener { parent, view, position, id ->
            val selectedOption = parent.getItemAtPosition(position) as DbItemRef
            goToItemDetailNew(selectedOption)
        }
    }

    private fun initHiddens() {
        b.newItemTxt.visibility = View.GONE
        b.newItemBtOk.visibility = View.GONE
        b.cartTotalTxt.visibility = View.GONE
        b.cartTotalTxt.visibility = View.GONE
    }


    private fun goToItemDetailNew(item: DbItemRef) {
        val newItem = viewModel.insertCartItem(item)
        val action = CartFragmentDirections.actionCartFragmentToItemDetailFragment(
            cart = viewModel.cart,
            itemPos = newItem.cartPos
        )
        findNavController().navigate(action)
        isAdding.value = false
    }


    fun updateCartTotal() {
        b.cartTotalTxt.text = adapter.getCartDescription()
    }


    fun updateAutoCompletes() {
        val items = viewModel.user.data.items
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, items)
        b.newItemTxt.setAdapter(adapter)
    }


    // zxing barcode
    fun onScanBarcode(result: String) {

    }
    fun scanBarcode() {
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
}