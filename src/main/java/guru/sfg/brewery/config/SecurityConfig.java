package guru.sfg.brewery.config;

import guru.sfg.brewery.security.BrewUserDetailsService;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true) // colocado quando usamos uma anotação para validar o acesso ao servico direto no Controller
// teste feito no controler costumer
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class).csrf().disable();

        http.addFilterBefore(restUrlAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests( auth -> {
            auth.antMatchers("/","/webjars/**", "/login","/resources/**","/beers/**").permitAll()
            .antMatchers("/beers/**").permitAll()
            .antMatchers(HttpMethod.GET,"/api/v1/beer/**").permitAll()
            .mvcMatchers(HttpMethod.GET,"/api/v1/beerUpc/**").hasAnyRole("ADMIN","CUSTOMER","USER")
            .mvcMatchers(HttpMethod.DELETE,"/api/v1/beer/**").hasRole("ADMIN")
            .mvcMatchers(HttpMethod.GET,"/brewery/api/v1/breweries/**").hasAnyRole("ADMIN","CUSTOMER")
            .antMatchers("/brewery/**").hasAnyRole("ADMIN","CUSTOMER")
            .antMatchers("/h2-console/**").permitAll();
        })
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();

        //para abrir o h2console , nao usar em producao;
        http.headers().frameOptions().sameOrigin();
    }

    //    @Bean
    protected UserDetailsService userDetailsService(){
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("crinnger")
                .password("oliveira")
                .roles("ADMIN")
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("samuel")
                .password("oliveira")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin,user);
    }

    //  uma das formar de fazer para confivurar o uso da autenticacao pelo banco,
    //@Autowired
    //BrewUserDetailsService brewUserDetailsService;

    //@Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //  uma das formar de fazer para confivurar o uso da autenticacao pelo banco,
        // auth.userDetailsService(this.brewUserDetailsService).passwordEncoder(passwordEncoder());
        /** auth.inMemoryAuthentication()
                .withUser("crinnger").password("{noop}oliveira").roles("ADMIN")
                .and()
                .withUser("samuel").password("{noop}oliveira").roles("USER")
                .and()
                .withUser("kleyver").password("{noop}oliveira").roles("CUSTOMER");
         */
        // na frete da senha e informado que tipo de algoritmo foi feito para encoder.
        /**auth.inMemoryAuthentication()
                .withUser("crinnger").password("{bcrypt}$2a$10$sN8YQ2lptpyyyPctH/azF.rjf6VBbB3OAEAyIQYI50gNZ8D35C3Iy").roles("ADMIN")
                .and()
                .withUser("samuel").password("{sha256}14d69bec076cfcc9f6a9b232dd9a534b6259432a90739db3609084f50aab230b3b9d96bf1f8860e3").roles("USER")
                .and()
                .withUser("kleyver").password("{bcrypt15}$2a$10$JR/RtM4yX8hR56YFNV.dYuffKR.k/kMZPR03gZFK25CWrOnmiaQoS").roles("CUSTOMER");
         */
    //}

    // Definir enconder para o password
    @Bean
    PasswordEncoder passwordEncoder(){
        //return NoOpPasswordEncoder.getInstance();
        //return  new LdapShaPasswordEncoder();
        //return  new BCryptPasswordEncoder();

        //implementacao padrao para Encoder
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();

        //Implementacao customizada do encoder, foi criado um nivel para o bcrypt
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager){
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public RestUrlAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager){
        RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

}
