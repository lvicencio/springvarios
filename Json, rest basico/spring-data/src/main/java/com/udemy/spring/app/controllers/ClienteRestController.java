package com.udemy.spring.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.udemy.spring.app.models.entity.Cliente;
import com.udemy.spring.app.service.IClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;
	
	@GetMapping(value="/listar")
	public List<Cliente> listarRest(){
		return clienteService.findAll();
	}
	
	
}
