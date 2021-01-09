package com.example.proyectoacolitame.security;


import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Bean;


import static com.example.proyectoacolitame.security.SecurityConstants.URL;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
//    private UserDetailsServiceImpl userDetailsService;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    public WebSecurity(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.userDetailsService = userDetailsService;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET,"/categoria/**").permitAll()
                .antMatchers(HttpMethod.GET,"/empresa/**").permitAll()
                .antMatchers(HttpMethod.GET,"/producto/**").permitAll()
                .antMatchers(HttpMethod.GET,"/calificacion/**").permitAll()
                .antMatchers(HttpMethod.GET,"/comentarios/**").permitAll()
                .antMatchers(HttpMethod.GET,"/respuesta/**").permitAll()
//                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
//    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }
}