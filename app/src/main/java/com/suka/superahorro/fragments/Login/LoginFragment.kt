package com.suka.superahorro.fragments.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.suka.superahorro.activities.MainActivity
import com.suka.superahorro.databinding.FragmentLoginBinding
import com.suka.superahorro.packages.setLoading
import com.suka.superahorro.packages.text

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    private  lateinit var b: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentLoginBinding.inflate(inflater, container, false)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginListener = this::onLoginListener
        viewModel.login()
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }

        b.loginBt.setOnClickListener() {
            resetErrors()

            if ( checkEmailOk() && checkPassOk() )
                viewModel.login(b.emailTxt.text(), b.passTxt.text())
        }
        b.signupBt.setOnClickListener() {
            resetErrors()
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment(b.emailTxt.text())
            findNavController().navigate(action)
        }
    }


    fun checkEmailOk(): Boolean {
        if (b.emailTxt.text().isEmpty()) {
            b.emailTxt.error = "Debe ingresar un email"
            return false
        }
        return true
    }

    fun checkPassOk(): Boolean {
        if (b.passTxt.text().isEmpty()) {
            b.passTxt.error = "Debe ingresar una contraseña"
            return false
        }
        return true
    }


    fun onLoginListener(result: LoginViewModel.LoginResult) {
        resetErrors()
        when (result) {
            LoginViewModel.LoginResult.SUCCESS -> {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            LoginViewModel.LoginResult.INVALID_CREDENTIALS -> {
                b.emailTxt.error = "Credenciales inválidas"
                b.passTxt.error = "Credenciales inválidas"
            }
            LoginViewModel.LoginResult.UNDEFINED -> {
                Snackbar.make(b.root, "Login Error", Snackbar.LENGTH_SHORT).show()
            }
            LoginViewModel.LoginResult.EMAIL_NOT_VERIFIED -> {
                Snackbar.make(b.root, "Debe verificar su email", Snackbar.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }


    fun resetErrors() {
        b.emailTxt.error = null
        b.passTxt.error = null
    }

}