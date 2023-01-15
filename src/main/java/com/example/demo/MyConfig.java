package com.example.demo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * @author Burkhard Graves
 */
@Configuration
public class MyConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        List<UserDetails> list = IntStream.range(1, 6).mapToObj(
                i -> User.withUsername("user" + i)
                        .password(passwordEncoder.encode("pw" + i))
                        .roles("USER")
                        .build()).collect(Collectors.toList());
        return new InMemoryUserDetailsManager(list);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .sessionManagement()
                .sessionAuthenticationStrategy(
                        new CompositeSessionAuthenticationStrategy(Arrays.asList(
                                new ChangeSessionIdAuthenticationStrategy(),
                                new FillSessionAuthenticationStrategy()
                        )))
                .maximumSessions(1); // or some other value
        
        return http.build();
    }
    
    private final static class FillSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

        @Override
        public void onAuthentication(Authentication authentication,
                HttpServletRequest request, HttpServletResponse response
        ) throws SessionAuthenticationException {

            HttpSession session = request.getSession();
            session.setAttribute(SessionRegistryMemo.class.getName(),
                    new SessionRegistryMemo(session.getId(), authentication.getPrincipal()));
        }
    }
}
