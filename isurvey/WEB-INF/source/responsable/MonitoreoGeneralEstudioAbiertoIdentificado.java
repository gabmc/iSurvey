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
public class MonitoreoGeneralEstudioAbiertoIdentificado extends GenericTransaction {

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
        	Recordset p = getParticipantesRegistrados(estudio.getString("id_estudio"));
        	Recordset intParticpanteInstrumento = getIntParticipanteInstrumento(instrumentos.getString("id_instrumento"));
        	intParticpanteInstrumento.top();
        	while(intParticpanteInstrumento.next()){
		        		if (intParticpanteInstrumento.getString("estatus").equals("Incompleta")){
		        			incompletas++;
		        		}
		        		if (intParticpanteInstrumento.getString("estatus").equals("Completa")){
		        			completas++;
		        		}
		        		p.top();
		        		while (p.next()){
			        		if ((intParticpanteInstrumento.getString("estatus").equals("Sin Iniciar")) && 
			        				(intParticpanteInstrumento.getString("id_participante").equals(p.getString("id_participante")))){
			        			sinIniciar++;
			        		}
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
    
    Recordset getParticipantesRegistrados (String idEstudioAbierto) throws Throwable{
    	String sql = "select * from ajvieira_isurvey_app.participante where id_estudio_identificado = " + idEstudioAbierto;
    	return this.getDb().get(sql);
    }

}
