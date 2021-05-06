package com.wildcard.back.configs;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.wildcard.back.dao.UserDAO;
//import com.wildcard.back.models.User;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//    private UserDAO userDAO;
//    public LoginFilter(String url, AuthenticationManager authenticationManager, UserDAO userDAO) {
//
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        User user = null;
//        try {
//            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//        Authentication authenticate = getAuthenticationManager().authenticate(
//                //TODO could be getUserName();
//                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
//        return authenticate;
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        String token = Jwts.builder()
//                .setSubject(authResult.getName())
//                .signWith(SignatureAlgorithm.HS512, "WildCard".getBytes())
//                .compact();
//        System.out.println(authResult);
////        userDAO.findUserByEmail(authResult.get)
//    }
//}
