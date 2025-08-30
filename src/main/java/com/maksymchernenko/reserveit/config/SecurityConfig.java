package com.maksymchernenko.reserveit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/**
 * Configuration class that provides beans for authentication and authorization.
 * <p>
 * Defines {@link UserDetailsManager}, {@link SecurityFilterChain} and {@link PasswordEncoder}.
 */
@Configuration
public class SecurityConfig {

    /**
     * Provides a {@link JdbcUserDetailsManager} configured with custom SQL queries
     * for loading user credentials and roles from the database.
     *
     * @param dataSource the application's {@link DataSource}
     * @return the configured user details manager
     */
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        userDetailsManager.setUsersByUsernameQuery("SELECT email AS username, password, active FROM User WHERE email = ?");
        userDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.email AS username, r.name AS authority FROM User u JOIN Role r ON r.id = u.role_id WHERE u.email = ?");

        return userDetailsManager;
    }

    /**
     * Configures the {@link SecurityFilterChain} with role-based access control and custom
     * login/logout behavior.
     * <p>
     * - Public access: "/", "/guest/login", "/guest/register", static resources (CSS, images). <br>
     * - Restricted: "/client/**" (CLIENT role), "/manager/**" (MANAGER role), "/admin/**" (ADMIN role). <br>
     * - Authenticated users: all other requests.
     * <p>
     * Defines a custom login page and logout URL.
     *
     * @param httpSecurity the {@link HttpSecurity} configuration builder
     * @return the built security filter chain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/", "/guest/login", "/guest/register", "/css/**", "/img/**").permitAll()
                        .requestMatchers("/client/**").hasRole("CLIENT")
                        .requestMatchers("/manager/**").hasRole("MANAGER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .formLogin(formLogin -> formLogin
                        .loginPage("/guest/login")
                        .loginProcessingUrl("/guest/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return httpSecurity.build();
    }

    /**
     * Provides a {@link PasswordEncoder} using the BCrypt hashing algorithm.
     *
     * @return a BCrypt-based password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
