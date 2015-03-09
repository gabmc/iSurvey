package domain.reports.service;

import java.io.ByteArrayOutputStream;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import dinamica.*;

/**
 * Reporte de los servicios y roles  
 * Reporte PDF (IText) basico con logo, titulo, filtro de busqueda, tabla, 
 * total, grafico y pie de pagina hecho con codigo, con estilo "pagina X de Y". 
 * Para el manejo de los eventos que imprimen el pie de pagina, esta clase usa
 * una clase generica del framework llamada "PDFPageEvents". Puede proveer
 * su propio manejador de eventos si tiene necesidades mas elaboradas.
 * <br><br>
 * Actualizado: 2007-09-06<br>
 * Framework Dinamica - Distribuido bajo licencia LGPL<br>
 * @author mcordova (martin.cordova@gmail.com)
 */
public class PDFReportService extends AbstractPDFOutput
{

	//parametros requeridos para el footer
    PdfTemplate tpl = null;
    BaseFont bf = null;
    PdfContentByte cb = null;
    Image img = null;
    Font tblHeaderFont = null;
    Font tblBodyFont = null;
    
    //cambiar: parametros generales del reporte	
    String reportTitle = ""; //lo lee de config.xml por defecto
    String footerText = ""; //lo lee de web.xml o config.xml por defecto
    String logoPath = "/images/logo-dinamica.png"; //ubicacion del logotipo
    String pageXofY = " de ";  //texto por defecto para Pagina X de Y
    
    protected void createPDF(GenericTransaction data, ByteArrayOutputStream buf)
            throws Throwable
    {

		//inicializar documento: tamano de pagina, orientacion, margenes
		Document doc = new Document();
		PdfWriter docWriter = PdfWriter.getInstance(doc, buf);
		doc.setPageSize(PageSize.LETTER.rotate());
		doc.setMargins(30,30,30,40);
		
		doc.open();

		//crear fonts por defecto
		tblHeaderFont = new Font(Font.HELVETICA, 10f, Font.BOLD);
		tblBodyFont = new Font(Font.HELVETICA, 10f, Font.NORMAL);
		
		//definir pie de pagina del lado izquierdo
		String footerText = this.getFooter(); //read it from config.xml or web.xml
		String reportDate = StringUtil.formatDate(new java.util.Date(), "dd-MM-yyyy HH:mm");
		
		//crear template (objeto interno de IText) y manejador de evento 
		//para imprimir el pie de pagina
		bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		cb = docWriter.getDirectContent();
		tpl = cb.createTemplate(20, 14);
		docWriter.setPageEvent(new PDFPageEvents(footerText, pageXofY, tpl, bf, cb, reportDate));

		//titulo - lo lee de config.xml por defecto
		reportTitle = getReportTitle();
		Paragraph t = new Paragraph(reportTitle, new Font(Font.HELVETICA, 14f, Font.BOLD));
		t.setAlignment(Rectangle.ALIGN_RIGHT);
		doc.add(t);

		//logo
		img = Image.getInstance(getImage(this.getServerBaseURL() + logoPath, false));
		img.scalePercent(100);
		float imgY = doc.top() - img.getHeight();
		float imgX = doc.left();
		img.setAbsolutePosition(imgX, imgY);
		doc.add(img);	
		
		//blank line
		doc.add(new Paragraph(" "));
		//blank line
		doc.add(new Paragraph(" "));
		//blank line
		doc.add(new Paragraph(" "));
		//blank line
		doc.add(new Paragraph(" "));
		
		//print master section
		doc.add(getApplication(data));
		
		//blank line
		doc.add(new Paragraph(" "));
		
		//imprimir tabla principal de data
		PdfPTable datatbl = getDataTable(data);
		datatbl.setSpacingBefore(10);
		doc.add(datatbl);

		doc.close();
		docWriter.close();
		
    }

    Paragraph getApplication(GenericTransaction data) throws Throwable
	{

    	Recordset rs = data.getRecordset("service.sql");
		rs.first();
		
		String text = "Aplicación: " + rs.getString("aplication");
		
		Paragraph t = new Paragraph(text,new Font(Font.HELVETICA, 12f, Font.BOLD));
		t.setAlignment(Rectangle.ALIGN_LEFT);
		return t;
		
	}
	/**
	 * Retorna una tabla conteniendo la data y los totales
	 * @param data Objeto de negocios que suple los recordsets
	 * @return
	 */
	PdfPTable getDataTable(GenericTransaction data) throws Throwable
	{

		ServiceRoles x = (ServiceRoles)data;
		
		//obtener recordset de data
		Recordset rs = data.getRecordset("service.sql");
		rs.top();

		//definir estructura de la tabla
		PdfPTable datatable = new PdfPTable(3);
		datatable.getDefaultCell().setPadding(3);
		int headerwidths[] = {45,40,10};
		datatable.setWidths(headerwidths);
		datatable.setWidthPercentage(100);
		datatable.setHeaderRows(1);

		PdfPCell c = null;
		String v = "";
		
		//encabezados de columnas
		c = new PdfPCell( new Phrase("Servicio o action", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);
		
		//encabezados de columnas
		c = new PdfPCell( new Phrase("Descripción", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);
	
		c = new PdfPCell( new Phrase("Roles autorizados", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);


		//imprimir cuerpo de la tabla
		while (rs.next())
		{			
			v = rs.getString("path");
			c = new PdfPCell( new Phrase( v, tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);
			
			v = rs.getString("description");
			c = new PdfPCell( new Phrase( v, tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);

			Recordset rs2 = x.getRoles(rs);
			
			String role = "";
			
			while (rs2.next())
			{
				role = role + rs2.getString("rolename") + "\n";	
			}
			c = new PdfPCell( new Phrase(role, tblBodyFont ));
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);
		}
		
		return datatable;
		
	}    
    
}
