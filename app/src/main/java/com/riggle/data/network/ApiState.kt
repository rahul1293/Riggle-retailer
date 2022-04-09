package com.riggle.data.network

sealed class ApiState {

    object Empty : ApiState()

    class Failure(val ex : Throwable) : ApiState()
    class  Success<T>(val data :T?, val type : String) : ApiState()

    object Loading : ApiState()


}