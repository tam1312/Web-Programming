package mk.ukim.finki.wp.exam.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // TODO: If you are implementing the security requirements, remove this following line
        //web.ignoring().antMatchers("/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/products/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().permitAll()
                .failureUrl("/login?error=BadCredentials")
                .defaultSuccessUrl("/products", true)
                .and()
                .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling().accessDeniedPage("/");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
