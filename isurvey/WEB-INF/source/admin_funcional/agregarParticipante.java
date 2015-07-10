/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin_funcional;

import tokens_participantes.*;
import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.RecordsetException;
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
        Recordset participantesFinales = new Recordset();
        participantesFinales.append("id_participante", java.sql.Types.VARCHAR);
        
        while(names.hasMoreElements()){
            String nombreCampo = (String) names.nextElement();
            if (nombreCampo.contains("cod_")){
                String nombreCampoParticipante[] = nombreCampo.split("_");
                String idParticipante = nombreCampoParticipante[1];
                participantesFinales.addNew();
                participantesFinales.setValue("id_participante", idParticipante);
                if (finder(idParticipante, idLista) == false){
                    String sql = StringUtil.replace(getResource("insert.sql"), "{{id_participante}}", idParticipante);
                    sql = StringUtil.replace(sql, "{{id_lista_participantes}}", idLista);
                    sql = StringUtil.replace(sql, "{{id_empresa}}", getIdEmpresa());
                    getDb().exec(sql);
                    
////////////////////////////////////////////////////////
                	Recordset instrumentos = getInstrumentos(idLista);
                    	instrumentos.top();
            	    	while (instrumentos.next()){
            	    		Recordset participante = getParticipante(idParticipante);
            	    		participante.first();
            	    		TokenGenerator tg = new TokenGenerator();
            		        String token = tg.generarToken(participante.getString("id_participante"), instrumentos.getString("id_instrumento"));
            		        if (findToken(token) == false){
            			            String sql2 = StringUtil.replace(getResource("insert-token.sql"), "{{id_participante}}", participante.getString("id_participante"));
            			            sql2 = StringUtil.replace(sql2, "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
            			            sql2 = StringUtil.replace(sql2, "{{token}}", token);
            			            getDb().exec(sql2);        			 
            	
            			            String sql3 = StringUtil.replace(getResource("insert-lime.sql"), "{{id_encuesta}}", instrumentos.getString("id_instrumento"));
            			            sql3 = StringUtil.replace(sql3, "{{firstname}}", participante.getString("nombre_participante"));
            			            sql3 = StringUtil.replace(sql3, "{{lastname}}", participante.getString("apellido_participante"));
            			            sql3 = StringUtil.replace(sql3, "{{email}}", participante.getString("email_participante"));
            			            sql3 = StringUtil.replace(sql3, "{{token}}", token);
            			            getDb().exec(sql3);
            			            
            			            String sql4 = StringUtil.replace(getResource("insert-lime-respuestas.sql"), "{{id_encuesta}}", instrumentos.getString("id_instrumento"));
            			            sql4 = StringUtil.replace(sql4, "{{token}}", token);
            			            getDb().exec(sql4);
            			    }
            		    }
                    }                 
            ////////////////////////////////////////////////////////
                }
            }
        
        Recordset participantesAquitar = new Recordset();
        participantesAquitar.append("id_participante", java.sql.Types.VARCHAR);
        
        Recordset participantesDeLista = getParticipantesDeLista(idLista);
        participantesDeLista.top();

        while(participantesDeLista.next()){
        	String valor = participantesDeLista.getString("id_participante");
        	if (!buscar(valor, participantesFinales, "id_participante")){
        		participantesAquitar.addNew();
        		participantesAquitar.setValue("id_participante", valor);
        	}	
        }
        
        Recordset instrumentos = getInstrumentos(idLista);
        participantesAquitar.top();
        while (participantesAquitar.next()){
        	String sql = StringUtil.replace(getResource("delete-int-part-lista.sql"), "{{id_participante}}", participantesAquitar.getString("id_participante"));
        	sql = StringUtil.replace(sql, "{{id_empresa}}", getIdEmpresa());
        	sql = StringUtil.replace(sql, "{{id_lista_participantes}}", idLista);
        	getDb().exec(sql);
        	instrumentos.top();
        	while (instrumentos.next()){
        		TokenGenerator tg = new TokenGenerator();
        		sql = StringUtil.replace(getResource("delete-token.sql"), 
        				"{{token}}", 
        				tg.generarToken(participantesAquitar.getString("id_participante"), instrumentos.getString("id_instrumento")));
        		getDb().exec(sql);
        		
        		sql = StringUtil.replace(getResource("delete-lime.sql"), "{{id_encuesta}}", instrumentos.getString("id_instrumento"));
        		sql = StringUtil.replace(sql, "{{token}}", tg.generarToken(participantesAquitar.getString("id_participante"), instrumentos.getString("id_instrumento")));
        		getDb().exec(sql);
        		
        		sql = StringUtil.replace(getResource("delete-lime-respuestas.sql"), "{{id_encuesta}}", instrumentos.getString("id_instrumento"));
        		sql = StringUtil.replace(sql, "{{token}}", tg.generarToken(participantesAquitar.getString("id_participante"), instrumentos.getString("id_instrumento")));
        		getDb().exec(sql);
        	}
        	
        }
        getDb().commit();
        return 0;
    }
    
    Boolean buscar(String valor, Recordset rs, String columna) throws Throwable{
    	rs.top();
    	while (rs.next()){
    		if (valor.equals(rs.getString(columna)))
    			return true;
    	}
    	return false;
    }
    
    Recordset getParticipantesDeLista(String idLista) throws Throwable{
    	String sql = "select * from ajvieira_isurvey_app.int_participante_lista_participantes " +
    			"where id_lista_participantes = " + idLista;
    	return this.getDb().get(sql);
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

    String getIdEmpresa() throws Throwable{
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
    
    Recordset getParticipante (String id) throws Throwable{
    	String query = "select id_participante, nombre_participante, apellido_participante, email_participante" +
    			" from ajvieira_isurvey_app.participante " +
    			"where id_empresa = (select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '" +this.getUserName()+ "') " +
    			"and id_participante = " + id;
    	return this.getDb().get(query);
    }
}
