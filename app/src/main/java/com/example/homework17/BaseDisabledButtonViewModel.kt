package com.example.homework17

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseDisabledButtonViewModel : ViewModel() {
    private val _btnStateFlow = MutableStateFlow(false)
    val btnStateFlow: SharedFlow<Boolean> = _btnStateFlow.asStateFlow()

    private var email = ""
    private var password = ""

    fun onEmailTextChanged(newText: CharSequence?) {
        email = newText.toString()
        updateButtonState()
    }

    fun onPasswordTextChanged(newText: CharSequence?) {
        password = newText.toString()
        updateButtonState()
    }

    private fun updateButtonState() {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.isNotBlank()

        _btnStateFlow.value = isEmailValid && isPasswordValid
    }
}