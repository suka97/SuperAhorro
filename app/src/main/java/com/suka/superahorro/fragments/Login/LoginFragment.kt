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

        initTexts()

        viewModel.loginListener = this::onLoginListener
        viewModel.login()
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }

        b.loginBt.setOnClickListener() {
            val userMail = b.emailTxt.text()
            val userPass = b.passTxt.text()

            if ( userMail.isEmpty() || userPass.isEmpty() ) {
                Snackbar.make(b.root, "Debe completar todos los campos", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(userMail, userPass)
        }
        b.signupBt.setOnClickListener() {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(action)
        }
    }


    fun initTexts() {
        b.emailTxt.editText?.setText("")
        b.passTxt.editText?.setText("")
        b.emailTxt.error = null
        b.passTxt.error = null
    }


    fun onLoginListener(result: LoginViewModel.LoginResult) {
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

}