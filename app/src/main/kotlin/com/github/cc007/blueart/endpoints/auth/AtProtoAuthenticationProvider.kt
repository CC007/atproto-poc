package com.github.cc007.blueart.endpoints.auth

import com.github.cc007.blueart.util.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.env.Environment
import org.springframework.core.env.getProperty
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import work.socialhub.kbsky.ATProtocolException
import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionRequest
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionResponse
import work.socialhub.kbsky.api.entity.share.Response

private val logger = KotlinLogging.logger {}

@Component
class AtProtoAuthenticationProvider(
    private val environment: Environment,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        val handle = authentication.principal.toString()
        val password = authentication.credentials.toString()
        if (!handle.contains(".")) {
            throw BadCredentialsException("Username should be of the form handle")
        }
        val authenticationUrl = handle.toAuthenticationUrl(resolveLocalhostPort())
        return when (val result = authenticate(handle, password, authenticationUrl)) {
            is Success<ServerCreateSessionResponse> -> result.data.toAuthentication(authenticationUrl)
            is Failure<*> -> {
                // it is valid for the whole handle to be the domain name
                when (val onlyDomainResult = authenticate(handle, password, "https://$handle")) {
                    is Success<ServerCreateSessionResponse> -> onlyDomainResult.data.toAuthentication("https://$handle")
                    is Failure<*> -> throw BadCredentialsException(result.message)
                    is Error<*> -> throw BadCredentialsException(result.message)
                }
            }

            is Error<*> -> throw InternalAuthenticationServiceException(result.message ?: "Unknown error")
        }

    }

    override fun supports(authentication: Class<*>): Boolean =
        UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)

    private fun resolveLocalhostPort(): Int {
        return environment.getProperty<Int>("local.server.port")
            ?.takeIf { it > 0 }
            ?: environment.getProperty<Int>("server.port")
            ?.takeIf { it > 0 }
            ?: 8080
    }
}

private fun ServerCreateSessionResponse.toAuthentication(uri: String): AtProtoAuthentication {
    return AtProtoAuthentication(
        uri,
        handle,
        accessJwt,
        refreshJwt,
        listOf(SimpleGrantedAuthority("ROLE_USER"))
    )
}

private fun authenticate(username: String, password: String, authenticationUrl: String): Result<ServerCreateSessionResponse> {
    val response: Response<ServerCreateSessionResponse> = try {
        BlueskyFactory
            .instance(authenticationUrl)
            .server()
            .createSessionBlocking(
                ServerCreateSessionRequest().also {
                    it.identifier = username
                    it.password = password
                }
            )
    } catch (e: ATProtocolException) {
        logger.warn(e) { "Failed login attempt: ${e.status}: ${e.message} (${e.body})" }
        return when {
            e.status == 401 -> e.message.toFailure()
            e.message == "Input must have the property \"password\"" -> e.message.toFailure()
            e.message == "Input must have the property \"identifier\"" -> e.message.toFailure()
            else -> "Something went wrong. Try again later.".toError()
        }
    }
    logger.info { response.json }
    return response.data.toSuccess()
}
