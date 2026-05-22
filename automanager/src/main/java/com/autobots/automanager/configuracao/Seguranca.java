package com.autobots.automanager.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.autobots.automanager.adaptadores.UserDetailsServiceImpl;
import com.autobots.automanager.filtros.Autenticador;
import com.autobots.automanager.filtros.Autorizador;
import com.autobots.automanager.jwt.ProvedorJwt;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Seguranca extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl servico;

    @Autowired
    private ProvedorJwt provedorJwt;

    private static final String ADMIN    = "ROLE_ADMIN";
    private static final String GERENTE  = "ROLE_GERENTE";
    private static final String VENDEDOR = "ROLE_VENDEDOR";
    private static final String CLIENTE  = "ROLE_CLIENTE";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeHttpRequests()
            .antMatchers(HttpMethod.POST, "/login").permitAll()
            .antMatchers("/h2-console/**").hasAnyAuthority(ADMIN)

            .antMatchers(HttpMethod.GET,    "/usuario", "/usuario/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.POST,   "/usuario", "/usuario/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/usuario", "/usuario/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.DELETE, "/usuario", "/usuario/**").hasAnyAuthority(ADMIN, GERENTE)

            .antMatchers(HttpMethod.GET,    "/cliente", "/cliente/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR, CLIENTE)
            .antMatchers(HttpMethod.POST,   "/cliente", "/cliente/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/cliente", "/cliente/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.DELETE, "/cliente", "/cliente/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)

            .antMatchers(HttpMethod.GET,    "/mercadoria", "/mercadoria/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.POST,   "/mercadoria", "/mercadoria/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.PUT,    "/mercadoria", "/mercadoria/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.DELETE, "/mercadoria", "/mercadoria/**").hasAnyAuthority(ADMIN, GERENTE)

            .antMatchers(HttpMethod.GET,    "/servico", "/servico/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.POST,   "/servico", "/servico/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.PUT,    "/servico", "/servico/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.DELETE, "/servico", "/servico/**").hasAnyAuthority(ADMIN, GERENTE)

            .antMatchers(HttpMethod.GET,    "/venda", "/venda/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR, CLIENTE)
            .antMatchers(HttpMethod.POST,   "/venda", "/venda/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/venda", "/venda/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.DELETE, "/venda", "/venda/**").hasAnyAuthority(ADMIN, GERENTE)

            .antMatchers(HttpMethod.GET,    "/empresa", "/empresa/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.POST,   "/empresa", "/empresa/**").hasAnyAuthority(ADMIN)
            .antMatchers(HttpMethod.PUT,    "/empresa", "/empresa/**").hasAnyAuthority(ADMIN)
            .antMatchers(HttpMethod.DELETE, "/empresa", "/empresa/**").hasAnyAuthority(ADMIN)

            .antMatchers(HttpMethod.GET,    "/endereco", "/endereco/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.POST,   "/endereco", "/endereco/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/endereco", "/endereco/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.DELETE, "/endereco", "/endereco/**").hasAnyAuthority(ADMIN, GERENTE)

            .antMatchers(HttpMethod.GET,    "/documento", "/documento/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.POST,   "/documento", "/documento/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/documento", "/documento/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.DELETE, "/documento", "/documento/**").hasAnyAuthority(ADMIN, GERENTE)

            .antMatchers(HttpMethod.GET,    "/telefone", "/telefone/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.POST,   "/telefone", "/telefone/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/telefone", "/telefone/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.DELETE, "/telefone", "/telefone/**").hasAnyAuthority(ADMIN, GERENTE)

            .antMatchers(HttpMethod.GET,    "/veiculo", "/veiculo/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.POST,   "/veiculo", "/veiculo/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/veiculo", "/veiculo/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.DELETE, "/veiculo", "/veiculo/**").hasAnyAuthority(ADMIN, GERENTE)

            .anyRequest().authenticated();

        http.addFilter(new Autenticador(authenticationManager(), provedorJwt));
        http.addFilter(new Autorizador(authenticationManager(), provedorJwt, servico));

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder autenticador) throws Exception {
        autenticador.userDetailsService(servico).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
        fonte.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return fonte;
    }
}