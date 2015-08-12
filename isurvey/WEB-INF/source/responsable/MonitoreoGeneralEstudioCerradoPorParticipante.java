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
public class MonitoreoGeneralEstudioCerradoPorParticipante extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idEstudio = ((String[]) parametros.get("id"))[0];
        
        int numeroInstrumentos = 0;
        int sinIniciar = 0;
        int incompletas = 0;
        int completas = 0;
        Recordset instrumentos = getInstrumentos(idEstudio);
        numeroInstrumentos = instrumentos.getRecordCount();
        
        Recordset participantes = getParticipantes(idEstudio);
        participantes.top();
        while (participantes.next()){
        	String completados = getInstrumentosCompletados(idEstudio, participantes.getString("id_participante"));
        	if (Integer.valueOf(completados) == numeroInstrumentos)
        		completas++;
        	if (Integer.valueOf(completados) == 0)
        		sinIniciar++;
        	if (Integer.valueOf(completados) > 0 && Integer.valueOf(completados) < numeroInstrumentos)
        		incompletas++;
        }
        
        completas = completas/getNumeroInstrumentos(idEstudio);
        sinIniciar = sinIniciar/getNumeroInstrumentos(idEstudio);
        incompletas = incompletas/getNumeroInstrumentos(idEstudio);       
        
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
    
    int getNumeroInstrumentos (String idEstudio) throws Throwable{
    	String sql = "select count(id_instrumento) as count from ajvieira_isurvey_app.instrumento where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return Integer.valueOf(rs.getString("count"));
    }
    
    String getInstrumentosCompletados (String idEstudio, String idParticipante) throws Throwable{
    	String query = "select count(estatus) as countm from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where id_participante = "+idParticipante+" " +
    					"and id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = " +idEstudio+ " " +
    			"and estatus = 'Completa')";
    	Recordset rs2 = this.getDb().get(query);
    	rs2.first();    	
    	return rs2.getString("countm");
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
    	String sql = "select token_participante, id_participante from ajvieira_isurvey_app.int_participante_instrumento where id_instrumento = " + idInstrumento;
    	return this.getDb().get(sql);
    }
    
    Recordset getParticipantes (String idEstudio) throws Throwable{
    	String sql = "select id_participante from ajvieira_isurvey_app.int_participante_instrumento where id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = " + idEstudio + ")";
    	return this.getDb().get(sql);
    }
    
}
