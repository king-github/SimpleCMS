package com.example.demo.configuration;

import com.example.demo.security.RefererAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebbSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RefererAuthenticationSuccessHandler refererAuthenticationSuccessHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable() // TODO add csrf to all forms

                .authorizeRequests()

                .antMatchers("/h2/**").permitAll()   // for h2 console

                .antMatchers("/register/*").permitAll()
                .antMatchers("/public/**/*").permitAll()
                .antMatchers("/panel").permitAll()
                .antMatchers("/panel/**/*").authenticated()
                .antMatchers("/").permitAll()
                .antMatchers("/**/*").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(refererAuthenticationSuccessHandler)
                // .defaultSuccessUrl("/panel")
                .failureUrl("/login?error=true")
                .permitAll()

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .logoutSuccessUrl("/login?logout=true")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll();


        http.headers().frameOptions().disable();  // for h2 console

    }

    @Bean("passwordEncoder")
    public static PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
