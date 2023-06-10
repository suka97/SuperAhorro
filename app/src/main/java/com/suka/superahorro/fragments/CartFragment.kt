package com.suka.superahorro.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.adapters.CartItemAdapter
import com.suka.superahorro.databinding.FragmentCartBinding
import com.suka.superahorro.my_entities.CartItem
import com.suka.superahorro.packages.createInputDialog

class CartFragment : Fragment() {
    private val viewModel: CartViewModel by viewModels()
    private  lateinit var b: FragmentCartBinding

    lateinit var adapter : CartItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.init(requireContext())
        b = FragmentCartBinding.inflate(inflater, container, false)

        return b.root
    }


    override fun onStart() {
        super.onStart()

        b.addItemBt.setOnClickListener{
            val dialog = Dialog(requireActivity())
            createInputDialog(dialog, "Nuevo Item", "") { name ->
                viewModel.insertCartItem( CartItem(name) )
                Snackbar.make(b.root, "Item agregado", Snackbar.LENGTH_SHORT).show()
            }
        }

        adapter = CartItemAdapter(viewModel.cartItems,
            // OnClick
            { position ->
                val action = CartFragmentDirections.actionCartFragmentToItemDetailFragment(viewModel.cartItems[position].id)
                findNavController().navigate(action)
            },
            // OnLongClick
            { position ->
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Borrar item")
                builder.setMessage("¿Está seguro que desea eliminar el item?")
                builder.setPositiveButton("Sí") { _, _ ->
                    viewModel.deleteCartItem(viewModel.cartItems[position])
                    Snackbar.make(b.root, "Item eliminado", Snackbar.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()
            }
        )
        b.recCartItems.layoutManager = LinearLayoutManager(context)
        b.recCartItems.adapter = adapter

        viewModel.onItemsChange = {
            adapter.updateItems(viewModel.cartItems)
            b.cartTotalTxt.text = adapter.getCartDescription()
        }
        viewModel.updateItems()
    }


    override fun onResume() {
        super.onResume()
        viewModel.updateItems()
    }

}