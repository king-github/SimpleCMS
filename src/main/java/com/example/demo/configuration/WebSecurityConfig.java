package com.example.demo.configuration;

import com.example.demo.security.RefererAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
class WebbSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RefererAuthenticationSuccessHandler refererAuthenticationSuccessHandler;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        auth.inMemoryAuthentication()
                .withUser("user1").password(passwordEncoder.encode("pass")).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder.encode("pass")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder.encode("pass")).roles("ADMIN");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable() // TODO add csrf to all forms

                .authorizeRequests()
                .antMatchers("/*").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/panel").permitAll()
                .antMatchers("/panel/**").authenticated()
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

    }

}
