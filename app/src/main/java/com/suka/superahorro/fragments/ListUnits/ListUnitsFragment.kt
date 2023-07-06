package com.suka.superahorro.fragments.ListUnits

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
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.suka.superahorro.R
import com.suka.superahorro.adapters.ModelAdapter
import com.suka.superahorro.adapters.UnitAdapter
import com.suka.superahorro.databinding.FragmentListModelsBinding
import com.suka.superahorro.databinding.FragmentListUnitsBinding
import com.suka.superahorro.fragments.ListModels.ListModelsFragmentDirections
import com.suka.superahorro.fragments.ListModels.ListModelsViewModel
import com.suka.superahorro.packages.createInputDialog
import com.suka.superahorro.packages.setLoading

class ListUnitsFragment : Fragment() {
    private val viewModel: ListUnitsViewModel by viewModels()
    private  lateinit var b: FragmentListUnitsBinding
    private lateinit var toolbarMenu: Menu

    lateinit var adapter : UnitAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentListUnitsBinding.inflate(inflater, container, false)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            setLoading(isLoading)
        }
        viewModel.init() {
            initAdapter()
        }

        // save changes on parent fragment
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.saveUnits {
                    findNavController().navigateUp()
                }
            }
        })
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
                createInputDialog(dialog, "Nueva Unidad", "") { name ->
                    viewModel.addNewUnit(name) {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initAdapter() {
        adapter = UnitAdapter(viewModel.user.data.units)
        b.recUnits.layoutManager = LinearLayoutManager(context)
        b.recUnits.adapter = adapter
    }

}