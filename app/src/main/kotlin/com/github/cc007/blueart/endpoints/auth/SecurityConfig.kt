package com.github.cc007.blueart.endpoints.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler

@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(
        http: HttpSecurity,
        atprotoAuthenticationProvider: AtProtoAuthenticationProvider
    ): SecurityFilterChain {
        http
            .authenticationProvider(atprotoAuthenticationProvider)
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/login", "/").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { login ->
                login
                    .loginPage("/login")
                    .successHandler(
                        SavedRequestAwareAuthenticationSuccessHandler().apply {
                            setDefaultTargetUrl("/browse")
                        }
                    )
                    .permitAll()
            }
            .logout { }
        return http.build()
    }
}