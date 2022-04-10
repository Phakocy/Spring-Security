package amigosecurity.example.jwt.security;

import amigosecurity.example.jwt.filter.CustomAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login"); // change to api/login instead of using ordinary login
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // http.authorizeRequests().anyRequest().permitAll(); EVERYBODY HAS ACCESS INSTEAD DO
           http.authorizeRequests().antMatchers("/api/login/**").permitAll(); //pass in only endpoints that you don't want to secure like "/login" spring have catered for it in UsernamePasswordAuthenticationFilter
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/user/save/**").hasAnyAuthority("ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/users/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().anyRequest().authenticated();
       // http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.addFilter(customAuthenticationFilter);//We now pass in customAuthenticationFilter instead of new CustomAuthenticationFilter(authenticationManagerBean())
        http.addFilterBefore(new CustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
