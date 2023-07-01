package com.suka.superahorro.fragments.ListItems

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.R
import com.suka.superahorro.adapters.CartAdapter
import com.suka.superahorro.adapters.ItemAdapter
import com.suka.superahorro.databinding.FragmentListItemsBinding
import com.suka.superahorro.fragments.ListCarts.ListCartsFragmentDirections
import com.suka.superahorro.fragments.ListCarts.ListCartsViewModel
import com.suka.superahorro.packages.createInputDialog

class ListItemsFragment : Fragment() {
    private val viewModel: ListItemsViewModel by viewModels()
    private  lateinit var b: FragmentListItemsBinding
    private lateinit var toolbarMenu: Menu

    lateinit var adapter : ItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentListItemsBinding.inflate(inflater, container, false)
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
                createInputDialog(dialog, "Nuevo Item", "") { name ->
                    viewModel.addNewItem(name) {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initAdapter() {
        adapter = ItemAdapter(viewModel.itemsList,
            // OnClick
            { position ->
                viewModel.getItem(position) { item ->
                    val action = ListItemsFragmentDirections.actionListItemsFragmentToListModelsFragment(item)
                    findNavController().navigate(action)
                }
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
        b.recItems.layoutManager = LinearLayoutManager(context)
        b.recItems.adapter = adapter

//        viewModel.onItemsChange = {
////            adapter.updateItems(viewModel.cartItems)
//            updateCartTotal()
//        }
//        viewModel.updateItems()
    }

}