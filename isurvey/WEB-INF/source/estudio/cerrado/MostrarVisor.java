/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package estudio.cerrado;

import tokens_participantes.*;
import dinamica.GenericTableManager;
import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.StringUtil;
import java.util.Map;
import java.util.Enumeration;

/**
 *
 * @author addSolutions
 */
public class MostrarVisor extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        this.getDb().beginTrans();
    	String red = "<img src=\"${def:context}/images/reddot.png\" " +
		"title=\"Sin Iniciar\" class=\"tool2\">";
    	String green = "<img src=\"${def:context}/images/greendot.png\" " +
		"title=\"Completa\" class=\"tool\">";
    	String yellow = "<img src=\"${def:context}/images/yellowdot.png\" " +
		"title=\"Incompleta\" class=\"tool\">";
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String token = ((String[]) parametros.get("token"))[0];
        Recordset empresa = getEmpresa(token);
        empresa.top();
        String logoEmpresa = "";
        while (empresa.next()){
        	logoEmpresa = empresa.getString("logo");
        }
        Recordset visor = new Recordset();
        Recordset logo = new Recordset();
        visor.append("estudio", java.sql.Types.VARCHAR);
        visor.append("instrumento", java.sql.Types.VARCHAR);
        visor.append("id_instrumento", java.sql.Types.VARCHAR);
        visor.append("token", java.sql.Types.VARCHAR);
        visor.append("estatus", java.sql.Types.VARCHAR);
        logo.append("logo", java.sql.Types.VARCHAR);
        logo.addNew();
        logo.setValue("logo", logoEmpresa);
        updateStatus(token);
        
        	Recordset info = getInfoVisor(token);
        	info.top();
        	while (info.next()){
        		visor.addNew();
	        	visor.setValue("estudio", info.getString("estudio"));
	        	visor.setValue("instrumento", info.getString("instrumento"));
	        	visor.setValue("id_instrumento", info.getString("id_instrumento"));
	        	visor.setValue("token", info.getString("token"));
	        	if (info.getValue("estatus").equals("Incompleta")){
	        		visor.setValue("estatus", yellow);
	        	}
	        	if (info.getValue("estatus").equals("Sin Iniciar")){
	        		visor.setValue("estatus", red);
	        	}
	        	if (info.getValue("estatus").equals("Completa")){
	        		visor.setValue("estatus", green);
	        	}
        	}
        this.publish("logo", logo);
        this.getSession().setAttribute("visor", visor);
        if (visor.getRecordCount() > 0)
	        return 0;
        else
        	return 1;
    }
 
  
    Recordset getEmpresa (String token) throws Throwable{
    	String query = "select empresa.* " +
    			"from ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.instrumento, " +
    			"ajvieira_isurvey_app.int_participante_instrumento, " +
    			"ajvieira_isurvey_app.empresa " +
    			"where estudio.id_estudio = instrumento.id_estudio " +
    			"and instrumento.id_instrumento = int_participante_instrumento.id_instrumento " +
    			"and estudio.id_empresa = empresa.id_empresa " +
    			"and int_participante_instrumento.token_participante = '" + token + "'";
    	Recordset empresa = this.getDb().get(query);
    	return empresa;
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
    
    Recordset getInfoVisor (String token) throws Throwable{
    	String query = "select estudio.nombre_estudio as estudio, instrumento.nombre as instrumento, " +
    			"instrumento.id_instrumento, int_participante_instrumento.token_participante as token, int_participante_instrumento.estatus " +
    			"from " +
    			"ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.instrumento, " +
    			"ajvieira_isurvey_app.int_participante_instrumento " +
    			"where " +
    			"estudio.id_estudio = instrumento.id_estudio " +
    			"and int_participante_instrumento.id_instrumento = instrumento.id_instrumento " +
    			"and estudio.tipo = 'Cerrado' " +
    			"and estudio.estatus = 'En Marcha' " +
    			"and int_participante_instrumento.id_participante = (select id_participante from  " +
    			"ajvieira_isurvey_app.int_participante_instrumento " +
    			"where token_participante = '"+token+"') " +
    			"order by estudio, instrumento";
    	Recordset info = this.getDb().get(query);
    	return info;
    }
    
    Recordset getNombresColumnas (String nombreTabla) throws Throwable{
    	String sql = "SELECT `COLUMN_NAME` " +
    			"FROM `INFORMATION_SCHEMA`.`COLUMNS` " +
    			"WHERE `TABLE_SCHEMA`='ajvieira_isurvey_lime' " +
    			"AND `TABLE_NAME`='"+nombreTabla+"';";
    	return this.getDb().get(sql);
    }
    
    void setEstatus (String token, String estatus, String porcentaje) throws Throwable{
    	String sql = "update ajvieira_isurvey_app.int_participante_instrumento set estatus = '" + estatus + "', " +
    			" porcentaje_completado = " + porcentaje + " where token_participante = '" + token + "'";
    	this.getDb().exec(sql);
    }
    
    Recordset questionsOrdenadas (String idEncuesta) throws Throwable{
    	String sql = "select * from ajvieira_isurvey_lime.questions where parent_qid = 0 and sid = " + idEncuesta;
    	Recordset questions = this.getDb().get(sql);
    	questions.top();
    	questions.sort("question_order");
    	return questions;
    }
    
    public void updateStatus (String token) throws Throwable{
    	String estatus = "";
    	float porcentaje = 1;
    	int preguntasObligatorias = 0;
    	int preguntasRespondidas = 0;
    	Recordset instrumentos = getInstrumentosParaCiclar(token);
    	instrumentos.top();
    	Recordset participante = getParticipante(token);
    	participante.top();
    	while (participante.next()){
	    	while (instrumentos.next()){
	    		TokenGenerator tg = new TokenGenerator();
	    		Recordset respuestas = getRespuestas(instrumentos.getString("id_instrumento"), 
	    				tg.generarToken(participante.getString("id_participante"), instrumentos.getString("id_instrumento")));
	    		respuestas.top();
	    		Recordset columnas = getNombresColumnas("survey_" + instrumentos.getString("id_instrumento"));
	    		columnas.top();
	    		int numeroColumnas = columnas.getRecordCount() - 5; //respuestas del participante (todas las posibles entre checks, matrices, etc)
	    		Recordset preguntas = questionsOrdenadas(instrumentos.getString("id_instrumento"));
	    		preguntas.top();
	    		while (preguntas.next()){
	    			if (preguntas.getString("mandatory").equals("Y")){
	    				if ((preguntas.getString("type").equals("F")) || (preguntas.getString("type").equals("E"))
	    			    		|| (preguntas.getString("type").equals("B")) || (preguntas.getString("type").equals("A"))
	    			    		|| (preguntas.getString("type").equals(":")) || (preguntas.getString("type").equals("C"))
	    			    		|| (preguntas.getString("type").equals(";")) || (preguntas.getString("type").equals("1"))
	    			    		|| (preguntas.getString("type").equals("H"))){
	    					preguntasObligatorias = preguntasObligatorias + getNumeroAnswers(preguntas.getString("qid"));
	    				}
	    				else
	    					preguntasObligatorias++;
	    			}
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
			    			int numeroColumnas2 = numeroColumnas; //variable que sera modificada para sacar % de completado luego
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
				    		else{ 
				    			estatus = "Incompleta";
				    			porcentaje = 1;
				    		}
		    		}
	    		}
	    		if (porcentaje != 0 && porcentaje != 100 && preguntasObligatorias != 0)
	    			porcentaje = (preguntasRespondidas*100)/preguntasObligatorias;

	    		if (preguntasObligatorias == 0)
	    			porcentaje = 100;
	    		setEstatus(tg.generarToken(participante.getString("id_participante"), instrumentos.getString("id_instrumento")), estatus, String.valueOf(porcentaje));
	    	}
    	}
    }
    
    int getNumeroAnswers (String qid) throws Throwable{
    	String sql = "select * from ajvieira_isurvey_lime.answers " +
    			"where qid = " + qid;
    	Recordset rs = this.getDb().get(sql);
    	return rs.getRecordCount();
    }
    
    Recordset getRespuestas (String idInstrumento, String token) throws Throwable{
    	String sql = "select * from ajvieira_isurvey_lime.survey_" + idInstrumento + " " +
		"where token = '" + token + "'";
    	Recordset rs = this.getDb().get(sql);
    	return rs;
    }
    
    Recordset getInstrumentosParaCiclar (String token) throws Throwable{
    	String sql = "select instrumento.id_instrumento " +
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
    	return this.getDb().get(sql);
    }
}
