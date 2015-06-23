/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package responsable;


import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.StringUtil;
import java.util.Map;
import java.util.Enumeration;

import tokens_participantes.TokenGenerator;

/**
 *
 * @author addSolutions
 */
public class MonitoreoGeneralEstudio extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String id = ((String[]) parametros.get("id"))[0];
        
        Recordset estudio = getEstudio(id);
        estudio.first();
        
        int sinIniciar = 0;
        int incompletas = 0;
        int completas = 0;
        Recordset instrumentos = getInstrumentos(id);
        instrumentos.top();
        while(instrumentos.next()){
        	Recordset tokens = getTokens(instrumentos.getString("id_instrumento"));
        	tokens.top();
        	while(tokens.next()){
        		updateStatus(tokens.getString("token_participante"));
        	}
        }
        instrumentos.top();
        while(instrumentos.next()){
        	Recordset intParticpanteInstrumento = getIntParticipanteInstrumento(instrumentos.getString("id_instrumento"));
        	intParticpanteInstrumento.top();
        	while(intParticpanteInstrumento.next()){
        		if (intParticpanteInstrumento.getString("estatus").equals("Incompleta")){
        			incompletas++;
        		}
        		if (intParticpanteInstrumento.getString("estatus").equals("Completa")){
        			completas++;
        		}
        		if (intParticpanteInstrumento.getString("estatus").equals("Sin Iniciar")){
        			sinIniciar++;
        		}
        	}    	
        }
        Recordset output = new Recordset();
        output.append("sin_iniciar", java.sql.Types.INTEGER);
        output.append("incompletas", java.sql.Types.INTEGER);
        output.append("completas", java.sql.Types.INTEGER);
        output.append("nombre_estudio", java.sql.Types.VARCHAR);
        output.append("total", java.sql.Types.INTEGER);
        output.addNew();
        output.setValue("sin_iniciar", sinIniciar);
        output.setValue("incompletas", incompletas);
        output.setValue("completas", completas);
        output.setValue("nombre_estudio", estudio.getString("nombre_estudio"));
        output.setValue("total", completas + incompletas + sinIniciar);
        publish("output", output);
        //getDb().commit();
        return 0;
    }
    

    Recordset getInstrumentos(String idEstudio) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.instrumento where id_estudio = " + idEstudio;
    	return this.getDb().get(query);
    }
    
    Recordset getIntParticipanteInstrumento(String idInstrumento) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.int_participante_instrumento where id_instrumento = " + idInstrumento;
    	return this.getDb().get(query);
    }
    
    Recordset getEstudio(String idEstudio) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.estudio where id_estudio = " + idEstudio;
    	return this.getDb().get(query);
    }
    
    Recordset getTokens(String idInstrumento) throws Throwable{
    	String sql = "select token_participante from ajvieira_isurvey_app.int_participante_instrumento where id_instrumento = " + idInstrumento;
    	return this.getDb().get(sql);
    }
    
    void updateStatus (String token) throws Throwable{
    	String estatus = "";
    	String query = "select instrumento.id_instrumento " +
		"from " +
		"ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.instrumento, " +
		"ajvieira_isurvey_app.int_participante_instrumento " +
		"where " +
		"estudio.id_estudio = instrumento.id_estudio " +
		"and estudio.tipo = 'Cerrado' " +
		"and int_participante_instrumento.id_instrumento = instrumento.id_instrumento " +
		"and int_participante_instrumento.id_participante = (select id_participante from  " +
		"ajvieira_isurvey_app.int_participante_instrumento " +
		"where token_participante = '"+token+"') ";
    	Recordset instrumentos = this.getDb().get(query);
    	instrumentos.top();
    	Recordset participante = getParticipante(token);
    	participante.top();
    	while (participante.next()){
	    	while (instrumentos.next()){
	    		TokenGenerator tg = new TokenGenerator();
	    		String sql = "select * from ajvieira_isurvey_lime.survey_" + instrumentos.getString("id_instrumento") + " " +
	    				"where token = '" + tg.generarToken(participante.getString("id_participante"), instrumentos.getString("id_instrumento")) + "'";
	    		Recordset respuestas = this.getDb().get(sql);
	    		respuestas.top();
	    		Recordset columnas = getNombresColumnas("survey_" + instrumentos.getString("id_instrumento"));
	    		columnas.top();
	    		int numeroColumnas = columnas.getRecordCount() - 5;
	    		Recordset preguntas = questionsOrdenadas(instrumentos.getString("id_instrumento"));

	    		while (respuestas.next()){
	    			preguntas.first();
		    		for (int i = 1; i <= 5; i++){
		    			columnas.next();
		    		}
	    			int numeroColumnas2 = numeroColumnas;
		    		while (columnas.next()){
		    			String column = columnas.getString("column_name");
		    			column = column.toLowerCase();
		    			if (respuestas.getString(column) == null && (preguntas.getString("mandatory").equals("Y")) && (!column.equals("submitdate") || !column.equals("lastpage"))){
		    				numeroColumnas2--;
		    			}
		    			preguntas.next();
		    		}
		    		if (numeroColumnas2 <= 0){
		    			estatus = "Sin Iniciar";
		    		}
		    		if (numeroColumnas2 > 0 && numeroColumnas2 < numeroColumnas){
		    			estatus = "Incompleta";
		    		}
		    		if (numeroColumnas2 == numeroColumnas){
		    			estatus = "Completa";
		    		}
	    		}
	    		setEstatus(token, estatus);
	    	}
    	}
    }
    
    Recordset getNombresColumnas (String nombreTabla) throws Throwable{
    	String sql = "SELECT `COLUMN_NAME` " +
    			"FROM `INFORMATION_SCHEMA`.`COLUMNS` " +
    			"WHERE `TABLE_SCHEMA`='ajvieira_isurvey_lime' " +
    			"AND `TABLE_NAME`='"+nombreTabla+"';";
    	return this.getDb().get(sql);
    }
    
    void setEstatus (String token, String estatus) throws Throwable{
    	String sql = "update ajvieira_isurvey_app.int_participante_instrumento set estatus = '" + estatus + "' " +
    			"where token_participante = '" + token + "'";
    	this.getDb().exec(sql);
    }
    
    Recordset getParticipante (String token) throws Throwable{
    	String query = "select participante.id_participante, participante.nombre_participante, " +
    			"participante.apellido_participante, participante.email_participante, participante.empresa, " +
    			"participante.cargo, participante.supervisor, participante.fecha_nacimiento, " +
    			"participante.fecha_ingreso, participante.sexo, participante.tipo_nomina, participante.funcion " +
    			"from ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_instrumento, " +
    			"ajvieira_isurvey_app.instrumento, ajvieira_isurvey_app.estudio, " +
    			"ajvieira_isurvey_app.empresa " +
    			"where participante.id_participante = int_participante_instrumento.id_participante " +
    			"and int_participante_instrumento.id_instrumento = instrumento.id_instrumento " +
    			"and instrumento.id_estudio = estudio.id_estudio " +
    			"and estudio.id_empresa = empresa.id_empresa " +
    			"and empresa.id_empresa = participante.id_empresa " +
    			"and int_participante_instrumento.token_participante = '" + token + "'";
    	Recordset participante = this.getDb().get(query);
    	return participante;
    }
    
    Recordset questionsOrdenadas (String idEncuesta) throws Throwable{
    	String sql = "select * from ajvieira_isurvey_lime.questions where parent_qid = 0 and sid = " + idEncuesta;
    	Recordset questions = this.getDb().get(sql);
    	questions.top();
    	questions.sort("question_order");
    	return questions;
    }
}
