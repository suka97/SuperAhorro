package com.suka.superahorro.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.suka.superahorro.R
import com.suka.superahorro.databinding.FragmentCartBinding
import com.suka.superahorro.databinding.FragmentListCartsBinding

class ListCartsFragment : Fragment() {
    private lateinit var viewModel: ListCartsViewModel
    private  lateinit var b: FragmentListCartsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentListCartsBinding.inflate(inflater, container, false)
        return b.root
    }

}