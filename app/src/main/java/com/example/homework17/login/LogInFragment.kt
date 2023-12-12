package com.example.homework17.login

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.homework17.BaseFragment
import com.example.homework17.R
import com.example.homework17.common.Resource
import com.example.homework17.common.User
import com.example.homework17.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LogInFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel: LogInFragmentViewModel by viewModels()
    private lateinit var user: User

    override fun setUp() {}

    override fun listeners() {
        fragmentResultListener()
        registerBtnListener()
        emailFieldListener()
        passwordFieldListener()
        loginBtnListener()
    }

    private fun checkRememberedUser() {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        if (binding.checkboxRememberMe.isChecked) {
            with(sharedPref.edit()) {
                putString("email", user.email)
                putBoolean("rememberUser", true)
                apply()
            }
        }
    }

    private fun registerBtnListener() {
        binding.btnRegister.setOnClickListener {
            findNavController()
                .navigate(R.id.action_logInFragment_to_registerFragment)
        }
    }

    private fun emailFieldListener() {
        binding.edEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.onEmailTextChanged(text)
        }
    }

    private fun passwordFieldListener() {
        binding.edPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordTextChanged(text)
        }
    }

    private fun loginBtnListener() {
        with(binding) {
            btnLogIn.setOnClickListener {
                user = User(edEmail.text.toString(), edPassword.text.toString())
                viewModel.loginUser(user)
            }
        }
    }

    private fun fragmentResultListener() {
        setFragmentResultListener("requestKey") { _, bundle ->
            val email = bundle.getString("email")
            val password = bundle.getString("password")
            with(binding) {
                edEmail.setText(email)
                edPassword.setText(password)
            }
        }
    }

    override fun bindObserves() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.responseFlow.collect {
                    when (it) {
                        is Resource.Success -> {
                            Log.d("login", it.data.toString())
                            goToHome()
                        }

                        is Resource.Error -> {
                            Log.d("login", "error: ${it.errorMessage}")
                            showMessage(getString(R.string.general_error))
                        }

                        is Resource.Loading -> {
                            Log.d("login", it.loading.toString())
                            showProgressBar(it.loading)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.btnStateFlow.collect {
                    binding.btnLogIn.isEnabled = it
                }
            }
        }
    }

    private fun goToHome() {
        checkRememberedUser()
        val action =
            LogInFragmentDirections.actionLogInFragmentToHomeFragment(user.email)
        findNavController().navigate(action)
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar(loading: Boolean) {
        binding.progressBar.visibility = if (loading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}