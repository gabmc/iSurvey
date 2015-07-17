package aplicador;

import dinamica.AbstractExcelOutput;
import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.RecordsetField;
import dinamica.StringUtil;
import java.io.ByteArrayOutputStream;
import javax.xml.xpath.XPathExpressionException;
import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import dinamica.xml.*;

/**
 * Clase generica para exportar un archivo excel con el contenido de un recordset,
 * Esta clase lee del config.xml un conjunto de elementos para imprimir las columnas y filas.
 * Se definen elementos en el config.xml como en el ejemplo:<br>
 * <xmp>
 * <excel recordset="query.sql" sheetname="Reporte" filename="data.xls" date-format="dd-MM-yyyy">
 *		<col id="col1" label="Columna 1"/>
 *		<col id="col2" label="Columna 2"/>
 *		<col id="col3" label="Columna 3"/>
 *		<col id="col4" label="Columna 4"/>
 *		<col id="col5" label="Columna 5"/>
 *		<col id="col6" label="Columna 6"/>
 *	</excel>
 *</xmp>
 * <br><br>
 * El atributo date-format es opcional, la mascara por defecto se lee de web.xml, del parametro
 * de contexto def-format-date.
 * <br><br>
 * Actualizado: 2008-07-28<br>
 * Framework Dinamica - Distribuido bajo licencia LGPL<br>
 * @author Martin Cordova y Asociados C.A.
 */
public class CrearExcelEjemplo extends AbstractExcelOutput {

    /**
     * Crear un workbook para exportar un recordset como un archivo excel
     * @param data Data object passed by the Transaction object to this Output object
     * @return Workbook
     * @throws Throwable
     */
    public WritableWorkbook createWorkbook(GenericTransaction data, ByteArrayOutputStream buf) throws Throwable {

        String dateFmt = getContext().getInitParameter("def-format-date");

        //obtener referencia a config.xml
        Document doc = getConfig().getDocument();
        String customDateFmt = doc.getElement("excel").getAttribute("date-format");
        if (customDateFmt == null) {
            customDateFmt = dateFmt;
        }

        //crear el workbook
        WritableWorkbook wb = Workbook.createWorkbook(buf);
        WritableSheet sheet = wb.createSheet(doc.getElement("excel").getAttribute("sheetname"), 0);
        WritableCellFormat dateFormat = new WritableCellFormat(new DateFormat(customDateFmt));

        //codigo que lee los nombres de las columnas, campos y recordset del config.xml
        //Element cols[] = doc.getElements("//col");

        beforeData(sheet, data, 0);

        //obtener recordset de data
        Recordset rs = data.getRecordset(doc.getElement("excel").getAttribute("recordset"));
        rs.top();

        //int count = sheet.getRows();

        //String lapso = (String) getRequest().getParameter("lapso_alias");

        //Label llapso = new Label(0, count, "Lapso: " + lapso);
        //sheet.addCell(llapso);

        //count++;

        //obtener todos los label del config.xml
//        for (int i = 0; i < cols.length; i++) {
//            Label label = new Label(i, count, cols[i].getAttribute("label"));
//            sheet.addCell(label);
//        }
        

        int fila = 0;
        int columna = 0;
        
        Label etiqueta = new Label(columna, fila, "Identificador");
        sheet.addCell(etiqueta);
        columna++;
        
        //headers
        while (rs.next()) {
        	String tipo = "";
        	if (rs.getString("type").equals("S"))
        		tipo = "(texto)";
        	if (rs.getString("type").equals("T"))
        		tipo = "(texto)";
        	if (rs.getString("type").equals("L"))
        		tipo = "(seleccion simple)";
        	if (rs.getString("type").equals("N"))
        		tipo = "(numero)";
        	if (rs.getString("type").equals("D"))
        		tipo = "(fecha)";
        	if (rs.getString("type").equals("5"))
        		tipo = "(elegir del 1 al 5)";
        	if (rs.getString("type").equals("!"))
        		tipo = "(lista desplegable)";
        	if (rs.getString("type").equals("M"))
        		tipo = "(seleccion multiple)";
        	etiqueta = new Label(columna, fila, "Pregunta " + String.valueOf(columna) + ": " + rs.getString("question") + " " + tipo);
        	sheet.addCell(etiqueta);
        	columna++;
        }
        
        fila++;
        columna = 0;
        
        etiqueta = new Label(columna, fila, "4576882");
        sheet.addCell(etiqueta);
        columna++;
        rs.top();
        
        //respuestas
        while (rs.next()) {
        	if (rs.getString("type").equals("S")){
        		etiqueta = new Label(columna, fila, "Desarrollo Integral");
            	sheet.addCell(etiqueta);
        	}
        	if (rs.getString("type").equals("T")){
        		etiqueta = new Label(columna, fila, "Av. Fco De Miranda con Principal de Los Ruices " +
        				"Edif. Centro Empresarial Miranda " +
        				"Piso 0. Of. 0-D Urb. Los Ruices");
            	sheet.addCell(etiqueta);
        	}
        	if (rs.getString("type").equals("L")){
        		etiqueta = new Label(columna, fila, "Masculino");
            	sheet.addCell(etiqueta);
        	}
        	if (rs.getString("type").equals("N")){
        		etiqueta = new Label(columna, fila, "435");
            	sheet.addCell(etiqueta);
        	}
        	if (rs.getString("type").equals("D")){
        		etiqueta = new Label(columna, fila, "23-09-1900");
            	sheet.addCell(etiqueta);
        	}
        	if (rs.getString("type").equals("5")){
        		etiqueta = new Label(columna, fila, "3");
            	sheet.addCell(etiqueta);
        	}
        	if (rs.getString("type").equals("!")){
        		etiqueta = new Label(columna, fila, "Amazonas");
            	sheet.addCell(etiqueta);
        	}
        	if (rs.getString("type").equals("M")){
        		etiqueta = new Label(columna, fila, "S;S;N;S;N;N");
            	sheet.addCell(etiqueta);
        	}
        	columna++;
        }

int count = 1;
        afterData(sheet, data, count);

        wb.write();
        wb.close();

        //retornar documento para su impresion hacia el browser
        return wb;

    }

    @Override
    protected String getAttachmentString() {

        String fileName = "data.xls";
        try {
            fileName = getConfig().getDocument().getElement("excel").getAttribute("filename");
        } catch (XPathExpressionException e) {
        }
        return "attachment; filename=\"" + fileName + "\";";
    }

    /**
     * Metodo que permite añadir data a la hoja de calculo antes
     * de la imprimir la data del detalle, es especial para los casos
     * cuando se desea imprimir titulos antes de la data.
     * @param sheet Hoja de calculo
     * @param data Objeto Transaction que contiene los recordsets del Action
     * @param countReg Row donde esta posicionada la hoja de calculo
     * @return sheet Hoja de calculo
     * @throws Throwable
     */
    protected WritableSheet beforeData(WritableSheet sheet, GenericTransaction data, int countReg) throws Throwable {
        return sheet;
    }

    /**
     * Metodo que permite añadir data a la hoja de calculo despues
     * de la imprimir la data del detalle, es especial para los casos
     * cuando se desea imprimir un total.
     * @param sheet Hoja de calculo
     * @param data Objeto Transaction que contiene los recordsets del Action
     * @param countReg Row donde esta posicionada la hoja de calculo
     * @return sheet Hoja de calculo
     * @throws Throwable
     */
    protected WritableSheet afterData(WritableSheet sheet, GenericTransaction data, int countReg) throws Throwable {
        return sheet;
    }
}
