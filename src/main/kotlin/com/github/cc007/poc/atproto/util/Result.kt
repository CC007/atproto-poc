package com.github.cc007.poc.atproto.util

sealed interface Result<T>
data class Success<T>(val data: T) : Result<T>

fun <T> T.toSuccess(): Success<T> = Success(this)
data class Failure<T>(val message: String?) : Result<T>

fun <T> String?.toFailure(): Failure<T> = Failure(this)
data class Error<T>(val message: String?) : Result<T>

fun <T> String?.toError(): Error<T> = Error(this)