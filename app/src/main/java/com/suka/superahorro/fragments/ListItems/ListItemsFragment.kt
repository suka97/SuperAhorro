package com.suka.superahorro.fragments.ListItems

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.suka.superahorro.R

class ListItemsFragment : Fragment() {

    companion object {
        fun newInstance() = ListItemsFragment()
    }

    private lateinit var viewModel: ListItemsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_items, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListItemsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}