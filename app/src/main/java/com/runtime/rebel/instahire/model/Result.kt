package com.runtime.rebel.instahire.model

// BoostStatus.kt
sealed class Result {
    object Success : Result()
    object Loading : Result()
    data class Error(val message: String) : Result()
}