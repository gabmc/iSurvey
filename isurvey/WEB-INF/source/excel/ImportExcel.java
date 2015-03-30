package excel;

import java.io.File;
import java.util.Date;
import jxl.*;
import dinamica.*;
import java.util.Map;

/**
 * Clase que lee una hoja de calculo de archivo Excel, la valida contra la
 * base de datos, verifica que los registros sean del tipo Date, Integer,
 * Double, character varying. Graba los registros en batch usando una tecnica muy
 * avanzada que incluye el framework. Además para el manejo de excepciones
 * genera un recordset con los errores.
 * <br><br>
 * (c) 2008 Martin Cordova y Asociados<br>
 * This code is released under the LGPL license<br>
 * Dinamica Framework - http://www.martincordova.com<br>
 * */

public class ImportExcel extends GenericTableManager  {

    //variable que contendra el nombre de la columna que produjo la excepcion
    String columna = null;

    @Override
    public int service(Recordset inputParams) throws Throwable {

        int rc = super.service(inputParams);

        //crea un recordset para el manejo de los errores
        Recordset rsError = new Recordset();

        //define la estructura del recordset que contendra los errores
        rsError.append("columna", java.sql.Types.VARCHAR);
        rsError.append("fila", java.sql.Types.INTEGER);
        rsError.append("error", java.sql.Types.VARCHAR);

        //se alamacena en sesion el recordset de errores validacion
        getSession().setAttribute("error.excel",rsError);

        //registrar el numero de errores
        inputParams.setValue("total_errores", new Integer(rsError.getRecordCount()));

        //archivo nulo sera considerado un error
        if (inputParams.isNull("file.filename"))
                throw new Throwable("¡Por favor indique una ruta válida de archivo!");

        //obtener parametros del validator
        String file = inputParams.getString("file");

        //obtener el archivo Excel
        Workbook wb;

        try {
                wb = Workbook.getWorkbook(new File(file));
        } catch (JXLException e1) {
                throw new Throwable ("Formato Excel no reconocido; use Excel 97, XP o 2003",e1);
        }

        //obtener la hoja de calculo del archivo
        Sheet sheet = wb.getSheet(0);

        //obtener el numero de registros
        int numOfRows = sheet.getRows();

        //crea un recordset que contendra los registro leidos del archivo Excel
        Recordset rs = new Recordset();

        //define la estructura del recordset
        rs.append("id_participante", java.sql.Types.INTEGER);
        rs.append("nombre_participante", java.sql.Types.VARCHAR);
        rs.append("apellido_participante", java.sql.Types.VARCHAR);
        rs.append("email_participante", java.sql.Types.VARCHAR);
        rs.append("empresa", java.sql.Types.VARCHAR);
        rs.append("cargo", java.sql.Types.VARCHAR);
        rs.append("supervisor", java.sql.Types.VARCHAR);
        rs.append("fecha_nacimiento", java.sql.Types.DATE);
        rs.append("fecha_ingreso", java.sql.Types.DATE);
        rs.append("sexo", java.sql.Types.VARCHAR);
        rs.append("tipo_nomina", java.sql.Types.VARCHAR);
        rs.append("funcion", java.sql.Types.VARCHAR);

        //obtener el numero de columnas
        int columnas = sheet.getColumns();

        //numero de columnas del archivo es igual a 10?
        if (columnas == 12)
        {
            //mientras exista registros
            for(int i = 1; i<numOfRows;i++)
            {
                try
                {
                    //añadir un record
                    rs.addNew();

                    //obtener la data de cada celda
                    Cell columna1 = sheet.getCell(0,i);
                    Cell columna2 = sheet.getCell(1,i);
                    Cell columna3 = sheet.getCell(2,i);
                    Cell columna4 = sheet.getCell(3,i);
                    Cell columna5 = sheet.getCell(4,i);
                    Cell columna6 = sheet.getCell(5,i);
                    Cell columna7 = sheet.getCell(6,i);
                    Cell columna8 = sheet.getCell(7,i);
                    Cell columna9 = sheet.getCell(8,i);
                    Cell columna10 = sheet.getCell(9,i);
                    Cell columna11 = sheet.getCell(10,i);
                    Cell columna12 = sheet.getCell(11,i);

                    //la celda esta vacia?
                    if (columna1.getContents() == null || columna1.getContents().equals(""))
                    {
                            columna = "Identificador";
                            throw new Throwable ("La celda no puede estar vacia.");
                    }
                    else
                    {
                            //lee y valida celda de tipo Integer
                            Integer dcolum1 = ValidatorUtil.testInteger(columna1.getContents());
                            if (dcolum1!=null)
                            {
                                    findParticipante(dcolum1);
                                    rs.setValue("id_participante", dcolum1);
                            }
                            else
                            {
                                    columna = "Identificador";
                                    throw new Throwable ("El dato ingresado no representa un número.");
                            }
                    }
                            rs.setValue("nombre_participante", columna2.getContents());
                            rs.setValue("apellido_participante", columna3.getContents());
                            rs.setValue("email_participante", columna4.getContents());
                            rs.setValue("empresa", columna5.getContents());
                            rs.setValue("cargo", columna6.getContents());
                            rs.setValue("supervisor", columna7.getContents());

                            if (!columna8.getContents().equals(""))
                            {
                                Date dcolum2 = ValidatorUtil.testDate(columna8.getContents(), "dd-MM-yy");
                                if (dcolum2!=null)
                                    rs.setValue("fecha_nacimiento", dcolum2);
                                else{
                                    columna = "Fecha de Nacimiento";
                                    throw new Throwable ("La fecha no fue ingresada correctamente. Escriba una fecha en formato Día-Mes-Año y use como separador el guión (-)");
                                }
                            }

                            if (!columna9.getContents().equals(""))
                            {
                                Date dcolum3 = ValidatorUtil.testDate(columna9.getContents(), "dd-MM-yy");
                                if (dcolum3!=null)
                                    rs.setValue("fecha_ingreso", dcolum3);
                                else{
                                    columna = "Fecha de Ingreso";
                                    throw new Throwable ("La fecha no fue ingresada correctamente. Escriba una fecha en formato Día-Mes-Año y use como separador el guión (-)");
                                }
                            }
                            if (columna10.getContents().equals("F") || columna10.getContents().equals("M"))
                                rs.setValue("sexo", columna10.getContents());
                            rs.setValue("tipo_nomina", columna11.getContents());
                            rs.setValue("funcion", columna12.getContents());

            }
            catch (Throwable a)
            {
                    //añadir un nuevo record
                    rsError.addNew();
                    rsError.setValue("columna", columna);
                    rsError.setValue("fila", (i+1));
                    rsError.setValue("error", a.getMessage());

                    //registrar el numero de errores
                    inputParams.setValue("total_errores", new Integer(rsError.getRecordCount()));

                    //recordset de errores tienes 20 registros?
                    if (rsError.getRecordCount()>20)
                    {
                            getSession().setAttribute("error.excel",rsError);
                            throw new Throwable("El archivo de Excel contiene más de 20 errores.");
                    }
            }
            }
        }
        else
        {
                throw new Throwable("El archivo de Excel no contiene el formato especifícado.");
        }

        //recordset de errores tiene registro?
        if (rsError.getRecordCount()>0)
        {
            getSession().setAttribute("error.excel",rsError);
            throw new Throwable("El archivo de Excel contiene errores.");
        }

        //registrar el numero de registros insertados
        inputParams.setValue("total_registros", new Integer(rs.getRecordCount()));

        //dar inicio a la transacción
        getDb().beginTrans();

        //ejecutar el insert de lotes
        //getDb().exec(getSQL(getResource("insert_lote.sql"), inputParams));

        //definir parametros
        String[] params =
        {
                "id_participante",
                "nombre_participante",
                "apellido_participante",
                "email_participante",
                "empresa",
                "cargo",
                "supervisor",
                "fecha_nacimiento",
                "fecha_ingreso",
                "sexo",
                "tipo_nomina",
                "funcion"
        };

        //obtener el Resource
        String sql = getSQL(getResource("insert.sql"), inputParams);

        //ejecutar en Batch
        getDb().execBatch(sql, rs, params);
        //getDb().commit();
        rs.first();
        Map parametros = this.getRequest().getParameterMap();
        String idLista = ((String[]) parametros.get("id_lista_participantes"))[0];
        String userlogin = ((String[]) parametros.get("userlogin"))[0];
        
        String query = StringUtil.replace(getResource("insert_int_participante_lista.sql"), 
	            "{{id_participante}}", rs.getString("id_participante"));
	        query = StringUtil.replace(query, "{{id_lista_participantes}}", idLista);
	        query = StringUtil.replace(query, "{{userlogin}}", userlogin);
	        System.out.println("id_participante: "+rs.getString("id_participante"));
	        getDb().exec(query);
        
        while (rs.next()){
	        query = StringUtil.replace(getResource("insert_int_participante_lista.sql"), 
	            "{{id_participante}}", rs.getString("id_participante"));
	        query = StringUtil.replace(query, "{{id_lista_participantes}}", idLista);
	        query = StringUtil.replace(query, "{{userlogin}}", userlogin);
	        System.out.println("id_participante: "+rs.getString("id_participante"));
	        getDb().exec(query);
        }
        getDb().commit();
        return rc;



    }

    /**
     * Verifica que el participante existe en base de datos
     * @param participante ID del participante
     * @return ID del participante
     * @throws Throwable
     */
    Integer findParticipante (Integer participante) throws Throwable
    {
            Recordset rs = getRecordset("participante.sql");

            if (rs.findRecord("id_participante", participante) == -1)
                    return participante;
            else
                    throw new Throwable("El Identificador del Participante [" + participante + "] ya está registrado.");
    }

}
