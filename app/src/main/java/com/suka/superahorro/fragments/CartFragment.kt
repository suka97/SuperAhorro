package com.suka.superahorro.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.adapters.CartItemAdapter
import com.suka.superahorro.databinding.FragmentCartBinding
import com.suka.superahorro.dbclasses.CartItem
import com.suka.superahorro.packages.createInputDialog

class CartFragment : Fragment() {
    private val viewModel: CartViewModel by viewModels()
    private  lateinit var b: FragmentCartBinding

    lateinit var adapter : CartItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentCartBinding.inflate(inflater, container, false)
        viewModel.init(requireContext()) {
            initButtons()
            initAdapter()
            updateCartTotal()
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
            val dialog = Dialog(requireActivity())
            createInputDialog(dialog, "Nuevo Item", "") { name ->
                val newItem = viewModel.newCartItem(name)
                val action = CartFragmentDirections.actionCartFragmentToItemDetailFragment(newItem)
                findNavController().navigate(action)
            }
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


    fun updateCartTotal() {
        b.cartTotalTxt.text = adapter.getCartDescription()
    }

}