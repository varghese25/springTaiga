package com.kpl.ttm.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.kpl.ttm.Model.MyAppUserService;
import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor


public class SecurityConfig {

   @Autowired
   private final MyAppUserService appUserService; // Allowing to use its funtionality

   @Bean
   public UserDetailsService userDetailsService(){
      return appUserService;

   }

   @Bean /*Authication Provdier*/

   public AuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
      provider.setUserDetailsService(appUserService);
      provider.setPasswordEncoder(passwordEncoder());
      return provider;
   }

      @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
      return httpSecurity
            .csrf(AbstractHttpConfigurer::disable) /* Diabled not recommended in production learning Purpose */
            .formLogin(httpForm -> {
               httpForm.loginPage("/login").permitAll();
               httpForm.defaultSuccessUrl("/index",true);// Redirect after successful login

            })
            //Logout 12/11/24
            .logout(logout -> {
               logout.logoutUrl("/signout")// Default logout URL
                     .logoutSuccessUrl("/login?logout=true") // Redirect to login with logout=true param
                     .invalidateHttpSession(true)// Invalidate the session
                     .clearAuthentication(true); // Clear authentication
           })
            .authorizeHttpRequests(registry -> {
               registry.requestMatchers("/req/signup", "/login", "/index", "/css/**", "/js/**", "/img/**").permitAll(); /*Allow Static Resourses*/
               registry.anyRequest().authenticated(); /*  All other requests require authentication*/
            })

            .build(); /*Build the security filter chain */
   }
}
