package com.suka.superahorro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.R

class SignupFragment : Fragment() {
    lateinit var v : View
    private val viewModel: SignupViewModel by viewModels()

    lateinit var btSignup : Button
    lateinit var txtEmail : EditText
    lateinit var txtPass : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_signup, container, false)
        viewModel.init(requireContext())

        btSignup = v.findViewById(R.id.btSignup_signup)
        txtEmail = v.findViewById(R.id.txtEmail_signup)
        txtPass = v.findViewById(R.id.txtPass_signup)

        return v
    }


    override fun onStart() {
        super.onStart()

        btSignup.setOnClickListener{
            val email = txtEmail.text.toString()
            val pass = txtPass.text.toString()

            if (email.isEmpty() || pass.isEmpty()) {
                Snackbar.make(v, "Debe completar todos los campos", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if ( viewModel.signup(email, pass) ) {
                Snackbar.make(v, "Usuario creado correctamente", Snackbar.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
            else {
                Snackbar.make(v, "Ya existe un usuario con ese email", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
        }
    }

}