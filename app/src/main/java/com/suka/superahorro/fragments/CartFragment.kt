package com.suka.superahorro.fragments

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.manager.Lifecycle
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.adapters.CartItemAdapter
import com.suka.superahorro.databinding.FragmentCartBinding
import com.suka.superahorro.dbclasses.CartItem
import com.suka.superahorro.packages.createInputDialog

class CartFragment : Fragment() {
    private val viewModel: CartViewModel by viewModels()
    private  lateinit var b: FragmentCartBinding

    lateinit var adapter : CartItemAdapter
    var isAdding: MutableLiveData<Boolean> = MutableLiveData(false)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentCartBinding.inflate(inflater, container, false)
        viewModel.init(requireContext()) {
            initTopBar()
            initButtons()
            initAdapter()
            updateCartTotal()
            updateAutoCompletes()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { newValue ->
            b.loading.visibility = if (newValue) View.VISIBLE else View.GONE
        }

        // save changes from detail view
        setFragmentResultListener("savedItem") { key, bundle ->
            val cartItem = bundle.getParcelable<CartItem>("savedItem")
            if ( cartItem != null) {
                viewModel.cart.setItem(cartItem)
                viewModel.saveCartChanges()
                updateCartTotal()
            }
        }

        return b.root
    }


    override fun onResume() {
        super.onResume()
        viewModel.updateItems()
    }


    private fun initButtons() {
        b.addItemBt.setOnClickListener {
            isAdding.value = !isAdding.value!!
        }
    }


    private fun initAdapter() {
        adapter = CartItemAdapter(viewModel.cart,
            // OnClick
            { position ->
                val action = CartFragmentDirections.actionCartFragmentToItemDetailFragment(viewModel.cart.getItem(position))
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
        viewModel.updateItems()
    }


    private fun initTopBar() {
        isAdding.observe(viewLifecycleOwner) { newValue ->
            b.newItemTxt.visibility = if (newValue) View.VISIBLE else View.GONE
            b.cartTotalTxt.visibility = if (newValue) View.GONE else View.VISIBLE
            if ( newValue == true ) {
                b.newItemTxt.setText("")
                b.newItemTxt.requestFocus()
            }
            else {
                b.newItemBtOk.visibility = View.GONE
            }
        }

        b.newItemTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se utiliza en este caso
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                b.newItemBtOk.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                // No se utiliza en este caso
            }
        })

        b.newItemBtOk.setOnClickListener() {
            goToItemDetailNew(b.newItemTxt.text.toString())
        }
        b.newItemTxt.setOnItemClickListener { parent, view, position, id ->
            val selectedOption = parent.getItemAtPosition(position).toString()
            goToItemDetailNew(selectedOption)
        }
    }


    private fun goToItemDetailNew(item_name: String) {
        val newItem = viewModel.newCartItem(item_name)
        val action = CartFragmentDirections.actionCartFragmentToItemDetailFragment(newItem)
        findNavController().navigate(action)
        isAdding.value = false
    }


    fun updateCartTotal() {
        b.cartTotalTxt.text = adapter.getCartDescription()
    }


    fun updateAutoCompletes() {
        val items = listOf("Aceite", "Arroz", "Arroz1", "Arroz2", "Fideos", "Leche", "Pan", "Queso", "Yogurt")

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, items)
//        b.newItemTxt.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//        b.newItemTxt.dropDownVerticalOffset = -autoCompleteTextView.height
        b.newItemTxt.setAdapter(adapter)
    }

}