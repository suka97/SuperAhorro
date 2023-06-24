package com.suka.superahorro.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.activities.LoginActivity
import com.suka.superahorro.databinding.FragmentUserBinding
import com.suka.superahorro.dbclasses.User

class UserFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()
    private  lateinit var b: FragmentUserBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.init(requireContext())
        b = FragmentUserBinding.inflate(inflater, container, false)

        return b.root
    }


    override fun onStart() {
        super.onStart()

//        b.emailTxt.text = viewModel.getUser().mail

        b.saveBt.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Guardar")
            builder.setMessage("¿Está seguro que desea guardar los cambios?")
            builder.setPositiveButton("Sí") { _, _ ->
                val email = b.emailTxt.text.toString()
                val pass = b.passTxt.text.toString()
//                viewModel.updateUser( User(email, pass) )
                Snackbar.make(b.root, "Cambios guardados", Snackbar.LENGTH_SHORT).show()
            }
        }

        b.logoutBt.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Logout")
            builder.setMessage("¿Está seguro que desea desvincular la cuenta?")
            builder.setPositiveButton("Sí") { _, _ ->
                viewModel.clearLoginInfo()

                startActivity(Intent(context, LoginActivity::class.java))
                requireActivity().finish()
            }
            builder.setNegativeButton("No") { _, _ ->

            }
            val dialog = builder.create()
            dialog.show()
        }
    }

}