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
public class DeleteEstudio extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idEstudio = ((String[]) parametros.get("id"))[0];
        
        Recordset estudio = getEstudio(idEstudio);
        estudio.top();
        String tipo = "";
        String nombre = "";
        String idEmpresa = "";
        while (estudio.next()){
        	tipo = estudio.getString("tipo");
        	nombre = estudio.getString("nombre_estudio");
        	idEmpresa = estudio.getString("id_empresa");
        }
        
        if (tipo.equals("Abierto-Identificado")){
        	String sql1 = StringUtil.replace(getResource("delete-lista.sql"), "{{nombre_lista}}", nombre);
        	this.getDb().exec(sql1);
        }
        if (tipo.equals("Abierto-Identificado") || (tipo.equals("Cerrado"))){
        	TokenGenerator tg = new TokenGenerator();
        	Recordset instrumentos = getInstrumentos(idEstudio);
        	Recordset participantes = getParticipantes(idEmpresa);
        	instrumentos.top();
        	while (instrumentos.next()){
        		participantes.top();
        		while (participantes.next()){
	        		String sql3 = StringUtil.replace(getResource("delete-lime-respuestas.sql"), "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
	        		sql3 = StringUtil.replace(sql3, "{{token}}", tg.generarToken(participantes.getString("id_participante"), instrumentos.getString("id_instrumento")));
	        		getDb().exec(sql3);
	        		
	        		String sql4 = StringUtil.replace(getResource("delete-lime-token.sql"), "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
	        		sql4 = StringUtil.replace(sql4, "{{token}}", tg.generarToken(participantes.getString("id_participante"), instrumentos.getString("id_instrumento")));
	        		getDb().exec(sql4);
        		}
        	}
        }
    	String sql2 = StringUtil.replace(getResource("delete.sql"), "{{id}}", idEstudio);
    	this.getDb().exec(sql2);

        getDb().commit();
        return 0;
    }
    
    Recordset getParticipantes(String idEmpresa) throws Throwable{
    	String query = "select id_participante, nombre_participante, apellido_participante, email_participante" +
    			" from ajvieira_isurvey_app.participante where id_empresa = " + idEmpresa;
    	return this.getDb().get(query);
    }
    
    Recordset getInstrumentos (String idEstudio) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.instrumento where id_estudio = " + idEstudio;
    	return this.getDb().get(query);
    }
    
    Recordset getEstudio (String id) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.estudio where id_estudio = " + id;
    	return this.getDb().get(query);
    }
}
