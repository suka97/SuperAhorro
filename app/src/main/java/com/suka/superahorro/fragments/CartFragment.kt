package com.suka.superahorro.fragments

import android.app.ActionBar
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.R
import com.suka.superahorro.adapters.CartItemAdapter
import com.suka.superahorro.databinding.FragmentCartBinding
import com.suka.superahorro.dbclasses.CartItem
import com.suka.superahorro.packages.hideKeyboard
import com.suka.superahorro.packages.showKeyboard


class CartFragment : Fragment() {
    private val viewModel: CartViewModel by viewModels()
    private  lateinit var b: FragmentCartBinding

    lateinit var adapter : CartItemAdapter
    var isAdding: MutableLiveData<Boolean> = MutableLiveData(false)


    // init top bar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.main_toolbar)
        toolbar?.title = "Título del Fragmento"
//        toolbar.setNavigationOnClickListener {
//            // Acción al hacer clic en el botón de navegación del Toolbar
//        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentCartBinding.inflate(inflater, container, false)
        initHiddens()
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

        setHasOptionsMenu(true)

        return b.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_cart, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = when(item.itemId) {
//            R.id.action_add -> Snackbar.make(v, "add", Snackbar.LENGTH_SHORT).show()
//            R.id.action_fav -> Snackbar.make(v, "fav", Snackbar.LENGTH_SHORT).show()
            else -> ""
        }
        return super.onOptionsItemSelected(item)
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
        isAdding.observe(viewLifecycleOwner) { isAdding ->
            b.newItemTxt.visibility = if (isAdding) View.VISIBLE else View.GONE
            b.cartTotalTxt.visibility = if (isAdding) View.GONE else View.VISIBLE
            if ( isAdding == true ) {
                b.newItemTxt.setText("")
                showKeyboard(b.newItemTxt)
            }
            else {
                b.newItemBtOk.visibility = View.GONE
                hideKeyboard()
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
            val itemName = b.newItemTxt.text.toString()
            viewModel.addNewItem(itemName) {
                goToItemDetailNew(itemName)
            }
        }
        b.newItemTxt.setOnItemClickListener { parent, view, position, id ->
            val selectedOption = parent.getItemAtPosition(position).toString()
            goToItemDetailNew(selectedOption)
        }
    }

    private fun initHiddens() {
        b.newItemTxt.visibility = View.GONE
        b.newItemBtOk.visibility = View.GONE
        b.cartTotalTxt.visibility = View.GONE
        b.cartTotalTxt.visibility = View.GONE
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
        val items = viewModel.user.getItemsAutocomplete()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, items)
        b.newItemTxt.setAdapter(adapter)
    }

}