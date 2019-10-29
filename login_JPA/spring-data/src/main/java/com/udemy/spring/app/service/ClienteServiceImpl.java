package com.udemy.spring.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udemy.spring.app.dao.IClienteDao;
import com.udemy.spring.app.dao.IFacturaDao;
import com.udemy.spring.app.dao.IProductoDao;
import com.udemy.spring.app.models.entity.Cliente;
import com.udemy.spring.app.models.entity.Factura;
import com.udemy.spring.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired
	private IProductoDao productoDao;
	
	@Autowired
	private IFacturaDao facturaDao;
	
	@Transactional(readOnly = true)
	@Override
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return (List<Cliente>) clienteDao.findAll();
	}

	
	@Override
	@Transactional()
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(long id) {
		// TODO Auto-generated method stub
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional()
	public void delete(long id) {
		clienteDao.deleteById(id);
		
	}

	//paginacion
	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return clienteDao.findAll(pageable);
	}


	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
		// TODO Auto-generated method stub
		return productoDao.findByNombre(term);
	}


	@Override
	@Transactional()
	public void saveFactura(Factura factura) {
		// TODO Auto-generated method stub
		facturaDao.save(factura);
		
	}


	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {
		// TODO Auto-generated method stub
		return productoDao.findById(id).orElse(null);
	}


	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		// TODO Auto-generated method stub
		return facturaDao.findById(id).orElse(null);
	}


	@Override
	@Transactional
	public void deleteFactura(long id) {
		// TODO Auto-generated method stub
		facturaDao.deleteById(id);
	}

	
}
