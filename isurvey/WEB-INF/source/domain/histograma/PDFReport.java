package domain.histograma;

import java.io.ByteArrayOutputStream;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import dinamica.*;

/**
 * Reporte PDF (IText) del histograma de operaciones con logo, titulo, filtro de busqueda, 
 * grafico y pie de pagina hecho con codigo, con estilo "pagina X de Y". 
 * Para el manejo de los eventos que imprimen el pie de pagina, esta clase usa
 * una clase generica del framework llamada "PDFPageEvents". Puede proveer
 * su propio manejador de eventos si tiene necesidades mas elaboradas.
 * <br><br>
 * Actualizado: 2007-05-29<br>
 * Framework Dinamica - Distribuido bajo licencia LGPL<br>
 * @author mcordova (martin.cordova@gmail.com)
 */
public class PDFReport extends AbstractPDFOutput
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
		doc.setPageSize(PageSize.LETTER);
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
		filter.setSpacingBefore(60);
		doc.add(filter);
		
		//imprimir grafico
		img = Image.getInstance(getImage(this.getServerBaseURL() + "/action/audit/histograma/chart", false));
		img.scalePercent(70);
		img.setAlignment(Element.ALIGN_CENTER);
		img.setSpacingBefore(10);
		doc.add(img);			
		
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
		Recordset rs = data.getRecordset("viewchart.filter");
		rs.first();
		
		//definir estructura de la tabla
		PdfPTable datatable = new PdfPTable(2);
		datatable.getDefaultCell().setPadding(3);
		int headerwidths[] = {50,50};
		datatable.setWidths(headerwidths);
		datatable.setWidthPercentage(45);
		
		PdfPCell c = null;
		String d = null;

		//encabezado para toda la tabla
		c = new PdfPCell(new Phrase("Filtro de Búsqueda", tblHeaderFont ));
		c.setGrayFill(0.95f);
		c.setColspan(2);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);
		
		//fila 1 - campo de filtro
		c = new PdfPCell(new Phrase("Fecha desde", tblHeaderFont));
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		datatable.addCell(c);
		
		//fila 1 - valor de busqueda
		d = StringUtil.formatDate(rs.getDate("fdesde"), "dd-MM-yyyy");
		c = new PdfPCell( new Phrase( d , tblBodyFont ) );
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		datatable.addCell(c);	

		//fila 2 - campo de filtro
		c = new PdfPCell(new Phrase("Fecha hasta", tblHeaderFont));
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		datatable.addCell(c);
		
		//fila 2 - valor de busqueda
		d = StringUtil.formatDate(rs.getDate("fhasta"), "dd-MM-yyyy");
		c = new PdfPCell( new Phrase( d , tblBodyFont ) );
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		datatable.addCell(c);	
		
		//fila 3 - campo de filtro
		c = new PdfPCell(new Phrase("Aplicación", tblHeaderFont));
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		datatable.addCell(c);
		
		//fila 3 - valor de busqueda
		d = rs.getString("contexto");
		c = new PdfPCell( new Phrase( d , tblBodyFont ) );
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		datatable.addCell(c);
		
		//fila 4 - campo de filtro
		c = new PdfPCell(new Phrase("Operación", tblHeaderFont));
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		datatable.addCell(c);
		
		//fila 4 - valor de busqueda
		d = rs.getString("operation");
		c = new PdfPCell( new Phrase( d , tblBodyFont ) );
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		datatable.addCell(c);
		
		//fila 5 - campo de filtro
		c = new PdfPCell(new Phrase("Area", tblHeaderFont));
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		datatable.addCell(c);
		
		//fila 5 - valor de busqueda
		d = rs.getString("area");
		c = new PdfPCell( new Phrase( d , tblBodyFont ) );
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		datatable.addCell(c);
		
		return datatable;
		
	}

}
