package com.udemy.spring.app.view.csv;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.udemy.spring.app.models.entity.Cliente;

@Component("listar.csv")
public class ClienteCsvView  extends AbstractView{

	
	
	public ClienteCsvView() {
		setContentType("text/csv");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean generatesDownloadContent() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		response.setHeader("Content-Disposition", "attachment; filename=\"clientes.csv\"");
		response.setContentType(getContentType());
		
		@SuppressWarnings("unchecked")
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");

		ICsvBeanWriter beanWriter = new CsvBeanWriter( response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] header = {"id", "nombre", "apellido", "email", "createAt"};
		beanWriter.writeHeader(header);
		
		for (Cliente cliente : clientes) {
			beanWriter.write(cliente, header);
		}
		beanWriter.close();
	}

}
