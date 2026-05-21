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

    // Perfis
    private static final String ADMIN   = "ROLE_ADMIN";
    private static final String GERENTE = "ROLE_GERENTE";
    private static final String VENDEDOR = "ROLE_VENDEDOR";
    private static final String CLIENTE = "ROLE_CLIENTE";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http.authorizeHttpRequests()
            // ========================
            // Autenticação pública
            // ========================
            .antMatchers(HttpMethod.POST, "/login").permitAll()

            // ========================
            // Usuários
            // ========================
            // ADMIN: CRUD completo de usuários (incluindo admins)
            .antMatchers(HttpMethod.GET,    "/usuario/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.POST,   "/usuario/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/usuario/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.DELETE, "/usuario/**").hasAnyAuthority(ADMIN, GERENTE)

            // ========================
            // Clientes
            // ========================
            // CLIENTE: lê apenas seu próprio cadastro (controle feito no serviço)
            .antMatchers(HttpMethod.GET,    "/cliente/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR, CLIENTE)
            .antMatchers(HttpMethod.POST,   "/cliente/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/cliente/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.DELETE, "/cliente/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)

            // ========================
            // Mercadorias
            // ========================
            .antMatchers(HttpMethod.GET,    "/mercadoria/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.POST,   "/mercadoria/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.PUT,    "/mercadoria/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.DELETE, "/mercadoria/**").hasAnyAuthority(ADMIN, GERENTE)

            // ========================
            // Serviços
            // ========================
            .antMatchers(HttpMethod.GET,    "/servico/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.POST,   "/servico/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.PUT,    "/servico/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.DELETE, "/servico/**").hasAnyAuthority(ADMIN, GERENTE)

            // ========================
            // Vendas
            // ========================
            // VENDEDOR: cria vendas suas e lê; CLIENTE: lê vendas das quais é consumidor
            .antMatchers(HttpMethod.GET,    "/venda/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR, CLIENTE)
            .antMatchers(HttpMethod.POST,   "/venda/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/venda/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.DELETE, "/venda/**").hasAnyAuthority(ADMIN, GERENTE)

            // ========================
            // Empresa, Endereço, Documento, Telefone, Veículo
            // ========================
            .antMatchers(HttpMethod.GET,    "/empresa/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.POST,   "/empresa/**").hasAnyAuthority(ADMIN)
            .antMatchers(HttpMethod.PUT,    "/empresa/**").hasAnyAuthority(ADMIN)
            .antMatchers(HttpMethod.DELETE, "/empresa/**").hasAnyAuthority(ADMIN)

            .antMatchers(HttpMethod.GET,    "/endereco/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.POST,   "/endereco/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/endereco/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.DELETE, "/endereco/**").hasAnyAuthority(ADMIN, GERENTE)

            .antMatchers(HttpMethod.GET,    "/documento/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.POST,   "/documento/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/documento/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.DELETE, "/documento/**").hasAnyAuthority(ADMIN, GERENTE)

            .antMatchers(HttpMethod.GET,    "/telefone/**").hasAnyAuthority(ADMIN, GERENTE)
            .antMatchers(HttpMethod.POST,   "/telefone/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/telefone/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.DELETE, "/telefone/**").hasAnyAuthority(ADMIN, GERENTE)

            .antMatchers(HttpMethod.GET,    "/veiculo/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.POST,   "/veiculo/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.PUT,    "/veiculo/**").hasAnyAuthority(ADMIN, GERENTE, VENDEDOR)
            .antMatchers(HttpMethod.DELETE, "/veiculo/**").hasAnyAuthority(ADMIN, GERENTE)

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
