package com.suka.superahorro.fragments.User

import android.app.AlertDialog
import android.content.Intent
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.R
import com.suka.superahorro.activities.LoginActivity
import com.suka.superahorro.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()
    private  lateinit var b: FragmentUserBinding
    private lateinit var toolbarMenu: Menu


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentUserBinding.inflate(inflater, container, false)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init {
            initTexts()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_user, menu)
        toolbarMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = when(item.itemId) {
            R.id.toolbar_user_signout -> {
                onSignout()
            }
            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }


    fun initTexts() {
        b.emailTxt.text = viewModel.user_email
    }


    fun onSignout() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cerrar carrito")
            .setMessage("¿Está seguro que desea cerrar el carrito?")
            .setPositiveButton("Sí") { _, _ ->
                viewModel.clearLoginInfo()

                startActivity(Intent(context, LoginActivity::class.java))
                requireActivity().finish()
            }
            .show()
    }

}