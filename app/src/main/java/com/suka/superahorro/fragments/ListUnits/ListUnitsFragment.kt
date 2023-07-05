package com.suka.superahorro.fragments.ListUnits

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
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
import com.suka.superahorro.packages.setLoading

class ListUnitsFragment : Fragment() {
    private val viewModel: ListUnitsViewModel by viewModels()
    private  lateinit var b: FragmentListUnitsBinding
    private lateinit var toolbarMenu: Menu

    lateinit var adapter : UnitAdapter


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
    }


    private fun initAdapter() {
        adapter = UnitAdapter(viewModel.units)
        b.recUnits.layoutManager = LinearLayoutManager(context)
        b.recUnits.adapter = adapter
    }

}