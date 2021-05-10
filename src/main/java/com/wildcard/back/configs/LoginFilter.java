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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private UserDAO userDAO;

    public LoginFilter(String url, AuthenticationManager authenticationManager, UserDAO userDAO) {
        setFilterProcessesUrl(url);
        setAuthenticationManager(authenticationManager);
        this.userDAO = userDAO;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        User user = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Authentication authenticate = getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        return authenticate;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        Date date = Date.from(LocalDateTime.now().plus(1, ChronoUnit.MINUTES).toInstant(ZoneOffset.ofHours(1)));
        String token = Jwts.builder()
                .setSubject(authResult.getName())
//                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, "WildCard".getBytes())
                .compact();
        User user = userDAO.findUserByUsername(authResult.getName());
        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        authToken.setUser(user);
        user.getAuthTokens().add(authToken);
        userDAO.save(user);
        response.addHeader("Authorization", "Bearer " + token);
        chain.doFilter(request, response);
    }
}
