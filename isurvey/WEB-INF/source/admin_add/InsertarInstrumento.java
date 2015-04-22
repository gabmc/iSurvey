/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin_add;

import tokens_participantes.*;
import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.StringUtil;
import java.util.Map;
import java.util.Enumeration;

/**
 *
 * @author addSolutions
 */
public class InsertarInstrumento extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idEstudio = ((String[]) parametros.get("id_estudio"))[0];
        String idInstrumento = ((String[]) parametros.get("id_survey"))[0];
        String nombreInstrumento = ((String[]) parametros.get("nombre"))[0];
        Recordset participantes = getParticipantesDeEstudio(idEstudio);
        
        String sql = StringUtil.replace(getResource("insert.sql"), "{{id_survey}}", idInstrumento);
        sql = StringUtil.replace(sql, "{{id_estudio}}", idEstudio);
        sql = StringUtil.replace(sql, "{{nombre}}", nombreInstrumento);
        getDb().exec(sql); 
        
        participantes.top();
        while (participantes.next()){
////////////////////////////////////////////////////////

                		TokenGenerator tg = new TokenGenerator();
            	        String token = tg.generarToken(participantes.getString("id_participante"), idInstrumento);
            	        if (findToken(token) == false){
            		            String sql2 = StringUtil.replace(getResource("insert-token.sql"), "{{id_participante}}", participantes.getString("id_participante"));
            		            sql2 = StringUtil.replace(sql2, "{{id_instrumento}}", idInstrumento);
            		            sql2 = StringUtil.replace(sql2, "{{token}}", token);
            		            getDb().exec(sql2);
            	        }
                                   
////////////////////////////////////////////////////////                    
        }   
        getDb().commit();
        return 0;
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
    
    String getEmpresa() throws Throwable{
        String query = "select * from ajvieira_isurvey_security.s_user where userlogin = '"+this.getUserName()+"'";
        Recordset rs;
			rs = this.getDb().get(query);
	        rs.first();
	        return rs.getString("id_empresa");
    }
    
    Recordset getInstrumentos (String idLista) throws Throwable{
    	String query = "SELECT instrumento . * " +
    			"FROM ajvieira_isurvey_app.instrumento, ajvieira_isurvey_app.estudio, " +
    			"ajvieira_isurvey_app.int_lista_participantes_estudio, ajvieira_isurvey_app.lista_participantes " +
    			"WHERE lista_participantes.id_lista_participantes = " + idLista + " " +
    			"AND lista_participantes.id_lista_participantes = int_lista_participantes_estudio.id_lista_participantes " +
    			"AND int_lista_participantes_estudio.id_estudio = estudio.id_estudio " +
    			"AND estudio.id_estudio = instrumento.id_estudio";
    	Recordset instrumentos = this.getDb().get(query);
    	return instrumentos;
    }
    
    Recordset getParticipantesDeEstudio (String idEstudio) throws Throwable{
    	String query = "select participante.* " +
    			"from ajvieira_isurvey_app.participante, " +
    			"ajvieira_isurvey_app.int_participante_lista_participantes, " +
    			"ajvieira_isurvey_app.lista_participantes, " +
    			"ajvieira_isurvey_app.int_lista_participantes_estudio, " +
    			"ajvieira_isurvey_app.estudio " +
    			"where " +
    			"participante.id_participante = int_participante_lista_participantes.id_participante " +
    			"and participante.id_empresa = int_participante_lista_participantes.id_empresa_participante " +
    			"and int_participante_lista_participantes.id_lista_participantes = lista_participantes.id_lista_participantes " +
    			"and lista_participantes.id_empresa = participante.id_empresa " +
    			"and lista_participantes.id_lista_participantes = int_lista_participantes_estudio.id_lista_participantes " +
    			"and int_lista_participantes_estudio.id_estudio = estudio.id_estudio " +
    			"and estudio.id_empresa = participante.id_empresa " +
    			"and estudio.id_estudio = 1";
    	Recordset participantes = this.getDb().get(query);
    	return participantes;
    }
}
