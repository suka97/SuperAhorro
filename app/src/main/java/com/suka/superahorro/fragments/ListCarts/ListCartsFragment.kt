package com.suka.superahorro.fragments.ListCarts

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.R
import com.suka.superahorro.adapters.CartAdapter
import com.suka.superahorro.databinding.FragmentListCartsBinding
import com.suka.superahorro.dbclasses.Cart
import com.suka.superahorro.packages.createInputDialog


class ListCartsFragment : Fragment() {
    private val viewModel: ListCartsViewModel by viewModels()
    private  lateinit var b: FragmentListCartsBinding
    private lateinit var toolbarMenu: Menu

    lateinit var adapter : CartAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_generic_list, menu)
        toolbarMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = when(item.itemId) {
            R.id.toolbar_cart_add -> {
                val dialog = Dialog(requireActivity())
                createInputDialog(dialog, "Nuevo Carrito", "") { name ->
                    viewModel.addNewCart(name) {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initAdapter() {
        adapter = CartAdapter(viewModel.carts,
            // OnClick
            { position ->
                val action =
                    ListCartsFragmentDirections.actionListCartsFragmentToCartFragment(viewModel.carts[position])
                findNavController().navigate(action)
            },
            // OnLongClick
            { position ->
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Borrar item")
                builder.setMessage("¿Está seguro que desea eliminar el carrito?")
                builder.setPositiveButton("Sí") { _, _ ->
                    viewModel.deleteCart(position) {
                        adapter.notifyDeleteItem(position)
                        Snackbar.make(b.root, "Carrito eliminado", Snackbar.LENGTH_SHORT).show()
                    }
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


    private fun initTopBar() {

    }

}