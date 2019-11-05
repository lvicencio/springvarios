package com.udemy.spring.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udemy.spring.app.models.entity.Cliente;
import com.udemy.spring.app.models.entity.Factura;
import com.udemy.spring.app.models.entity.Producto;

public interface IClienteService {

	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable); //paginacion
	
	public void save(Cliente cliente);
	
	public Cliente findOne(long id);
	
	public void delete(long id);
	
	public List<Producto> findByNombre(String term);
	
	public void saveFactura(Factura factura);
	
	public Producto findProductoById(Long id);
	
	public Factura findFacturaById(Long id);
	
	public void deleteFactura(long id);
	
	
}
