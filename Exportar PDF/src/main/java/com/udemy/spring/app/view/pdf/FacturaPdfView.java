package com.udemy.spring.app.view.pdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.udemy.spring.app.models.entity.Factura;
import com.udemy.spring.app.models.entity.ItemFactura;

@Component("factura/ver")
public class FacturaPdfView  extends AbstractPdfView{

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		Factura factura = (Factura) model.get("factura");
		//tabla 1
		PdfPTable tabla = new PdfPTable(1);
		tabla.setSpacingAfter(20);
		tabla.addCell("Datos del Cliente");
		tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		tabla.addCell(factura.getCliente().getEmail());
		//tabla 2
		PdfPTable tabla2 = new PdfPTable(1);
		tabla2.setSpacingAfter(20);;
		tabla2.addCell("Datos de la Factura");
		tabla2.addCell("Folio: " + factura.getId());
		tabla2.addCell("Descripción: " + factura.getDescripcion());
		tabla2.addCell("Fecha: " + factura.getCreateAt());
		//guarda las tablas al documento
		document.add(tabla);
		document.add(tabla2);
		//tabla del detalle de los productos
		PdfPTable tabla3 = new PdfPTable(4);
		tabla3.addCell("Producto");
		tabla3.addCell("Precio");
		tabla3.addCell("Cantidad");
		tabla3.addCell("Total");
				
		for (ItemFactura item : factura.getItems()) {
			tabla3.addCell(item.getProducto().getNombre());
			tabla3.addCell(item.getProducto().getPrecio().toString());
			tabla3.addCell(item.getCantidad().toString());
			tabla3.addCell(item.calcularImporte().toString());
		}
		
		PdfPCell celda = new PdfPCell(new Phrase("Total: "));
		celda.setColspan(3);
		celda.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabla3.addCell(celda);
		tabla3.addCell(factura.getTotal().toString());
		
		document.add(tabla3);
		
	}

}
