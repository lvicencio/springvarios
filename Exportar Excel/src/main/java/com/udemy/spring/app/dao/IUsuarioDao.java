package com.udemy.spring.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.udemy.spring.app.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{

	public Usuario findByUsername(String username);
}
