package com.wildcard.back.configs;

//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private UserDetailsService userDetailsService;
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(new BCryptPasswordEncoder());
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }
//}
