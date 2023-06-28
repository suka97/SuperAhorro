package com.suka.superahorro.fragments

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.R
import com.suka.superahorro.adapters.CartAdapter
import com.suka.superahorro.adapters.CartItemAdapter
import com.suka.superahorro.databinding.FragmentCartBinding
import com.suka.superahorro.databinding.FragmentListCartsBinding

class ListCartsFragment : Fragment() {
    private val viewModel: ListCartsViewModel by viewModels()
    private  lateinit var b: FragmentListCartsBinding

    lateinit var adapter : CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentListCartsBinding.inflate(inflater, container, false)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) {
            b.loading.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.init() {
            initAdapter()
        }
    }


    private fun initAdapter() {
        adapter = CartAdapter(viewModel.carts,
            // OnClick
            { position ->
                val action = ListCartsFragmentDirections.actionListCartsFragmentToCartFragment(viewModel.carts[position])
                findNavController().navigate(action)
            },
            // OnLongClick
            { position ->
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Borrar item")
                builder.setMessage("¿Está seguro que desea eliminar el item?")
                builder.setPositiveButton("Sí") { _, _ ->
                    //viewModel.deleteCartItem(position)
                    adapter.notifyDeleteItem(position)
                    Snackbar.make(b.root, "Item eliminado", Snackbar.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()
            }
        )
        b.recCarts.layoutManager = LinearLayoutManager(context)
        b.recCarts.adapter = adapter

//        viewModel.onItemsChange = {
////            adapter.updateItems(viewModel.cartItems)
//            updateCartTotal()
//        }
//        viewModel.updateItems()
    }

}