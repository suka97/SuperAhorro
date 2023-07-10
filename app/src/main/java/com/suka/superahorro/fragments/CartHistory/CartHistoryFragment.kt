package com.suka.superahorro.fragments.CartHistory

import android.app.AlertDialog
import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.R
import com.suka.superahorro.adapters.CartAdapter
import com.suka.superahorro.databinding.FragmentCartHistoryBinding
import com.suka.superahorro.databinding.FragmentListCartsBinding
import com.suka.superahorro.fragments.ListCarts.ListCartsViewModel
import com.suka.superahorro.packages.createInputDialog
import com.suka.superahorro.packages.setLoading

class CartHistoryFragment : Fragment() {
    private val viewModel: CartHistoryViewModel by viewModels()
    private  lateinit var b: FragmentCartHistoryBinding

    lateinit var adapter : CartAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentCartHistoryBinding.inflate(inflater, container, false)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }
        viewModel.init() {
            initAdapter()
        }
    }


    private fun initAdapter() {
        adapter = CartAdapter(viewModel.carts,
            // OnClick
            { position ->
//                val action =
//                    ListCartsFragmentDirections.actionListCartsFragmentToCartFragment(viewModel.carts[position])
//                findNavController().navigate(action)
            },
            // OnLongClick
            { position ->
                // NO SE PUEDE BORRAR UN CARRITO YA CERRADO
            }
        )
        b.recCarts.layoutManager = LinearLayoutManager(context)
        b.recCarts.adapter = adapter
    }

}