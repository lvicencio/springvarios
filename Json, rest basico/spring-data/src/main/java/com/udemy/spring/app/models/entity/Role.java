package com.udemy.spring.app.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "rol" })})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Role implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String rol;
	
	
	
	
	public String getRol() {
		return rol;
	}




	public void setRol_name(String rol) {
		this.rol = rol;
	}




	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

}
