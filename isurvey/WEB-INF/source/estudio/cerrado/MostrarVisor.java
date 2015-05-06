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
		"title=\"Incompleta\" class=\"tool\" width=\"24\" height=\"24\">";
    	String green = "<img src=\"${def:context}/images/greendot.png\" " +
		"title=\"Completa\" class=\"tool\" width=\"24\" height=\"24\">";
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
        visor.append("estudio", java.sql.Types.VARCHAR);
        visor.append("instrumento", java.sql.Types.VARCHAR);
        visor.append("id_instrumento", java.sql.Types.VARCHAR);
        visor.append("token", java.sql.Types.VARCHAR);
        visor.append("estatus", java.sql.Types.VARCHAR);
        visor.append("logo", java.sql.Types.VARCHAR);
        
        updateStatus(token);
        
        	Recordset info = getInfoVisor(token);
        	info.top();
        	while (info.next()){
        		visor.addNew();
	        	visor.setValue("estudio", info.getString("estudio"));
	        	visor.setValue("instrumento", info.getString("instrumento"));
	        	visor.setValue("id_instrumento", info.getString("id_instrumento"));
	        	visor.setValue("token", info.getString("token"));
	        	visor.setValue("logo", logoEmpresa);
	        	if (info.getValue("estatus").equals("Incompleta")){
	        		visor.setValue("estatus", red);
	        	}
	        	else{
	        		visor.setValue("estatus", green);
	        	}
        	}
        this.publish("visor", visor);
       // this.getSession().setAttribute("visor", visor);

        return 0;
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
    	String query = "select participante.* " +
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
    
    void setEstatus (String token, String estatus) throws Throwable{
    	String sql = "update ajvieira_isurvey_app.int_participante_instrumento set estatus = '" + estatus + "' " +
    			"where token_participante = '" + token + "'";
    	this.getDb().exec(sql);
    }
    
    void updateStatus (String token) throws Throwable{
    	String estatus = "Completa";
    	String query = "select instrumento.id_instrumento " +
		"from " +
		"ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.instrumento, " +
		"ajvieira_isurvey_app.int_participante_instrumento " +
		"where " +
		"estudio.id_estudio = instrumento.id_estudio " +
		"and int_participante_instrumento.id_instrumento = instrumento.id_instrumento " +
		"and int_participante_instrumento.id_participante = (select id_participante from  " +
		"ajvieira_isurvey_app.int_participante_instrumento " +
		"where token_participante = '"+token+"') ";
    	Recordset instrumentos = this.getDb().get(query);
    	instrumentos.top();
    	Recordset participante = getParticipante(token);
    	participante.top();
    	while (instrumentos.next()){
    		TokenGenerator tg = new TokenGenerator();
    		String sql = "select * from ajvieira_isurvey_lime.survey_" + instrumentos.getString("id_instrumento") + " " +
    				"where token = '" + tg.generarToken(/*participante.getString("id_participante")*/"1", instrumentos.getString("id_instrumento")) + "'";
    		Recordset respuestas = this.getDb().get(sql);
    		respuestas.top();
    		Recordset columnas = getNombresColumnas("survey_" + instrumentos.getString("id_instrumento"));
    		columnas.top();
    		while (respuestas.next()){
	    		while (columnas.next()){
	    			String column = columnas.getString("column_name");
	    			column = column.toLowerCase();
	    			if (respuestas.getString(column) == null){
	    				estatus = "Incompleta";
	    			}
	    		}
    		}
    		setEstatus(token, estatus);
    	}
    }
}
