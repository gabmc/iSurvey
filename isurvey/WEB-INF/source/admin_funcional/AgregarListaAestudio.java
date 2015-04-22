/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin_funcional;

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
public class AgregarListaAestudio extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idEstudio = ((String[]) parametros.get("id_estudio"))[0];
        String idLista = ((String[]) parametros.get("id_lista_participantes"))[0];
        
        Recordset participantes = getParticipantes(idLista);
        participantes.top();
        while (participantes.next()){ 
////////////////////////////////////////////////////////
                	Recordset instrumentos = getInstrumentos(idEstudio);
                	instrumentos.top();
                	while (instrumentos.next()){
                		TokenGenerator tg = new TokenGenerator();
            	        String token = tg.generarToken(participantes.getString("id_participante"), instrumentos.getString("id_instrumento"));
            	        if (findToken(token) == false){
            		            String sql2 = StringUtil.replace(getResource("insert-token.sql"), "{{id_participante}}", participantes.getString("id_participante"));
            		            sql2 = StringUtil.replace(sql2, "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
            		            sql2 = StringUtil.replace(sql2, "{{token}}", token);
            		            getDb().exec(sql2);
            		            String sql3 = StringUtil.replace(getResource("insert-lime.sql"), "{{id_encuesta}}", instrumentos.getString("id_instrumento"));
            		            sql3 = StringUtil.replace(sql3, "{{firstname}}", participantes.getString("nombre_participante"));
            		            sql3 = StringUtil.replace(sql3, "{{lastname}}", participantes.getString("apellido_participante"));
            		            sql3 = StringUtil.replace(sql3, "{{email}}", participantes.getString("email_participante"));
            		            sql3 = StringUtil.replace(sql3, "{{token}}", token);
            		            getDb().exec(sql3);
            	        }
                    }                 
////////////////////////////////////////////////////////                    
        }        
        
        String sql = StringUtil.replace(getResource("insert.sql"), "{{id_lista_participantes}}", idLista);
        sql = StringUtil.replace(sql, "{{id_estudio}}", idEstudio);
        getDb().exec(sql);
        //getDb().commit();
        return 0;
    }

    Boolean finder (String idParticipante, String idLista) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.int_participante_lista_participantes";
    	Recordset rs = this.getDb().get(query);
    	rs.top();
    	while (rs.next()){
    		if ((rs.getString("id_participante").equals(idParticipante)) && (rs.getString("id_lista_participantes").equals(idLista))){
    			return true;
    		}
    	}
    	return false;
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
    
    Recordset getInstrumentos (String idEstudio) throws Throwable{
    	String query = "select instrumento.* " +
    			"from ajvieira_isurvey_app.instrumento, ajvieira_isurvey_app.estudio " +
    			"where instrumento.id_estudio = estudio.id_estudio " +
    			"and estudio.id_estudio = " + idEstudio;
    	Recordset instrumentos = this.getDb().get(query);
    	return instrumentos;
    }
    
    Recordset getParticipantes (String idLista) throws Throwable{
    	String query = "select participante.* from ajvieira_isurvey_app.participante, " +
    			"ajvieira_isurvey_app.int_participante_lista_participantes, " +
    			"ajvieira_isurvey_app.lista_participantes " +
    			"where " +
    			"lista_participantes.id_lista_participantes = int_participante_lista_participantes.id_lista_participantes " +
    			"and int_participante_lista_participantes.id_participante = participante.id_participante " +
    			"and lista_participantes.id_lista_participantes = " + idLista;
    	Recordset instrumentos = this.getDb().get(query);
    	return instrumentos;
    }
}
