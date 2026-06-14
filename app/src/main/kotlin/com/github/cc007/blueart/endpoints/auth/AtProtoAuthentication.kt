package com.github.cc007.blueart.endpoints.auth

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AtProtoAuthentication(
    val socialUrl: String,
    val username: String,
    val accessToken: String,
    val refreshToken: String,
    authorities: Collection<GrantedAuthority>
) : AbstractAuthenticationToken(authorities) {

    init {
        isAuthenticated = true
    }

    override fun getCredentials(): Any? = null
    override fun getPrincipal(): Any = username
}