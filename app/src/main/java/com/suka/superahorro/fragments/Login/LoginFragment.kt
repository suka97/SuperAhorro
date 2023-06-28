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

class LoginFragment : Fragment(), LoginViewModel.LoginListener {
    private val viewModel: LoginViewModel by viewModels()
    private  lateinit var b: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.init(requireContext())
        b = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel.loginListener = this
        viewModel.isLoading.observe(viewLifecycleOwner) { newValue ->
            Log.d("loading", "loading: $newValue")
        }

        return b.root
    }


    override fun onLoginSuccess() {
        startActivity(Intent(context, MainActivity::class.java))
        requireActivity().finish()
    }

    override fun onLoginError() {
        Snackbar.make(b.root, "Login error", Snackbar.LENGTH_SHORT).show()
    }


    override fun onStart() {
        super.onStart()
        viewModel.login()

        b.loginBt.setOnClickListener() {
            val userMail = b.emailTxt.text.toString()
            val userPass = b.passTxt.text.toString()

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

}