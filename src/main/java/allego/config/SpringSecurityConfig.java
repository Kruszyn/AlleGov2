package allego.config;

import allego.services.implementations.UserSecurityService;
import allego.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private Environment environment;

    @Autowired
    private UserSecurityService userSecurityService;

    private BCryptPasswordEncoder passwordEncoder(){
        return SecurityUtility.passwordEncoder();
    }

    private static final String[] PUBLIC_PATHS = {
            "/css/**",
            "/js/**",
            "/img/**",
            "/fonts/**",
            "/img/product/**"
    };

    private static final String[] AVAILABLE_PATHS = {
            "/",
            "index",
            "/register",
            "/confirm",
            "/products",
            "/product",
            "/forgetPassword"
    };


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
            authorizeRequests()
                .antMatchers(PUBLIC_PATHS).permitAll()
                .antMatchers(AVAILABLE_PATHS).permitAll()
                .antMatchers("/h2/**").permitAll() //TODO TYLKO DO TESTÓW
                .antMatchers("/user/**").hasAnyRole("USER")
                .antMatchers("/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
            .and()
                .formLogin().failureUrl("/login?error").defaultSuccessUrl("/")
                .loginPage("/login").permitAll()
            .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
            .and()
                .rememberMe();
        // add this line to use H2 web console, USUNĄc w WERSJI KONCOWEJ TODO
       http.csrf().disable().cors().disable();
       http.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
    }

}
