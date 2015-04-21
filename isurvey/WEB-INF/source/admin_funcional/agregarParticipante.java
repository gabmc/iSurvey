/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin_funcional;

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
public class agregarParticipante extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idLista = ((String[]) parametros.get("id_lista_participantes"))[0];
        String userlogin = ((String[]) parametros.get("userlogin"))[0];
        while(names.hasMoreElements()){
            String nombreCampo = (String) names.nextElement();
            if (nombreCampo.contains("cod_")){
                String nombreCampoParticipante[] = nombreCampo.split("_");
                String idParticipante = nombreCampoParticipante[1];
                if (finder(idParticipante, idLista) == false){
                    String sql = StringUtil.replace(getResource("insert.sql"), "{{id_participante}}", idParticipante);
                    sql = StringUtil.replace(sql, "{{id_lista_participantes}}", idLista);
                    sql = StringUtil.replace(sql, "{{userlogin}}", userlogin);
                    getDb().exec(sql);
////////////////////////////////////////////////////////
                	Recordset instrumentos = getInstrumentos(idLista);
                	instrumentos.top();
                	while (instrumentos.next()){
                		TokenGenerator tg = new TokenGenerator();
            	        String token = tg.generarToken(idParticipante, instrumentos.getString("id_instrumento"));
            	        if (findToken(token) == false){
            		            String sql2 = StringUtil.replace(getResource("insert-token.sql"), "{{id_participante}}", idParticipante);
            		            sql2 = StringUtil.replace(sql2, "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
            		            sql2 = StringUtil.replace(sql2, "{{token}}", token);
            		            getDb().exec(sql2);
            	        }
                    }                 
////////////////////////////////////////////////////////                    
                }
            }
        }
        getDb().commit();
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
}
