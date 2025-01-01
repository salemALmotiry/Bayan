package com.example.bayan.Config;


import com.example.bayan.Service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class ConfigurationSecurity {

    private final MyUserDetailsService myUserDeletesService;

    public ConfigurationSecurity(MyUserDetailsService myUserDeletesService) {
        this.myUserDeletesService = myUserDeletesService;
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDeletesService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;

    }



        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.csrf().disable() // Disable CSRF for testing purposes
                    .authorizeHttpRequests()
                    .anyRequest().permitAll() // Allow all requests
                    .and()
                    .logout().disable() // Disable logout endpoint
                    .httpBasic().disable(); // Disable HTTP Basic Authentication

            return http.build();
        }



}