package com.suka.superahorro.fragments.UnitDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.suka.superahorro.R

class UnitDetailFragment : Fragment() {

    companion object {
        fun newInstance() = UnitDetailFragment()
    }

    private lateinit var viewModel: UnitDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_unit_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UnitDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}