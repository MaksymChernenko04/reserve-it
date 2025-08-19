package com.maksymchernenko.reserveit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        userDetailsManager.setUsersByUsernameQuery("SELECT email AS username, password, active FROM User WHERE email = ?");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT u.email AS username, r.name AS authority FROM User u JOIN Role r ON r.id = u.role_id WHERE u.email = ?");

        return userDetailsManager;
    }

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
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout", "GET"))
                        .logoutSuccessUrl("/")
                        .permitAll()
                )

                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/user/logout", "GET"))
                );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
