package com.udemy.spring.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.udemy.spring.app.models.entity.Cliente;
import com.udemy.spring.app.service.IClienteService;
import com.udemy.spring.app.util.paginator.PageRender;


@Controller
public class ClienteController {

	
	
	@Autowired
	private IClienteService clienteService;
	
	@Secured("ROLE_USER")
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash ) {
		
		Cliente cliente = clienteService.findOne(id);
		if (cliente==null) {
			flash.addFlashAttribute("error", "El Cliente no Existe en la DB");
			return "redirect:/listar";
		}
		
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Detalle del Cliente: " + cliente.getNombre());
		
		return "ver";
		
	}
	
	@GetMapping(value="/listar-rest")
	public @ResponseBody List<Cliente> listarRest(){
		return clienteService.findAll();
	}
	
	
	@RequestMapping(value= {"/listar", "/" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {
		
		//prueba
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(hasRole("ROLE_ADMIN")) {
			System.out.println(auth.getName().concat(" Tienes Acceso! "));
		} else {
			System.out.println(auth.getName().concat(" NO Tienes Acceso! "));
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		
		if (securityContext.isUserInRole("ADMIN")) {
			System.out.println(auth.getName().concat(" Tienes Acceso!  -> por securityContext "));
		} else {
			System.out.println(auth.getName().concat(" NO Tienes Acceso! -> por securityContext "));
		}
		
		
		Pageable pageRequest = PageRequest.of(page, 6);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
		
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
		
	}
	
	/*  Listar sin Paginación
	 * @RequestMapping(value="/listar", method = RequestMethod.GET) public String
	 * listar( Model model) { model.addAttribute("titulo", "Listado de Clientes");
	 * model.addAttribute("clientes", clienteService.findAll()); return "listar";
	 * 
	 * }
	 */
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/form")
	public String crear(Model model) {
		
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Formulario de Cliente");
		return "form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto,RedirectAttributes flash) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		
		if (!foto.isEmpty()) {
			/*
			 * Path directorioRecursos = Paths.get("src//main//resources//static/uploads");
			 * String rootPath = directorioRecursos.toFile().getAbsolutePath();
			 */
			String rootPath = "D://Spring//imagenes//uploads";
			
			try {
				byte[] bytes = foto.getBytes();
				Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
				Files.write(rutaCompleta, bytes);
				flash.addFlashAttribute("info", "Se subio correctamente '" + foto.getOriginalFilename() + "'" );
				
				cliente.setFoto(foto.getOriginalFilename());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		String msg = (cliente.getId() != null)? "Cliente Editado con Exito!" : "Cliente Guardado con Exito";
		
		clienteService.save(cliente);
		flash.addFlashAttribute("success", msg);
		return "redirect:/listar";
		
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Model model, RedirectAttributes flash) {

		Cliente cliente = null;
		
		if (id>0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El Cliente No EXISTE en el Sistema");
				return "redirect:/listar";
			}
		}else {
			flash.addFlashAttribute("error", "El Cliente No Es Valido");
			return "redirect:/listar";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Formulario de Cliente");
		return "form";		
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente Eliminado con Exito");
		}
		return "redirect:/listar";		
	}
	
	private boolean hasRole(String role) {
		
		SecurityContext context = SecurityContextHolder.getContext() ;
		if (context == null) {
			return false;
		} 
		
		Authentication auth = context.getAuthentication();
		
		if ( auth == null) {
			return false;
		}
		
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.contains(new SimpleGrantedAuthority(role));
		
		/*
		 * for (GrantedAuthority authority : authorities) {
		 * 
		 * if (role.equals(authority.getAuthority())) {
		 * System.out.println(auth.getName().concat(" tu Role es: "
		 * .concat(authority.getAuthority()))); return true; } }
		 * 
		 * return false;
		 */
		
	}
	
	
	
}
