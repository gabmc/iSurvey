/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package estudio.abierto.identificado;


import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.StringUtil;
import java.util.Map;
import java.util.Enumeration;

import javax.sound.midi.ControllerEventListener;

/**
 *
 * @author addSolutions
 */
public class LogIn extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idParticipante = ((String[]) parametros.get("id_participante"))[0];
        String idEmpresa = ((String[]) parametros.get("id_empresa"))[0];
        String idInstrumento = ((String[]) parametros.get("id_instrumento"))[0];
        Recordset estudio = getEstudioDeInstrumento(idInstrumento);
        estudio.top();
        String idEstudio = "";
        while (estudio.next()){
        	idEstudio = estudio.getString("id_estudio");
        }
        Recordset instrumentos = getInstrumentosDeEstudio(idEstudio);
        instrumentos.top();
        int numeroDeInstrumentos = contadorDeInstrumentos(instrumentos);
        
        if (numeroDeInstrumentos > 1){
        }else{
        	Recordset record = new Recordset();
        	record.append("id_instrumento", java.sql.Types.VARCHAR);
        	record.append("token", java.sql.Types.VARCHAR);
        	record.addNew();
        	record.setValue("id_instrumento", idInstrumento);
        	record.setValue("token", getToken(idInstrumento, idParticipante));
        	this.publish("id_instrumento", record);
        }
        return 0;
    }
    
    String getToken (String idInstrumento, String idParticipante) throws Throwable{
    	String sql = "select * from ajvieira_isurvey_app.int_participante_instrumento where " +
    			"id_participante = " + idParticipante + " " +
    			"and id_instrumento = " + idInstrumento;
    	Recordset interseccion = this.getDb().get(sql);
    	interseccion.top();
    	String token = "";
    	while (interseccion.next()){
    		token = interseccion.getString("token_participante");
    	}
    	return token;
    }
    
    Recordset getEstudioDeInstrumento (String idInstrumento) throws Throwable{
    	String sql = " select estudio.* " +
    			"from ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.instrumento " +
    			"where estudio.id_estudio = instrumento.id_estudio " +
    			"and instrumento.id_instrumento = " + idInstrumento;
    	return this.getDb().get(sql);
    }
    
    Recordset getInstrumentosDeEstudio(String idEstudio) throws Throwable{
    	String sql = "select instrumento.* " +
    			"from ajvieira_isurvey_app.estudio, " +
    			"ajvieira_isurvey_app.instrumento " +
    			"where " +
    			"instrumento.id_estudio = estudio.id_estudio " +
    			"and estudio.id_estudio = " + idEstudio;
    	return this.getDb().get(sql);
    }
    
    int contadorDeInstrumentos (Recordset instrumentos){
    	int cont = 0;
    	while (instrumentos.next()){
    		cont++;
    	}
    	return cont;
    }
}
