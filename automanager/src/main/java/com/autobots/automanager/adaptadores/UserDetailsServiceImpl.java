package com.autobots.automanager.adaptadores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio repositorio;

    private Usuario obterPorNomeUsuario(String nomeUsuario) {
        List<Usuario> usuarios = repositorio.findAll();
        for (Usuario usuario : usuarios) {
            for (Credencial credencial : usuario.getCredenciais()) {
                if (credencial instanceof CredencialUsuarioSenha) {
                    CredencialUsuarioSenha cus = (CredencialUsuarioSenha) credencial;
                    if (cus.getNomeUsuario().equals(nomeUsuario)) {
                        return usuario;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = obterPorNomeUsuario(username);
        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(usuario);
    }
}
