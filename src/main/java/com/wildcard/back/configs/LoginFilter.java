package com.wildcard.back.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildcard.back.dao.UserDAO;
import com.wildcard.back.models.AuthToken;
import com.wildcard.back.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private UserDAO userDAO;
    public LoginFilter(String url, AuthenticationManager authenticationManager, UserDAO userDAO) {
        System.out.println("1");
        setFilterProcessesUrl(url);
        setAuthenticationManager(authenticationManager);
        this.userDAO = userDAO;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("2");
        User user = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            System.out.println(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Authentication authenticate = getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        System.out.println(authenticate);
        return authenticate;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("3");
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .signWith(SignatureAlgorithm.HS512, "WildCard".getBytes())
                .compact();
        User user = userDAO.findUserByUsername(authResult.getName());
        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        authToken.setUser(user);
        user.getAuthTokens().add(authToken);
//        userDAO.save(user);

        response.addHeader("Authorization", "Bearer " + token);
        chain.doFilter(request, response);
    }
}
