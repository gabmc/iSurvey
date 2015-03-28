package domain.reports.user;

import java.io.ByteArrayOutputStream;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import dinamica.*;

/**
 * Reporte de los usuarios y roles asignados 
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
public class PDFReportUser extends AbstractPDFOutput
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

		//imprimir filtro de busqueda
		PdfPTable filter = getReportFilter(data);
		filter.setSpacingBefore(40);
		doc.add(filter);
		
		//imprimir tabla principal de data 
		PdfPTable datatbl = getDataTable(data);
		datatbl.setSpacingBefore(10);
		doc.add(datatbl);

		doc.close();
		docWriter.close();
		
    }

	/**
	 * Retorna una tabla conteniendo el filtro de busqueda
	 * @param data Objeto de negocios que suple los recordsets
	 * @return
	 */
	PdfPTable getReportFilter(GenericTransaction data) throws Throwable
	{

		//obtener recordset de filtro
		Recordset rs = data.getRecordset("user.filter");
		rs.first();
		
		//definir estructura de la tabla
		PdfPTable datatable = new PdfPTable(2);
		datatable.getDefaultCell().setPadding(3);
		int headerwidths[] = {50,50};
		datatable.setWidths(headerwidths);
		datatable.setWidthPercentage(40);
		datatable.setHeaderRows(1);
		
		PdfPCell c = null;
		String d = null;

		//encabezado para toda la tabla
		c = new PdfPCell(new Phrase("Filtro de Búsqueda", tblHeaderFont ));
		c.setGrayFill(0.95f);
		c.setColspan(2);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);
		
		/* campos del filtro - solo se imprimen si no son nulos*/
		
		if (!rs.isNull("userlogin")) {
			c = new PdfPCell(new Phrase("Login del usuario comienza", tblHeaderFont));
			c.setGrayFill(0.95f);
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);
		
			d = rs.getString("userlogin");
			c = new PdfPCell( new Phrase( d , tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);	
		}
		
		if (!rs.isNull("fname")) {
			c = new PdfPCell(new Phrase("Apellido comienza", tblHeaderFont));
			c.setGrayFill(0.95f);
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);
			
			d = rs.getString("fname");
			c = new PdfPCell( new Phrase( d , tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);	
		}

		if (!rs.isNull("lname")) {
			c = new PdfPCell(new Phrase("Nombre comienza", tblHeaderFont));
			c.setGrayFill(0.95f);
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);
			
			d = rs.getString("lname");
			c = new PdfPCell( new Phrase( d , tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);	
		}
		
		if (datatable.getRows().size()==1)
		{
			c = new PdfPCell(new Phrase("-- Sin filtro de búsqueda --", tblHeaderFont));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			c.setColspan(2);
			datatable.addCell(c);
		}
		
		return datatable;
		
	}

	
	/**
	 * Retorna una tabla conteniendo la data y los totales
	 * @param data Objeto de negocios que suple los recordsets
	 * @return
	 */
	PdfPTable getDataTable(GenericTransaction data) throws Throwable
	{

		UserRoles x = (UserRoles)data;
		
		//obtener recordset de data
		Recordset rs = data.getRecordset("user.results");
		rs.top();

		//definir estructura de la tabla
		PdfPTable datatable = new PdfPTable(6);
		datatable.getDefaultCell().setPadding(3);
		int headerwidths[] = {15,10,10,20,8,50};
		datatable.setWidths(headerwidths);
		datatable.setWidthPercentage(100);
		datatable.setHeaderRows(1);

		PdfPCell c = null;
		String v = "";
		
		//encabezados de columnas
		c = new PdfPCell( new Phrase("Login de Usuario", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);
		
		//encabezados de columnas
		c = new PdfPCell( new Phrase("Apellido", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);

		c = new PdfPCell( new Phrase("Nombre", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);
		
		c = new PdfPCell( new Phrase("E-mail", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);
		
		c = new PdfPCell( new Phrase("Tipo", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);
		
		c = new PdfPCell( new Phrase("Roles", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);


		//imprimir cuerpo de la tabla
		while (rs.next())
		{			
			v = rs.getString("userlogin");
			c = new PdfPCell( new Phrase( v, tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);
			
			v = rs.getString("lname");
			c = new PdfPCell( new Phrase( v, tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);
			
			v = rs.getString("fname");
			c = new PdfPCell( new Phrase( v, tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);
			
			v = rs.getString("email");
			c = new PdfPCell( new Phrase( v, tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);
			
			v = rs.getString("tipo");
			c = new PdfPCell( new Phrase( v, tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);

			Recordset rs2 = x.getRoles(rs);
			
			String role = "";
			
			while (rs2.next())
			{
				role = role + rs2.getString("description") + "/" + rs2.getString("rolename") + "\n";	
			}
			c = new PdfPCell( new Phrase(role, tblBodyFont ));
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);
		}
		
		return datatable;
		
	}    
    
}
