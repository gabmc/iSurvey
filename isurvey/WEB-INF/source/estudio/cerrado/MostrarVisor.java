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
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String token = ((String[]) parametros.get("token"))[0];
        Recordset participante = getParticipante(token);
        Recordset listas = getListas(token);
        participante.top();
        listas.top();
        Recordset visor = new Recordset();
        visor.append("estudio", java.sql.Types.VARCHAR);
        visor.append("instrumento", java.sql.Types.VARCHAR);
        visor.append("id_instrumento", java.sql.Types.VARCHAR);
        visor.append("token", java.sql.Types.VARCHAR);
        while (listas.next()){
        	Recordset info = getInfoVisor(listas.getString("id_lista_participantes"), token);
        	info.top();
        	while (info.next()){
        		visor.addNew();
	        	visor.setValue("estudio", info.getString("estudio"));
	        	visor.setValue("instrumento", info.getString("instrumento"));
	        	visor.setValue("id_instrumento", info.getString("id_instrumento"));
	        	visor.setValue("token", info.getString("token"));
        	}
        }
        this.publish("visor", visor);
       // this.getSession().setAttribute("visor", visor);

        return 0;
    }
 
  
    Recordset getEmpresa (String token) throws Throwable{
    	String query = "select empresa.* " +
    			"from ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.instrumento, " +
    			"ajvieira_isurvey_app.int_participante_instrumento " +
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
    
    Recordset getListas (String token) throws Throwable{
    	String query = "select lista_participantes.* " +
    			"from ajvieira_isurvey_app.lista_participantes, " +
    			"ajvieira_isurvey_app.empresa, " +
    			"ajvieira_isurvey_app.int_lista_participantes_estudio, " +
    			"ajvieira_isurvey_app.estudio, " +
    			"ajvieira_isurvey_app.int_participante_instrumento, " +
    			"ajvieira_isurvey_app.participante, " +
    			"ajvieira_isurvey_app.instrumento " +
    			"where " +
    			"participante.id_participante = int_participante_instrumento.id_participante " +
    			"and instrumento.id_instrumento = int_participante_instrumento.id_instrumento " +
    			"and instrumento.id_estudio = estudio.id_estudio " +
    			"and estudio.id_empresa = empresa.id_empresa " +
    			"and int_lista_participantes_estudio.id_lista_participantes = lista_participantes.id_lista_participantes " +
    			"and int_lista_participantes_estudio.id_estudio = estudio.id_estudio " +
    			"and lista_participantes.id_empresa = empresa.id_empresa " +
    			"and participante.id_empresa = empresa.id_empresa " +
    			"and int_participante_instrumento.token_participante = '" + token + "'";
    	Recordset listas = this.getDb().get(query);
    	return listas;
    }
    
    Recordset getInfoVisor (String idLista, String token) throws Throwable{
    	String query = "select estudio.nombre_estudio as estudio, instrumento.nombre as instrumento, instrumento.id_instrumento, '" + token + "' as token " +
    			"from ajvieira_isurvey_app.estudio, " +
    			"ajvieira_isurvey_app.instrumento, " +
    			"ajvieira_isurvey_app.int_lista_participantes_estudio, " +
    			"ajvieira_isurvey_app.lista_participantes " +
    			"where " +
    			"int_lista_participantes_estudio.id_lista_participantes = lista_participantes.id_lista_participantes " +
    			"and int_lista_participantes_estudio.id_estudio = estudio.id_estudio " +
    			"and instrumento.id_estudio = estudio.id_estudio " +
    			"and lista_participantes.id_lista_participantes = " + idLista + " " +
    			"order by estudio";
    	Recordset info = this.getDb().get(query);
    	return info;
    }
}
