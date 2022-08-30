package pl.ms.fire.emblem.app.configuration.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pl.ms.fire.emblem.app.services.UserService

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var tokenService: TokenService

    @Bean
    fun securityFilterChain(http: HttpSecurity?): SecurityFilterChain? {
        if (http == null) return null

        http.csrf().disable()
            .cors()
            .and()
            .addFilterBefore(JwtFilter(tokenService, userService), UsernamePasswordAuthenticationFilter::class.java)
            .userDetailsService(userService)
            .authorizeRequests()
            .antMatchers("/auth", "/api/v1/user/create", "/recoverPwd", "/fireEmblemMulti", "/api/v1/test").permitAll()
            .anyRequest().authenticated()

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        return http.build()
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager? =
        config.authenticationManager

    @Bean
    @Scope(value = "singleton")
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder)
        provider.setUserDetailsService(userService)
        return provider
    }

    @Bean
    @Scope(value = "singleton")
    fun getHierarchy(): RoleHierarchy {
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy(
            "ROLE_ADMIN > ROLE_USER\nROLE_USER > ROLE_GUEST"
        )

        return roleHierarchy
    }
}