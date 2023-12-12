package com.example.homework17.home

import android.content.Context
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.homework17.BaseFragment
import com.example.homework17.R
import com.example.homework17.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun setUp() {
        setEmail()
    }

    private fun setEmail() {
        val email = requireArguments().getString("email")
        binding.tvUserEmail.text = email
    }

    override fun listeners() {
        logoutBtnListener()
    }

    private fun logoutBtnListener() {
        binding.btnLogOut.setOnClickListener {
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("email", null)
                putBoolean("rememberUser", false)
                apply()
            }
            findNavController().navigate(R.id.action_homeFragment_to_logInFragment)
        }
    }

    override fun bindObserves() {}
}