package com.udemy.spring.app.dao;


//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.udemy.spring.app.models.entity.Cliente;


public interface IClienteDao  extends PagingAndSortingRepository<Cliente, Long>{

	//CrudRepository
	
}
