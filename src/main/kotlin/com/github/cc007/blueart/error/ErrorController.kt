package com.github.cc007.blueart.error

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class ErrorController {

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<String> {
        if (e.statusCode == HttpStatus.UNAUTHORIZED) {
            val headers = HttpHeaders()
            headers["Location"] = "/login?error=${e.reason}"
            return ResponseEntity(null, headers, HttpStatus.FOUND)
        }
        return ResponseEntity(e.reason ?: "Unknown error", e.statusCode)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<String> {
        e.printStackTrace()
        return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
