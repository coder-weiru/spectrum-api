package li.spectrum.api;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Profile({"local"})
@Configuration
@EnableOAuth2Sso
public class WebSecurityConfigLocal extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {        
         http.authorizeRequests()
             .antMatchers("/", "/login").permitAll()
             .mvcMatchers(HttpMethod.GET,"/**").access("hasRole('ROLE_ANONYMOUS')")
             .mvcMatchers(HttpMethod.POST,"/**").access("hasRole('ROLE_ANONYMOUS')")
             .mvcMatchers(HttpMethod.PUT,"/**").access("hasRole('ROLE_ANONYMOUS')")
             .mvcMatchers(HttpMethod.DELETE,"/**").access("hasRole('ROLE_ANONYMOUS')")
             .anyRequest().authenticated()
             .and()
				.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
   }
   

}