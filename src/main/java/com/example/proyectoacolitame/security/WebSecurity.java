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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


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
                .antMatchers(HttpMethod.GET,"/api/categoria/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/empresa/admin/").authenticated()
                .antMatchers(HttpMethod.GET,"/api/empresa/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/producto/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/calificacion/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/comentarios/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/respuesta/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/empresa/insertar").permitAll()
                .antMatchers(HttpMethod.GET,"/api/static/verificacion/logoU.svg").permitAll()
                .antMatchers(HttpMethod.GET,"/verificacion/**").permitAll()
                .anyRequest().authenticated()//esto estaba comentado
                .and()
                .cors().and()
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

   // @Override
  //  public void configure(AuthenticationManagerBuilder auth) throws Exception {
  //      auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
//    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
   }

}