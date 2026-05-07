package com.github.cc007.blueart.endpoints.auth

import com.github.cc007.blueart.util.*
import io.github.oshai.kotlinlogging.KotlinLogging
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
class AtProtoAuthenticationProvider : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        val handle = authentication.principal.toString()
        val password = authentication.credentials.toString()
        if (!handle.contains(".")) {
            throw BadCredentialsException("Username should be of the form handle")
        }
        val result = authenticate(handle, password, "https://${handle.toSocialUrl()}")
        return when(result) {
            is Success<ServerCreateSessionResponse> -> result.data.toAuthentication()
            is Failure<*> -> {
                // it is valid for the whole handle to be the domain name
                val onlyDomainResult = authenticate(handle, password, "https://$handle")
                when(onlyDomainResult) {
                    is Success<ServerCreateSessionResponse> -> onlyDomainResult.data.toAuthentication()
                    is Failure<*> -> throw BadCredentialsException(result.message)
                    is Error<*> -> throw BadCredentialsException(result.message)
                }
            }
            is Error<*> -> throw InternalAuthenticationServiceException(result.message ?: "Unknown error")
        }

    }

    override fun supports(authentication: Class<*>): Boolean =
        UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
}

private fun ServerCreateSessionResponse.toAuthentication(): AtProtoAuthentication {
    return AtProtoAuthentication(
        handle,
        accessJwt,
        refreshJwt,
        listOf(SimpleGrantedAuthority("ROLE_USER"))
    )
}

private fun authenticate(username: String, password: String, networkUrl: String): Result<ServerCreateSessionResponse> {
    val response: Response<ServerCreateSessionResponse> = try {
        BlueskyFactory
            .instance(networkUrl)
            .server()
            .createSessionBlocking(
                ServerCreateSessionRequest().also {
                    it.identifier = username
                    it.password = password
                }
            )
    } catch (e: ATProtocolException) {
        logger.warn { "Failed login attempt: ${e.status}: ${e.message} (${e.body})" }
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
