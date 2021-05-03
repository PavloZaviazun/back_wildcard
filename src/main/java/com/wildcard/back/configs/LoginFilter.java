package com.wildcard.back.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildcard.back.dao.UserDAO;
import com.wildcard.back.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private UserDAO userDAO;
    public LoginFilter(String url, AuthenticationManager authenticationManager, UserDAO userDAO) {

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        User user = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Authentication authenticate = getAuthenticationManager().authenticate(
                //TODO could be getUserName();
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        return authenticate;
    }
}
