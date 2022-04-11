package amigosecurity.example.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    @Autowired
    private AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManagerBean) {
        this.authenticationManager = authenticationManagerBean;
    }

    public CustomAuthenticationFilter() {

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Username is : {}", username);
        log.info("Password is : {}", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
        //If you want to pass in the username and password as a json object, use Model mapper like the one in UserMicroservice Sergey kalpogorov
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); //The secret need to be encrypted
        String accessToken = JWT.create()
                .withSubject(user.getUsername()) //The subject is something unique about the user, it could be user_id, email e.t.c.
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10minutes added to the current system time
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername()) //The subject is something unique about the user, it could be user_id, email e.t.c.
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30minutes added to the current system time
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

       /* response.setHeader("accessToken", accessToken);
        response.setHeader("refreshToken", refreshToken);*/     //The result will be displayed in the header

        //OR To output the tokens and not put them in the header
        Map<String,String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        response.setContentType(APPLICATION_JSON_VALUE); // more actions --> import static
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);

    }

    /*  To get number of failed login and take action
*    @Override
*    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
*       super.unsuccessfulAuthentication(request, response, failed);}
*/

}
