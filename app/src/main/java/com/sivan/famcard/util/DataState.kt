package com.sivan.famcard.util

sealed class DataState<out R> {

    data class Success<out T>(val data: T) : DataState<T>()

    data class Error(val exception: String) : DataState<Nothing>() {
        val message = exception
    }

    object Loading : DataState<Nothing>()
}