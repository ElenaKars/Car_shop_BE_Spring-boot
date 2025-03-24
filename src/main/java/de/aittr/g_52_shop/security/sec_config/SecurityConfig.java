package de.aittr.g_52_shop.security.sec_config;

import de.aittr.g_52_shop.security.sec_filter.TokenFilter;
import org.apache.commons.collections4.Get;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String ADMIN_ROLE = "ADMIN";
    private final String USER_ROLE = "USER";


    private final TokenFilter filter;

    public SecurityConfig(@Lazy TokenFilter filter) {
        this.filter = filter;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(x -> x
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //basic auth
//                .httpBasic(Customizer.withDefaults())
                //token auth
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(x -> x
                                .requestMatchers(HttpMethod.GET, "/products/all").permitAll()
                                .requestMatchers(HttpMethod.GET, "/products/").hasAnyRole(ADMIN_ROLE, USER_ROLE)
                                .requestMatchers(HttpMethod.POST, "/products").hasRole(ADMIN_ROLE)
                                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/refresh").permitAll()
                                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                                .requestMatchers(HttpMethod.GET, "/register/{code}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/hello").permitAll()
                                .requestMatchers(HttpMethod.POST, "/files").hasRole(ADMIN_ROLE)
                                .requestMatchers(HttpMethod.POST, "/customers").hasRole(ADMIN_ROLE)
                                .requestMatchers(HttpMethod.PUT, "/customers/**").hasAnyRole(ADMIN_ROLE, USER_ROLE)
                        .anyRequest().authenticated()

                )
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
