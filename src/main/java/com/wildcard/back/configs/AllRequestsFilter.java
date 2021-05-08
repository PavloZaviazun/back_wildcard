package com.wildcard.back.configs;

import com.wildcard.back.dao.AuthTokenDAO;
import com.wildcard.back.models.AuthToken;
import com.wildcard.back.models.User;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AllRequestsFilter extends GenericFilter {
    private AuthTokenDAO authTokenDAO;

    public AllRequestsFilter(AuthTokenDAO authTokenDAO) {
        this.authTokenDAO = authTokenDAO;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = null;
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String authorizationToken = servletRequest.getHeader("Authorization");
        if(authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
            String token = authorizationToken.replaceAll("Bearer ", "");
//            String tokenData = Jwts.parser().
//                    setSigningKey("WildCard".getBytes())
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .getSubject();
            AuthToken authToken = authTokenDAO.findByToken(token);
            User user = authToken.getUser();
            if(user != null) {
                authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            }
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
