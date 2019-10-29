package com.udemy.spring.app.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udemy.spring.app.dao.IUsuarioDao;
import com.udemy.spring.app.models.entity.Role;
import com.udemy.spring.app.models.entity.Usuario;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService{

	@Autowired
	private IUsuarioDao usuarioDao;
	
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class); 
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioDao.findByUsername(username);
		
		if (usuario == null) {
			logger.error("Error de Autentificacion: Usuario no Existe!! '" + username +  "'");
			throw new UsernameNotFoundException("Username " + username + " No existe en la Base de Datos del Sistema");
		}
		
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Role role : usuario.getRoles()) {
			logger.info("Rol : ".concat(role.getRol()));
			authorities.add(new SimpleGrantedAuthority(role.getRol()));
		}
		
		if (authorities.isEmpty()) {
			logger.error("Error de Autentificacion: EL Usuario'" + username +  " no tiene roles asignados'");
			throw new UsernameNotFoundException("Username " + username + " sin roles");
		}
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled(), true, true, true, authorities);
	}

	
}
