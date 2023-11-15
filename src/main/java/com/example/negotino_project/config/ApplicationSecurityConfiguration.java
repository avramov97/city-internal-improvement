package com.example.negotino_project.config;

import com.example.negotino_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter
{


    private final UserService userService;

    @Autowired
    public ApplicationSecurityConfiguration(UserService userService)
    {
        this.userService = userService;
    }

    private CsrfTokenRepository csrfTokenRepository()
    {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();

        repository.setSessionAttributeName("_csrf");

        return repository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http

                .cors().disable()
                .csrf() //.disable()
                    .csrfTokenRepository(csrfTokenRepository())
                    .and()
                    .authorizeRequests()
                    .antMatchers("/**", "/search", "/news", "/events", "/good-things", "/donations", "/talents", "/login", "/register").permitAll()
                    .antMatchers("/talents/add", "/talents/approve-all", "/talents/discard-all", "/talents/approve", "/talents/discard", "/donations/add", "/good-things/add", "/events/add", "/organization/change-role").permitAll()
                    .antMatchers("/news/single-post/**", "/news/search", "/events/single-event/**", "/good-things/single-good-thing/**", "/donations/single-donation/**", "/talents/single-talent/**").permitAll()
                    .antMatchers("/upload", "/css/**", "/js/**", "/Photos/**", "/img/**", "/logs/all").permitAll()
                    .antMatchers("/admin/**").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/",true)
                .and()
                    .rememberMe()
                    .rememberMeParameter("rememberMe")
                    .key("PLYOK")
                    .userDetailsService(this.userService)
                    .rememberMeCookieName("KLYOK")
                    .tokenValiditySeconds(1200)
                .and()
                    .exceptionHandling()
                    .accessDeniedPage("/unauthorized")
                .and()
                    .logout()
                    .logoutSuccessUrl("/") // .permitAll()???
                .and()
                    .oauth2Login() // enable OAuth2
                    .loginPage("/login")
                    .defaultSuccessUrl("/");



        //super.configure(http);
    }


}
