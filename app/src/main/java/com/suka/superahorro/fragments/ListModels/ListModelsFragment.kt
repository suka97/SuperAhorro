package com.suka.superahorro.fragments.ListModels

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.suka.superahorro.R

class ListModelsFragment : Fragment() {

    companion object {
        fun newInstance() = ListModelsFragment()
    }

    private lateinit var viewModel: ListModelsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_models, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListModelsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}