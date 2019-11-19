package com.udemy.spring.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.udemy.spring.app.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long> {

}
