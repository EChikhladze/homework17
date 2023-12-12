package com.example.homework17.login

import androidx.lifecycle.viewModelScope
import com.example.homework17.BaseDisabledButtonViewModel
import com.example.homework17.common.Resource
import com.example.homework17.common.ServiceResponse
import com.example.homework17.common.User
import com.example.homework17.network.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LogInFragmentViewModel : BaseDisabledButtonViewModel() {
    private val _responseFlow = MutableStateFlow<Resource<ServiceResponse>>(Resource.Loading(false))
    val responseFlow: SharedFlow<Resource<ServiceResponse>> = _responseFlow.asStateFlow()

    fun loginUser(user: User) {
        viewModelScope.launch {
            _responseFlow.value = Resource.Loading(true)
            try {
                val response = Network.loginService().login(user)
                if (response.isSuccessful) {
                    _responseFlow.value = Resource.Success(response.body()!!)
                } else {
                    _responseFlow.value = Resource.Error(response.message())
                }
            } catch (e: IOException) {
                _responseFlow.value = Resource.Error("Network error: ${e.localizedMessage}")
            } catch (e: HttpException) {
                _responseFlow.value = Resource.Error("HTTP error: ${e.localizedMessage}")
            } catch (e: Throwable) {
                _responseFlow.value = Resource.Error("Unexpected error: ${e.localizedMessage}")
            } finally {
                _responseFlow.value = Resource.Loading(false)
            }
        }
    }
}