package aplicador;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import jxl.*;
import dinamica.*;

import java.util.Map;

import tokens_participantes.TokenGenerator;

/**
 * Clase que lee una hoja de calculo de archivo Excel, la valida contra la
 * base de datos, verifica que los registros sean del tipo Date, Integer,
 * Double, character varying. Graba los registros en batch usando una tecnica muy
 * avanzada que incluye el framework. AdemÃ¡s para el manejo de excepciones
 * genera un recordset con los errores.
 * <br><br>
 * (c) 2008 Martin Cordova y Asociados<br>
 * This code is released under the LGPL license<br>
 * Dinamica Framework - http://www.martincordova.com<br>
 * */

public class CargaManual extends GenericTableManager  {

    //variable que contendra el nombre de la columna que produjo la excepcion
    String columna = null;

    @Override
    public int service(Recordset inputParams) throws Throwable {
    	
    	///////////////////////////////////////////////////////////////////////////////////////////////////
    	
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idEstudio = ((String[]) parametros.get("id_estudio"))[0];
        
        Recordset instrumentos = getInstrumentosDeEstudio(idEstudio);
        instrumentos.top();
        String idEncuesta = "";
        while (instrumentos.next()){
        	idEncuesta = instrumentos.getString("id_instrumento");
        }
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////

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
                throw new Throwable("Â¡Por favor indique una ruta vÃ¡lida de archivo!");

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
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        
        Recordset preguntasOrdenadas = questionsOrdenadas(idEncuesta);
        preguntasOrdenadas.top();
        int numeroPreguntas = 0;
        while (preguntasOrdenadas.next())
        	numeroPreguntas++;
        preguntasOrdenadas.top();
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////

        //define la estructura del recordset
        rs.append("id_participante", java.sql.Types.INTEGER);
        
        while (preguntasOrdenadas.next()){
        	rs.append(preguntasOrdenadas.getString("question"), java.sql.Types.VARCHAR);
        }
        preguntasOrdenadas.top();

        //obtener el numero de columnas
        int columnas = sheet.getColumns();
        //numero de columnas del archivo es igual a 10?
        if (columnas == numeroPreguntas + 1){
            //mientras exista registros
            for(int i = 1; i<numOfRows;i++){
                try{
                    //aÃ±adir un record
                    rs.addNew();   
                    Cell columna1 = sheet.getCell(0,i);
                    //la celda esta vacia?
                    if (columna1.getContents() == null || columna1.getContents().equals("")){
                            columna = "A";
                            throw new Throwable ("La celda no puede estar vacia.");
                    }
                    else{
                            //lee y valida celda de tipo Integer
                            Integer dcolum1 = ValidatorUtil.testInteger(columna1.getContents());
                            if (dcolum1!=null){
                                TokenGenerator tg = new TokenGenerator();
                            	if (searchToken(tg.generarToken(String.valueOf(dcolum1), idEncuesta), idEncuesta) == true)
                                    rs.setValue("id_participante", dcolum1);
                            	else{
                            		columna = "A";
                            		throw new Throwable ("El Identificador del participante no se encuentra registrado en el estudio");
                            	}
                            }
                            else{
                                    columna = "A";
                                    throw new Throwable ("El dato ingresado no representa un número.");
                            }
                    }
                    preguntasOrdenadas.first();
                    for(int m = 1; m<=numeroPreguntas; m++){
                    	Cell columna2 = sheet.getCell(m,i);
                    	if ((preguntasOrdenadas.getString("mandatory").equals("Y")) && 
	                    		(columna2.getContents().equals("") || columna2.getContents() == null)){
	                    			columna = getExcelColumnName(m + 1);
	                    			throw new Throwable ("La pregunta es obligatoria");
	                    }
                    	else{
                    		if (preguntasOrdenadas.getString("type").equals("S"))			                
			                    rs.setValue(preguntasOrdenadas.getString("question"), columna2.getContents());
                    		
                    		if (preguntasOrdenadas.getString("type").equals("T"))		                
			                    rs.setValue(preguntasOrdenadas.getString("question"), columna2.getContents());
		                    
		                    if (preguntasOrdenadas.getString("type").equals("L")){
		                    	if (!columna2.getContents().equals("") || columna2.getContents() != null){
			                    	Recordset respuestas = answers(preguntasOrdenadas.getString("qid"));
			                    	respuestas.top();
			                    	int flag = 0;
			                    	String codigo = "";
			                    	while (respuestas.next()){
			                    		if (respuestas.getString("answer").equals(columna2.getContents())){
			                    			flag = 1;
			                    			codigo = respuestas.getString("code");
			                    		}
			                    	}
			                    	if ((flag == 0) && (!columna2.getContents().equals("-oth-"))){
		                                columna = getExcelColumnName(m + 1);
		                                throw new Throwable ("La respuesta no se encuentra en la lista de respuestas válidas");
			                    	}
			                    	else
			                    		rs.setValue(preguntasOrdenadas.getString("question"), codigo);
		                    	}
		                    	else
		                    		rs.setValue(preguntasOrdenadas.getString("question"), "");
		                    }
                    		
		                    if (preguntasOrdenadas.getString("type").equals("N")){
		                    	if (!columna2.getContents().equals("") || columna2.getContents() != null){
			                    	Integer t = ValidatorUtil.testInteger(columna2.getContents());
			                    	if (t == null){
		                                    columna = getExcelColumnName(m + 1);
		                                    throw new Throwable ("La respuesta debe ser un número");
		                            }
			                    	else
			                    		rs.setValue(preguntasOrdenadas.getString("question"), columna2.getContents());
		                    	}
		                    	else
		                    		rs.setValue(preguntasOrdenadas.getString("question"), "");
		                    }
		                    
		                    if (preguntasOrdenadas.getString("type").equals("D")){
		                    	if (!columna2.getContents().equals("") || columna2.getContents() != null){
			                    	Date t = ValidatorUtil.testDate(columna2.getContents(), "dd-MM-yy");
			                    	if (t == null){
		                                    columna = getExcelColumnName(m + 1);
		                                    throw new Throwable ("La respuesta debe ser una fecha con el formato dd/mm/yyyy");
		                            }
			                    	else{
			                    		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			                    		rs.setValue(preguntasOrdenadas.getString("question"), sdf.format(t));
			                    	}
		                    	}
		                    	else
		                    		rs.setValue(preguntasOrdenadas.getString("question"), "");
		                    }
		                    
		                    if (preguntasOrdenadas.getString("type").equals("5")){
		                    	if (!columna2.getContents().equals("") || columna2.getContents() != null){
			                    	Integer t = ValidatorUtil.testInteger(columna2.getContents());
			                    	if (t == null){
		                                    columna = getExcelColumnName(m + 1);
		                                    throw new Throwable ("La respuesta debe ser un número entre 1 y 5");
		                            }
			                    	else
			                    		rs.setValue(preguntasOrdenadas.getString("question"), columna2.getContents());
		                    	}
		                    	else
		                    		rs.setValue(preguntasOrdenadas.getString("question"), "");
		                    }
		                    
		                    if (preguntasOrdenadas.getString("type").equals("!")){
		                    	if (!columna2.getContents().equals("") || columna2.getContents() != null){
			                    	Recordset respuestas = answers(preguntasOrdenadas.getString("qid"));
			                    	respuestas.top();
			                    	int flag = 0;
			                    	String codigo = "";
			                    	while (respuestas.next()){
			                    		if (respuestas.getString("answer").equals(columna2.getContents())){
			                    			flag = 1;
			                    			codigo = respuestas.getString("code");
			                    		}
			                    	}
			                    	if (flag == 0){
		                                columna = getExcelColumnName(m + 1);
		                                throw new Throwable ("La respuesta no se encuentra en la lista de respuestas válidas");
			                    	}
			                    	else{
			                    		rs.setValue(preguntasOrdenadas.getString("question"), codigo);
			                    	}
		                    	}
		                    	else{
		                    		rs.setValue(preguntasOrdenadas.getString("question"), "");
		                    	}
		                    }
		                    
		                    if (preguntasOrdenadas.getString("type").equals("M")){
		                    	String respuesta = "";
		                    	if (!columna2.getContents().equals("") || columna2.getContents() != null){
			                    	String[] opciones = columna2.getContents().split(";");
			                    	Recordset opcionesBd = questionsDeQuestions(preguntasOrdenadas.getString("qid"));
			                    	if (opciones.length != opcionesBd.getRecordCount()){
			                    		columna = getExcelColumnName(m + 1);
			                    		throw new Throwable ("El número de opciones no coincide con el número de opciones registrados para la pregunta");
			                    	}
			                    	else{
					                    for (int q=0; q<=opcionesBd.getRecordCount()-1; q++){
					                    	if (!opciones[q].equals("S") && !opciones[q].equals("N")){
					                    		columna = getExcelColumnName(m + 1);
					                    		throw new Throwable ("Las opciones deben presentarse con \"S\" para un check o con \"N\" para un check vacío");
					                    	}
					                    	else{
						                    	if (opciones[q].equals("S"))
						                    		respuesta = respuesta + "Y";
						                    	else
						                    		respuesta = respuesta + "N";
					                    	}
					                    }
				                    	rs.setValue(preguntasOrdenadas.getString("question"), respuesta);
			                    	}
		                    	}
		                    }
		                    
//		                    if (preguntasOrdenadas.getString("type").equals("Y")){
//		                    	if (!columna2.getContents().equals("") || columna2.getContents() != null){
//		                    		if (columna2.getContents().equals("S"))
//		                    			rs.setValue(preguntasOrdenadas.getString("question"), "Y");
//		                    		if (columna2.getContents().equals("N"))
//		                    			rs.setValue(preguntasOrdenadas.getString("question"), "N");
//		                    	}
//		                    	else
//		                    		rs.setValue(preguntasOrdenadas.getString("question"), "");
//		                    }
		                    
		                    preguntasOrdenadas.next();
	                    }
                    }

            }
            catch (Throwable a){
                    //aÃ±adir un nuevo record
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
            rs.top();
        }
        else{
                throw new Throwable("El archivo de Excel no contiene el formato especificado.");
        }

        //recordset de errores tiene registro?
        if (rsError.getRecordCount()>0)
        {
            getSession().setAttribute("error.excel",rsError);
            throw new Throwable("El archivo de Excel contiene errores.");
        }

        //registrar el numero de registros insertados
        inputParams.setValue("total_registros", new Integer(rs.getRecordCount()));

        //dar inicio a la transacciÃ³n
        getDb().beginTrans();
     
     rs.top();
     TokenGenerator generator = new TokenGenerator();
     String idUsuario = getIdUsuario(this.getUserName());
     while (rs.next()){
    	 String query = updateConstructor(numeroPreguntas, idEncuesta, generator.generarToken(rs.getString("id_participante"), idEncuesta), rs, preguntasOrdenadas);
    	 getDb().exec(query);
    	 String registro = StringUtil.replace(getResource("insert-registro.sql"), "{{id_participante}}", rs.getString("id_participante"));
    	 registro = StringUtil.replace(registro, "{{id_instrumento}}", idEncuesta);
    	 registro = StringUtil.replace(registro, "{{id_usuario}}", idUsuario);
    	 getDb().exec(registro); 
     }
    getDb().commit();
    return rc;
    }

    
    Boolean findToken (String token) throws Throwable{
        String query = "select * from ajvieira_isurvey_app.int_participante_instrumento";
        Recordset rs = this.getDb().get(query);
        rs.top();
        while (rs.next()){
            if (rs.getString("token_participante").equals(token)){
                return true;
            }
        }
        return false;
    }
    
    Recordset questionsOrdenadas (String idEncuesta) throws Throwable{
    	String sql = "select * from ajvieira_isurvey_lime.questions where parent_qid = 0 and sid = " + idEncuesta;
    	Recordset questions = this.getDb().get(sql);
    	questions.top();
    	questions.sort("question_order");
    	return questions;
    }
    
    Recordset questionsDeQuestions (String idQuestionPadre) throws Throwable{
    	String sql = "select * from ajvieira_isurvey_lime.questions where parent_qid = " + idQuestionPadre;
    	Recordset questions = this.getDb().get(sql);
    	questions.top();
    	questions.sort("question_order");
    	return questions;
    }
    
    Recordset getNombresColumnas (String nombreTabla) throws Throwable{
    	String sql = "SELECT `COLUMN_NAME` " +
    			"FROM `INFORMATION_SCHEMA`.`COLUMNS` " +
    			"WHERE `TABLE_SCHEMA`='ajvieira_isurvey_lime' " +
    			"AND `TABLE_NAME`='"+nombreTabla+"';";
    	return this.getDb().get(sql);
    }
    
    Recordset getInstrumentosDeEstudio (String idEstudio) throws Throwable{
    	String query = "select instrumento.* " +
    			"from ajvieira_isurvey_app.estudio, " +
    			"ajvieira_isurvey_app.instrumento " +
    			"where " +
    			"instrumento.id_estudio = estudio.id_estudio " +
    			"and estudio.id_estudio = " + idEstudio;
    	Recordset instrumentos = this.getDb().get(query);
    	return instrumentos;
    }
    
    String getExcelColumnName(int number) {
        final StringBuilder sb = new StringBuilder();

        int num = number - 1;
        while (num >=  0) {
            int numChar = (num % 26)  + 65;
            sb.append((char)numChar);
            num = (num  / 26) - 1;
        }
        return sb.reverse().toString();
    }
    
    Recordset answers (String idQuestion) throws Throwable{
    	String sql = "select * from ajvieira_isurvey_lime.answers where qid = " + idQuestion;
    	return this.getDb().get(sql);
    }
    
    
    
    String updateConstructor (int numeroPreguntas, String idEncuesta, String token, Recordset respuestas, Recordset preguntas) throws Throwable{
   	 String query = "update ajvieira_isurvey_lime.survey_" + idEncuesta + " set submitdate=now(), startlanguage='es', ";
	 preguntas.first();
   	 for (int s = 1; s <= numeroPreguntas; s++){
   		 if (preguntas.getString("type").equals("M") && s<numeroPreguntas){
   			 Recordset opciones = questionsDeQuestions(preguntas.getString("qid"));
   			 int numeroOpciones = 0;
   			 while (opciones.next())
   				 numeroOpciones++;
   			String opcion = respuestas.getString(preguntas.getString("question"));
   			String cadena = "";
   			 for (int i = 0; i <= numeroOpciones - 1; i++){
   				 if (opcion.charAt(i) == 'Y')
   					 cadena = cadena + preguntas.getString("question") + "_opc" + String.valueOf(i+1) + " = '" + opcion.charAt(i) + "', ";
   				 else
   					cadena = cadena + preguntas.getString("question") + "_opc" + String.valueOf(i+1) + " = '', ";
   			 }
   			 query = query + " " + cadena;
   		 }

   	   		 if (preguntas.getString("type").equals("M") && s==numeroPreguntas){
   	   			 Recordset opciones = questionsDeQuestions(preguntas.getString("qid"));
   	   			int numeroOpciones = opciones.getRecordCount();
   	   			String opcion = respuestas.getString(preguntas.getString("question"));
   	   			String cadena = "";
   	   			for (int i = 0; i <= numeroOpciones - 1; i++){
   	   				if (opcion.charAt(i) == 'Y'){
	   	   				 if (i<numeroOpciones-1)
	   	   					 cadena = cadena + preguntas.getString("question") + "_opc" + String.valueOf(i+1) + " = '" + opcion.charAt(i) + "', ";
	   	   				 else
	   	   					 cadena = cadena + preguntas.getString("question") + "_opc" + String.valueOf(i+1) + " = '" + opcion.charAt(i) + "'";
   	   				}
   	   				else{
	   	   				 if (i<numeroOpciones-1)
	   	   					 cadena = cadena + preguntas.getString("question") + "_opc" + String.valueOf(i+1) + " = '', ";
	   	   				 else
	   	   					 cadena = cadena + preguntas.getString("question") + "_opc" + String.valueOf(i+1) + " = ''";
   	   				}
   	   			 }
   	   			 query = query + " " + cadena;
   	   		 }
   	   			    	   				 
   	   				 if (!preguntas.getString("type").equals("M")){
						 if (s < numeroPreguntas)
							 query = query + preguntas.getString("question") + "='" + respuestas.getString(preguntas.getString("question")) + "', ";
						 else
							 query = query + preguntas.getString("question") + "='" + respuestas.getString(preguntas.getString("question")) + "'";
   	   			 	 }
   	   		     preguntas.next();
   	   		 	 } 
   	 query = updateConstructor2(query, "survey_" + idEncuesta, preguntas);
   	 query = query + " where token='" + token + "'";
	 return query;
    }
    
    String updateConstructor2 (String query, String nombreTabla, Recordset preguntas) throws Throwable{
    	Recordset nombresColumnas = getNombresColumnas(nombreTabla);
    	nombresColumnas.top();
    	for (int u = 1; u <=5; u++){
    		nombresColumnas.next();
    	}
    	preguntas.first();
    	while (nombresColumnas.next()){
    		if (preguntas.getString("type").equals("M")){
    			Recordset opciones = questionsDeQuestions(preguntas.getString("qid"));
    			int numeroOpciones = opciones.getRecordCount();
    			for (int i=1; i<=numeroOpciones; i++){
    				query = StringUtil.replace(query, preguntas.getString("question") + "_opc" + String.valueOf(i), nombresColumnas.getString("column_name"));
    				nombresColumnas.next();
    			}
    		}
    		else{
    			query = StringUtil.replace(query, preguntas.getString("question"), nombresColumnas.getString("column_name"));
    		}
    		preguntas.next();
    	}
    	return query;
    }
    
    boolean searchToken (String token, String idEncuesta) throws Throwable{
    	String query = "select * from ajvieira_isurvey_lime.survey_" + idEncuesta + " where token = '" + token + "'";
    	Recordset rs = this.getDb().get(query);
        rs.top();
        while (rs.next()){
            if (rs.getString("token").equals(token)){
                return true;
            }
        }
    	return false;
    }
    
    String getIdUsuario (String userlogin) throws Throwable{
    	String query = "select user_id from ajvieira_isurvey_security.s_user where userlogin = '" + userlogin + "'";
    	Recordset rs = this.getDb().get(query);
    	rs.first();
    	return rs.getString("user_id");
    }
        
}
