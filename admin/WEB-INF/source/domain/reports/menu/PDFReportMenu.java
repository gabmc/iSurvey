package domain.reports.menu;

import java.io.ByteArrayOutputStream;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import dinamica.*;

/**
 * Reporte de los menus e items con su servicio
 * <br><br>
 * (c) 2007 Martin Cordova<br>
 * This code is released under the LGPL license<br>
 * Dinamica Framework - http://www.martincordova.com
 * @author Martin Cordova (dinamica@martincordova.com)
 * */
public class PDFReportMenu extends AbstractPDFOutput
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
    
    @Override
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

				
		//for each master record print a master/detail section
		MasterDetailReader dataobj = (MasterDetailReader)data;
		Recordset master = dataobj.getRecordset("master");
		master.top();
		while (master.next())
		{
			//blank line
			doc.add(new Paragraph(" "));

			//print master section
			doc.add( getGroupMaster(master) );
			
			//print detail section
			doc.add( getGroupDetail(master, dataobj.getDetail(master)) );
			
		}
		
		//print grand total
		doc.add(new Paragraph(" "));
		
		doc.close();
		docWriter.close();
		
	}

    /**
	 * Return group header (master)
	 * @param master Recordset containing master records
	 * @return
	 * @throws Throwable
	 */
    Paragraph getGroupMaster(Recordset master) throws Throwable
	{

		String text = "Aplicación: " + master.getString("aplication") + "\n";
		text = text + "Menú: " + master.getString("title");
		
		Paragraph t = new Paragraph(text,new Font(Font.HELVETICA, 12f, Font.BOLD));
		t.setAlignment(Rectangle.ALIGN_LEFT);
		return t;
		
	}

	/**
	 * Return a report section formatted as a table
	 * @param data
	 * @return
	 */
	PdfPTable getGroupDetail(Recordset master, Recordset detail) throws Throwable
	{

		//cols
		PdfPTable datatable = new PdfPTable(2);
            
		//header
		datatable.getDefaultCell().setPadding(1);
		int headerwidths[] = {50,50}; // percentage
		datatable.setWidths(headerwidths);
		datatable.setWidthPercentage(70); // percentage
		datatable.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.getDefaultCell().setBorderWidth(1);
		datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		
		PdfPCell c = null;
		String v = "";
		
		//encabezados de columnas
		c = new PdfPCell( new Phrase("ITEMS DEL MENÚ", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setColspan(2);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);

		c = new PdfPCell( new Phrase("Item del menú", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);

		c = new PdfPCell( new Phrase("Servicio", tblHeaderFont) );
		c.setGrayFill(0.95f);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable.addCell(c);
	
		while (detail.next())
		{
			v = detail.getString("description");
			c = new PdfPCell( new Phrase( v, tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);
			
			v = detail.getString("path");
			c = new PdfPCell( new Phrase( v, tblBodyFont ) );
			c.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable.addCell(c);
		}

		datatable.setSpacingBefore(20);
		return datatable;
		
	}

}
