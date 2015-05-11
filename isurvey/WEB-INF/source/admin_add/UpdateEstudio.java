/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin_add;


import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.StringUtil;
import java.util.Map;
import java.util.Enumeration;
import tokens_participantes.*;

/**
 *
 * @author addSolutions
 */
public class UpdateEstudio extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String nombre = ((String[]) parametros.get("nombre_estudio"))[0];
        String tipo = ((String[]) parametros.get("tipo"))[0];
        String idEmpresa = ((String[]) parametros.get("id_empresa"))[0];
        String idEstudio = ((String[]) parametros.get("id"))[0];
        
        TokenGenerator tg = new TokenGenerator();
        Recordset estudio = getEstudio(idEstudio);
        estudio.top();
        String tipoOld = "";
        while (estudio.next()){
        	tipoOld = estudio.getString("tipo");
        }
        //Si se esta cambiando de cualquier tipo a abierto-identificado
        if (!tipoOld.equals("Abierto-Identificado") && tipo.equals("Abierto-Identificado")){
        	String sql1 = StringUtil.replace(getResource("insert-lista.sql"), "{{nombre_lista}}", nombre);
        	sql1 = StringUtil.replace(sql1, "{{id_empresa}}", idEmpresa);
        	getDb().exec(sql1);
        	String idLista = getIdListaParticipantes(nombre);
        	Recordset participantes = getParticipantes(idEmpresa);
        	Recordset instrumentos = getInstrumentos(idEstudio);
        	participantes.top();
        	while (participantes.next()){
        		String sql2 = StringUtil.replace(getResource("insert-int-lista-participante.sql"), "{{id_participante}}", participantes.getString("id_participante"));
        		sql2 = StringUtil.replace(sql2, "{{id_empresa_participante}}", idEmpresa);
        		sql2 = StringUtil.replace(sql2, "{{id_lista_participantes}}", idLista);
        		getDb().exec(sql2);
        		instrumentos.top();
        		while (instrumentos.next()){
        			String token = tg.generarToken(participantes.getString("id_participante"), instrumentos.getString("id_instrumento"));
        			String sql3 = StringUtil.replace(getResource("insert-lime.sql"), "{{id_encuesta}}", instrumentos.getString("id_instrumento"));
        			sql3 = StringUtil.replace(sql3, "{{firstname}}", participantes.getString("nombre_participante"));
        			sql3 = StringUtil.replace(sql3, "{{lastname}}", participantes.getString("apellido_participante"));
        			sql3 = StringUtil.replace(sql3, "{{email}}", participantes.getString("email_participante"));
        			sql3 = StringUtil.replace(sql3, "{{token}}", token);
        			this.getDb().exec(sql3);
        			
        			String sql4 = StringUtil.replace(getResource("insert-lime-respuestas.sql"), "{{id_encuesta}}", instrumentos.getString("id_instrumento"));
        			sql4 = StringUtil.replace(sql4, "{{token}}", token);
        			this.getDb().exec(sql4);
        			
        			String sql5 = StringUtil.replace(getResource("insert-token.sql"), "{{id_participante}}", participantes.getString("id_participante"));
        			sql5 = StringUtil.replace(sql5, "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
        			sql5 = StringUtil.replace(sql5, "{{token}}", token);
        			this.getDb().exec(sql5);
        		}
        	}
            sql1 = StringUtil.replace(getResource("update.sql"), "{{nombre_estudio}}", nombre);
        	sql1 = StringUtil.replace(sql1, "{{tipo}}", tipo);
        	sql1 = StringUtil.replace(sql1, "{{id_empresa}}", idEmpresa);
        	sql1 = StringUtil.replace(sql1, "{{id_estudio}}", idEstudio);
        	getDb().exec(sql1);
            
        	String sql4 = StringUtil.replace(getResource("insert-int-lista-estudio.sql"), "{{id_lista}}", idLista);
            getDb().exec(sql4);
        }
        //Si se esta dejando el tipo abierto-identificado igual
        if (tipoOld.equals("Abierto-Identificado") && tipo.equals("Abierto-Identificado")) {
            String sql1 = StringUtil.replace(getResource("update.sql"), "{{nombre_estudio}}", nombre);
        	sql1 = StringUtil.replace(sql1, "{{tipo}}", tipo);
        	sql1 = StringUtil.replace(sql1, "{{id_empresa}}", idEmpresa);
        	sql1 = StringUtil.replace(sql1, "{{id_estudio}}", idEstudio);
        	getDb().exec(sql1);
        }
        //Si se esta pasando de abierto-identificado a cualquier otro tipo
        if (tipoOld.equals("Abierto-Identificado") && !tipo.equals("Abierto-Identificado")){
            String sql1 = StringUtil.replace(getResource("update.sql"), "{{nombre_estudio}}", nombre);
        	sql1 = StringUtil.replace(sql1, "{{tipo}}", tipo);
        	sql1 = StringUtil.replace(sql1, "{{id_empresa}}", idEmpresa);
        	sql1 = StringUtil.replace(sql1, "{{id_estudio}}", idEstudio);
        	getDb().exec(sql1);
        	
        	String sql2 = StringUtil.replace(getResource("delete-lista.sql"), "{{nombre_lista}}", nombre);
        	getDb().exec(sql2);	
        	
        	Recordset participantes = getParticipantes(idEmpresa);
        	participantes.top();
        	Recordset instrumentos = getInstrumentos(idEstudio);
        	while (participantes.next()){
        		instrumentos.top();
        		while (instrumentos.next()){
        			String token = tg.generarToken(participantes.getString("id_participante"), instrumentos.getString("id_instrumento"));
        			String sql3 = StringUtil.replace(getResource("delete-lime-respuestas.sql"), "{{token}}", token);
        			sql3 = StringUtil.replace(sql3, "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
        			getDb().exec(sql3);
        			
        			String sql4 = StringUtil.replace(getResource("delete-lime-token.sql"), "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
        			sql4 = StringUtil.replace(sql4, "{{token}}", token);
        			getDb().exec(sql4);
        			
        			String sql5 = StringUtil.replace(getResource("delete-token.sql"), "{{token_participante}}", token);
        			getDb().exec(sql5);
        		}
        	}
        }
        

        //getDb().commit();
        return 0;
    }
    
    Recordset getInstrumentos (String idEstudio) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.instrumento where id_estudio = " + idEstudio;
    	return this.getDb().get(query);
    }
    
    Recordset getEstudio (String id) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.estudio where id_estudio = " + id;
    	return this.getDb().get(query);
    }
    
    String getIdEstudio (String nombre) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.estudio where nombre_estudio = '" + nombre + "'";
    	Recordset lista = this.getDb().get(query);
    	lista.top();
    	String id = "";
    	while (lista.next()){
    		id = lista.getString("id_estudio");
    	}
    	return id;
    }
    
    String getIdListaParticipantes (String nombre) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.lista_participantes where nombre_lista_participantes = '" + nombre + "' and oculta = 'Si'";
    	Recordset lista = this.getDb().get(query);
    	lista.top();
    	String id = "";
    	while (lista.next()){
    		id = lista.getString("id_lista_participantes");
    	}
    	return id;
    }
    
    Recordset getParticipantes(String idEmpresa) throws Throwable{
    	String query = "select id_participante, nombre_participante, apellido_participante, email_participante" +
    			" from ajvieira_isurvey_app.participante where id_empresa = " + idEmpresa;
    	return this.getDb().get(query);
    }
}
