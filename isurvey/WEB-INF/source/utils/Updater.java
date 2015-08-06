/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import tokens_participantes.TokenGenerator;
import dinamica.GenericTransaction;
import dinamica.Recordset;

/**
 *
 * @author Gary
 */
public class Updater extends GenericTransaction{

    public void updateStatus (String token) throws Throwable{
    	String estatus = "";
    	float porcentaje = 1;
    	int preguntasObligatorias = 0;
    	int preguntasRespondidas = 0;
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
	    		preguntas.top();
	    		while (preguntas.next()){
	    			if (preguntas.getString("mandatory").equals("Y"))
	    				preguntasObligatorias++;
	    		}

	    		while (respuestas.next()){
		    		if (respuestas.getString("submitdate") != null){
		    			estatus = "Completa";
		    			porcentaje = 100;
		    		}
		    		else{
		    			preguntas.first();
			    		for (int i = 1; i <= 5; i++){
			    			columnas.next();
			    		}
		    			int numeroColumnas2 = numeroColumnas;
			    		while (columnas.next()){
			    			String column = columnas.getString("column_name");
			    			column = column.toLowerCase();
			    			if (respuestas.getString(column) == null && (preguntas.getString("mandatory").equals("Y")) && (!column.equals("submitdate") && !column.equals("lastpage"))){
			    				estatus = "Incompleta";
			    			}
			    			if (respuestas.getString(column) != null && respuestas.getString(column) != "" && (preguntas.getString("mandatory").equals("Y")) && (!column.equals("submitdate") && !column.equals("lastpage"))){
			    				preguntasRespondidas++;
			    			}
			    			if (respuestas.getString(column) == null)
			    				numeroColumnas2--;
			    			preguntas.next();
			    		}
			    		if (numeroColumnas2 <= 0){
			    			estatus = "Sin Iniciar";
			    			porcentaje = 0;
			    		}
			    		else 
			    			estatus = "Incompleta";
		    		}
	    		}
	    		if (porcentaje != 0 && porcentaje != 100 && preguntasObligatorias != 0)
	    			porcentaje = (preguntasRespondidas*100)/preguntasObligatorias;
	    		if (preguntasObligatorias == 0)
	    			porcentaje = 100;
	    		if (porcentaje == 100)
	    			estatus = "Completa";
	    		if (porcentaje == 0)
	    			estatus = "Sin Iniciar";
	    		setEstatus(tg.generarToken(participante.getString("id_participante"), instrumentos.getString("id_instrumento")), estatus, String.valueOf(porcentaje));
	    	}
    	}
    }
	    	
	    	
	        void setEstatus (String token, String estatus, String porcentaje) throws Throwable{
	        	String sql = "update ajvieira_isurvey_app.int_participante_instrumento set estatus = '" + estatus + "', " +
	        			" porcentaje_completado = " + porcentaje + " where token_participante = '" + token + "'";
	        	this.getDb().exec(sql);
	        }
	        
	        Recordset getNombresColumnas (String nombreTabla) throws Throwable{
	        	String sql = "SELECT `COLUMN_NAME` " +
	        			"FROM `INFORMATION_SCHEMA`.`COLUMNS` " +
	        			"WHERE `TABLE_SCHEMA`='ajvieira_isurvey_lime' " +
	        			"AND `TABLE_NAME`='"+nombreTabla+"';";
	        	return this.getDb().get(sql);
	        }
	        
	        Recordset questionsOrdenadas (String idEncuesta) throws Throwable{
	        	String sql = "select * from ajvieira_isurvey_lime.questions where parent_qid = 0 and sid = " + idEncuesta;
	        	Recordset questions = this.getDb().get(sql);
	        	questions.top();
	        	questions.sort("question_order");
	        	return questions;
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
}

