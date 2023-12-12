package com.example.homework17.register

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.homework17.BaseFragment
import com.example.homework17.R
import com.example.homework17.common.Resource
import com.example.homework17.common.User
import com.example.homework17.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    private val viewModel: RegisterFragmentViewModel by viewModels()
    private lateinit var user: User

    override fun setUp() {}

    override fun listeners() {
        registerBtnListener()
        backBtnListener()
        emailFieldListener()
        passwordFieldListener()
    }

    private fun registerBtnListener() {
        with(binding) {
            btnRegister.setOnClickListener {
                if (edPassword.text.toString() != edRepeatPassword.text.toString()) {
                    showMessage(getString(R.string.mismatched_passwords_error))
                } else {
                    user = User(edEmail.text.toString(), edPassword.text.toString())
                    viewModel.registerUser(user)
                }
            }
        }
    }

    private fun backBtnListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
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
        binding.edRepeatPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordTextChanged(text)
        }
    }

    override fun bindObserves() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.responseFlow.collect {
                    when (it) {
                        is Resource.Success -> {
                            Log.d("registering", it.data.toString())
                            returnToLogin()
                        }

                        is Resource.Error -> {
                            Log.d("registering", it.errorMessage)
                            showMessage(getString(R.string.general_error))
                        }

                        is Resource.Loading -> {
                            showProgressBar(it.loading)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.btnStateFlow.collect {
                    binding.btnRegister.isEnabled = it
                }
            }
        }
    }

    private fun returnToLogin() {
        val bundle = bundleOf("email" to user.email, "password" to user.password)
        setFragmentResult("requestKey", bundle)
        findNavController().popBackStack()
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