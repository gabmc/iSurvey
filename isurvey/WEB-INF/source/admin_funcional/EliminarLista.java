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

import com.mysql.jdbc.StringUtils;

/**
 *
 * @author addSolutions
 */
public class EliminarLista extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();

        String idLista = ((String[]) parametros.get("id"))[0];
        
        Recordset estudios = getEstudiosDeLista(idLista);
        estudios.top();
        Recordset participantes = getParticipantes(idLista);
        
        while (estudios.next()){
        	Recordset instrumentos = getInstrumentosDeEstudio(estudios.getString("id_estudio"));
        	while (instrumentos.next()){
        		participantes.top();
        		while (participantes.next()){
        			if (estaEnVariasListas(participantes.getString("id_participante"), participantes.getString("id_empresa"), estudios.getString("id_estudio")) == false){
        			TokenGenerator tg = new TokenGenerator();
        			String token = tg.generarToken(participantes.getString("id_participante"), instrumentos.getString("id_instrumento"));
	        			if (findToken(token) == true){
	        				String sql = StringUtil.replace(getResource("delete-token.sql"), "{{token}}", token);
	        				this.getDb().exec(sql);
	        				String sql2 = StringUtil.replace(getResource("delete-lime-token.sql"), "{{token}}", token);
	        				sql2 = StringUtil.replace(sql2, "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
	        				this.getDb().exec(sql2);
	        				String sql3 = StringUtil.replace(getResource("delete-lime-respuestas.sql"), "{{token}}", token);
	        				sql3 = StringUtil.replace(sql3, "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
	        				this.getDb().exec(sql3);
	        			}
        			}
        		}
        	}
        	
        }
        String sql4 = StringUtil.replace(getResource("delete.sql"), "{{id_lista_participantes}}", idLista);
        getDb().exec(sql4);
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
    
    Recordset getParticipantes (String idLista) throws Throwable{
    	String query = "select participante.* from ajvieira_isurvey_app.participante, " +
    			"ajvieira_isurvey_app.int_participante_lista_participantes, " +
    			"ajvieira_isurvey_app.lista_participantes " +
    			"where " +
    			"lista_participantes.id_lista_participantes = int_participante_lista_participantes.id_lista_participantes " +
    			"and int_participante_lista_participantes.id_participante = participante.id_participante " +
    			"and participante.id_empresa = lista_participantes.id_empresa " +
    			"and lista_participantes.id_lista_participantes = " + idLista;
    	Recordset instrumentos = this.getDb().get(query);
    	return instrumentos;
    }
    
    Boolean estaEnVariasListas (String idParticipante, String idEmpresa, String idEstudio) throws Throwable{
    	String query = "select lista_participantes.* " +
    			"from ajvieira_isurvey_app.participante, " +
    			"ajvieira_isurvey_app.int_participante_lista_participantes, " +
    			"ajvieira_isurvey_app.lista_participantes, " +
    			"ajvieira_isurvey_app.int_lista_participantes_estudio, " +
    			"ajvieira_isurvey_app.estudio " +
    			"where " +
    			"participante.id_participante = " + idParticipante + " " +
    			"and participante.id_empresa = " + idEmpresa + " " +
    			"and estudio.id_estudio = " + idEstudio + " " +
    			"and int_participante_lista_participantes.id_participante = participante.id_participante " +
    			"and int_participante_lista_participantes.id_empresa_participante = participante.id_empresa " +
    			"and int_participante_lista_participantes.id_lista_participantes = lista_participantes.id_lista_participantes " +
    			"and lista_participantes.id_lista_participantes = int_lista_participantes_estudio.id_lista_participantes " +
    			"and int_lista_participantes_estudio.id_estudio = estudio.id_estudio";
    	Recordset listas = this.getDb().get(query);
    	listas.top();
    	int contador = 0;
    	while (listas.next()){
    		contador++;
    	}
    	if (contador > 1)
    		return true;
    	else
    		return false;
    }
    
    Recordset getEstudiosDeLista (String idLista) throws Throwable{
    	String query = "select estudio.* " +
    			"from ajvieira_isurvey_app.estudio, " +
    			"ajvieira_isurvey_app.int_lista_participantes_estudio, " +
    			"ajvieira_isurvey_app.lista_participantes " +
    			"where " +
    			"estudio.id_estudio = int_lista_participantes_estudio.id_estudio " +
    			"and int_lista_participantes_estudio.id_lista_participantes = lista_participantes.id_lista_participantes " +
    			"and lista_participantes.id_empresa = estudio.id_empresa " +
    			"and lista_participantes.id_lista_participantes = " + idLista;
    	Recordset estudios = this.getDb().get(query);
    	return estudios;
    }
    
    Recordset getInstrumentosDeEstudio (String idEstudio) throws Throwable{
    	String query = "select instrumento.* " +
    			"from ajvieira_isurvey_app.estudio, " +
    			"ajvieira_isurvey_app.instrumento " +
    			"where " +
    			"instrumento.id_estudio = estudio.id_estudio " +
    			"and estudio.id_estudio = " + idEstudio;
    	Recordset instrumentos = this.getDb().get(query);
    	return instrumentos;
    }
}
