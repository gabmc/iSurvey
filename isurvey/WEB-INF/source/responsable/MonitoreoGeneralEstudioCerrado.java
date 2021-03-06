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
public class MonitoreoGeneralEstudioCerrado extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String id = ((String[]) parametros.get("id"))[0];
        
        
        int sinIniciar = 0;
        int incompletas = 0;
        int completas = 0;
        Recordset instrumentos = getInstrumento(id);
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
        output.append("total", java.sql.Types.INTEGER);
        output.addNew();
        output.setValue("sin_iniciar", sinIniciar);
        output.setValue("incompletas", incompletas);
        output.setValue("completas", completas);
        output.setValue("total", completas + incompletas + sinIniciar);
        publish("output", output);
        return 0;
    }
    

    Recordset getInstrumento(String idInstrumento) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.instrumento where id_instrumento = " + idInstrumento;
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
    
}
