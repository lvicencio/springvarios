package com.udemy.spring.app.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"rol", "user_id"})})
public class Role implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String rol;
	
	
	
	
	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public String getRol() {
		return rol;
	}




	public void setRol(String rol) {
		this.rol = rol;
	}




	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

}
