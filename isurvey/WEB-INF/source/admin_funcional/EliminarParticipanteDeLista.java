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
public class EliminarParticipanteDeLista extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idLista = ((String[]) parametros.get("id_lista"))[0];
        String idEmpresa = ((String[]) parametros.get("id_empresa"))[0];
        String idParticipante = ((String[]) parametros.get("id_participante"))[0];
        Recordset estudios = getEstudios(idEmpresa, idLista);
        estudios.top();
        
        while (estudios.next()){
        	
////////////////////////////////////////////////////////
        			Recordset instrumentos = getInstrumentos(estudios.getString("id_estudio"));
                	instrumentos.top();
                	while (instrumentos.next()){
                		TokenGenerator tg = new TokenGenerator();
            	        String token = tg.generarToken(idParticipante, instrumentos.getString("id_instrumento"));
            	        if (findToken(token) == true){
            		            String sql2 = StringUtil.replace(getResource("delete-token.sql"), "{{id_participante}}", idParticipante);
            		            sql2 = StringUtil.replace(sql2, "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
            		            sql2 = StringUtil.replace(sql2, "{{token_participante}}", token);
            		            getDb().exec(sql2);
            	        }
                    }                 
////////////////////////////////////////////////////////                            
        
			        String sql = StringUtil.replace(getResource("delete.sql"), "{{id_lista}}", idLista);
			        sql = StringUtil.replace(sql, "{{id_participante}}", idParticipante);
			        sql = StringUtil.replace(sql, "{{userlogin}}", this.getUserName());
			        System.out.println(sql);
			        getDb().exec(sql);
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
    
    Recordset getInstrumentos (String idEstudio) throws Throwable{
    	String query = "select instrumento.* " +
    			"from ajvieira_isurvey_app.instrumento, ajvieira_isurvey_app.estudio " +
    			"where instrumento.id_estudio = estudio.id_estudio " +
    			"and estudio.id_estudio = " + idEstudio;
    	Recordset instrumentos = this.getDb().get(query);
    	return instrumentos;
    }
    
    Recordset getEstudios (String idEmpresa, String idLista) throws Throwable{
    	String query = "select estudio.* from " +
    			"ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.int_lista_participantes_estudio, " +
    			"ajvieira_isurvey_app.lista_participantes, ajvieira_isurvey_app.empresa " +
    			"where empresa.id_empresa = estudio.id_empresa " +
    			"and int_lista_participantes_estudio.id_estudio = estudio.id_estudio " +
    			"and int_lista_participantes_estudio.id_lista_participantes = lista_participantes.id_lista_participantes " +
    			"and lista_participantes.id_empresa =  " + idEmpresa + " " +
    			"and lista_participantes.id_lista_participantes =  " + idLista + " " +
    			"and empresa.id_empresa = " + idEmpresa;
    	Recordset instrumentos = this.getDb().get(query);
    	return instrumentos;
    }
}
