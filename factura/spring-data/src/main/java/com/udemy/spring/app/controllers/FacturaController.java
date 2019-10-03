package com.udemy.spring.app.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.udemy.spring.app.models.entity.Cliente;
import com.udemy.spring.app.models.entity.Factura;
import com.udemy.spring.app.models.entity.ItemFactura;
import com.udemy.spring.app.models.entity.Producto;
import com.udemy.spring.app.service.IClienteService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	private IClienteService clienteService;
	
	
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value="clienteId") Long clienteId, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(clienteId);
		if (cliente == null) {
			flash.addFlashAttribute("error", "El Cliente no Existe");
			return "redirect:/listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente);
		
		model.put("factura", factura);
		model.put("titulo", "Crear Factura");
		return "factura/form";
	}
	
	@GetMapping(value="/cargar-productos/{term}", produces = {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){
		return clienteService.findByNombre(term);
		
	}
	
	@PostMapping("/form")
	public String guardar(@Valid Factura factura, BindingResult result,
			Model model,
			@RequestParam(name="item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
			RedirectAttributes flash, SessionStatus status
			) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo","Crear Factura");
			return "factura/form";
		}
		
		if (itemId == null ||  itemId.length == 0) {
			model.addAttribute("titulo", "Crear Factura");
			model.addAttribute("error", "Error, La Factura debe tener Lineas de Productos");
			return "factura/form";
		}
		
		
		for (int i = 0; i < itemId.length; i++) {
			Producto producto = clienteService.findProductoById(itemId[i]);
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
		}
		
		clienteService.saveFactura(factura);
		status.setComplete();
		flash.addFlashAttribute("success", "Factura Creada con Exito");
		return "redirect:/ver/" + factura.getCliente().getId(); 
	}
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value="id")Long id,
			Model model, RedirectAttributes flash
			) {
		Factura factura = clienteService.findFacturaById(id);
		
		if (factura == null) {
			flash.addFlashAttribute("error", "La Factura no Existe!");
			return "redirect:/listar";
			
		}
		
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Factura : ".concat(factura.getDescripcion()));
		
		return "factura/ver";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id,
			RedirectAttributes flash
			) {
		Factura factura = clienteService.findFacturaById(id);
		if (factura != null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", "Factura fue Eliminada con Exito");
			return "redirect:/ver/" + factura.getCliente().getId();
		}
		
		flash.addFlashAttribute("error", "Factura no encontrada");
		
		return "redirect:/listar";
	}
	
}
