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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private UserDAO userDAO;

    public LoginFilter(String url, AuthenticationManager authenticationManager, UserDAO userDAO) {
        setFilterProcessesUrl(url);
        setAuthenticationManager(authenticationManager);
        this.userDAO = userDAO;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        User user = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Authentication authenticate = null;

        if (user != null && userDAO.findUserByUsername(user.getUsername()) != null) {
            authenticate = getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        }
        //TODO что делать если authenticate == null (в случае правильного юзера и неправильного пароля вызовется
        //unsuccessfulAuthentication, в случае неправильного юзера вернется налл и в локалсторадж сетнется андефайнд
        return authenticate;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000*60)); // 30 seconds

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
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

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        System.out.println("User not found");
    }

}
