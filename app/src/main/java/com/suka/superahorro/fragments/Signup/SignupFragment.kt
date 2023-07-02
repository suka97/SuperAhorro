package com.suka.superahorro.fragments.Signup

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
import com.suka.superahorro.databinding.FragmentLoginBinding
import com.suka.superahorro.databinding.FragmentSignupBinding
import com.suka.superahorro.packages.setLoading
import com.suka.superahorro.packages.text

class SignupFragment : Fragment() {
    private val viewModel: SignupViewModel by viewModels()
    private  lateinit var b: FragmentSignupBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentSignupBinding.inflate(inflater, container, false)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }

        b.signupBt.setOnClickListener{
            resetErrors()
            if (!checkEmailOk()) return@setOnClickListener
            if (!checkPassOk()) return@setOnClickListener

            viewModel.signup(b.emailTxt.text(), b.passTxt.text()) { result ->
                if (result == SignupViewModel.SignupResult.SUCCESS) {
                    Snackbar.make(b.root, "Usuario creado correctamente", Snackbar.LENGTH_LONG)
                        .show()
                    findNavController().navigateUp()
                }
                else onSignupError(result)
            }
        }
    }


    fun onSignupError(error: SignupViewModel.SignupResult) {
        when(error) {
            SignupViewModel.SignupResult.MAIL_EXISTS -> {
                b.emailTxt.error = "El email ya está en uso"
            }
            SignupViewModel.SignupResult.PASS_INVALID -> {
                b.passTxt.error = "La contraseña debe tener al menos 6 caracteres"
            }
            SignupViewModel.SignupResult.MAIL_INVALID -> {
                b.emailTxt.error = "El email no es válido"
            }
            SignupViewModel.SignupResult.UNDEFINED -> {
                Snackbar.make(b.root, "Error desconocido", Snackbar.LENGTH_LONG).show()
            }
            else -> {}
        }
    }


    fun resetErrors() {
        b.emailTxt.error = null
        b.passTxt.error = null
        b.passRetypeTxt.error = null
    }


    fun checkEmailOk(): Boolean {
        if ( b.emailTxt.text().isEmpty() ) {
            b.emailTxt.error = "Debe completar todos los campos"
            return false
        }
        return true
    }


    fun checkPassOk(): Boolean {
        if ( b.passTxt.text().isEmpty() ) {
            b.passTxt.error = "Debe completar todos los campos"
            return false
        }
        if ( b.passRetypeTxt.text().isEmpty() ) {
            b.passRetypeTxt.error = "Debe completar todos los campos"
            return false
        }
        if ( b.passTxt.text() != b.passRetypeTxt.text() ) {
            b.passRetypeTxt.error = "Las contraseñas no coinciden"
            return false
        }
        return true
    }

}