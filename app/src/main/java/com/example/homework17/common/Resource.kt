package com.example.homework17.common

sealed class Resource<D> {
    data class Success<D>(val data: D) : Resource<D>()
    data class Error<D>(val errorMessage: String) : Resource<D>()
    data class Loading<D>(val loading: Boolean) : Resource<D>()
}