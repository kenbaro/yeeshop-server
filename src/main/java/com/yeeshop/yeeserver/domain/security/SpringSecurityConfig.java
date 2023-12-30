package com.yeeshop.yeeserver.domain.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.yeeshop.yeeserver.domain.constant.YeeCommonConst;
import com.yeeshop.yeeserver.domain.security.oauth2.CustomAuthenticationFailureHandler;
import com.yeeshop.yeeserver.domain.security.oauth2.CustomAuthenticationSuccessHandler;
import com.yeeshop.yeeserver.domain.security.oauth2.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;
import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter ;
	private final CustomOAuth2UserService customOauth2UserService;
	private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception { 
    	http.csrf(crsf -> crsf.disable())
    		.cors(cors -> withDefaults())
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/user/**").hasAuthority(YeeCommonConst.YeeRole.USER_ROLE)
            		.requestMatchers("/customer/**").permitAll()
                    .requestMatchers("/admin/**").hasAuthority(YeeCommonConst.YeeRole.ADMIN_ROLE)
                    .requestMatchers("/public/**", "/auth/**", "/oauth2/**").permitAll()
                    .anyRequest().authenticated())
            .oauth2Login(oauth2Login -> oauth2Login
            		.userInfoEndpoint(userInfoEndpointConfigurer -> userInfoEndpointConfigurer.userService(customOauth2UserService))
            		.successHandler(customAuthenticationSuccessHandler)
            		.failureHandler(authenticationFailureHandler()))
            .logout(l -> l.logoutSuccessUrl("/customer/logout").permitAll());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
    		throws Exception { 

    	return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { 
    	
    	return new BCryptPasswordEncoder(); 
    }
    
    @Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
    
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}