package com.suka.superahorro.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.R
import com.suka.superahorro.adapters.CartItemAdapter
import com.suka.superahorro.database.AppDatabase
import com.suka.superahorro.database.CartItemDao
import com.suka.superahorro.entities.CartItem
import com.suka.superahorro.packages.createInputDialog

class CartFragment : Fragment() {
    lateinit var v : View
    private val viewModel: CartViewModel by viewModels()

    lateinit var recCartItems : RecyclerView
    lateinit var adapter : CartItemAdapter
    lateinit var btCardAdd : ImageButton
    lateinit var txtCartTotal : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_cart, container, false)
        viewModel.init(requireContext())

        recCartItems = v.findViewById(R.id.recCartItems)
        btCardAdd = v.findViewById(R.id.btCardAdd)
        txtCartTotal = v.findViewById(R.id.txtCartTotal)
        return v
    }


    override fun onStart() {
        super.onStart()

        btCardAdd.setOnClickListener{
            val dialog = Dialog(requireActivity())
            createInputDialog(dialog, "Nuevo Item", "") { name ->
                viewModel.insertCartItem( CartItem(name) )
                Snackbar.make(v, "Item agregado", Snackbar.LENGTH_SHORT).show()
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
                    Snackbar.make(v, "Item eliminado", Snackbar.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()
            }
        )
        recCartItems.layoutManager = LinearLayoutManager(context)
        recCartItems.adapter = adapter

        viewModel.onItemsChange = {
            adapter.updateItems(viewModel.cartItems)
            txtCartTotal.text = adapter.getCartDescription()
        }
        viewModel.updateItems()
    }


    override fun onResume() {
        super.onResume()
        viewModel.updateItems()
    }

}