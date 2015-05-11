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
public class EliminarInstrumento extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idInstrumento = ((String[]) parametros.get("id"))[0];
        Recordset empresa = getEmpresa(this.getUserName());
        String idEmpresa = "";
        empresa.top();
        TokenGenerator tg = new TokenGenerator();
        while (empresa.next()){
        	idEmpresa = empresa.getString("id_empresa");
        }
	    Recordset participantes = getParticipantes(idEmpresa); 
	    participantes.top();
	    while (participantes.next()){
	    	String sql3 = StringUtil.replace(getResource("delete-lime-respuestas.sql"), "{{id_instrumento}}", idInstrumento);
	    	sql3 = StringUtil.replace(sql3, "{{token}}", tg.generarToken(participantes.getString("id_participante"), idInstrumento));
	    	getDb().exec(sql3);
	    	
	    	String sql4 = StringUtil.replace(getResource("delete-lime-token.sql"), "{{id_instrumento}}", idInstrumento);
	    	sql4 = StringUtil.replace(sql4, "{{token}}", tg.generarToken(participantes.getString("id_participante"), idInstrumento));
	    	getDb().exec(sql4);
	    }
	        	////////////////////////////////////////////////////////
	            		            String sql2 = StringUtil.replace(getResource("delete-token.sql"), "{{id}}", idInstrumento);
	            		            getDb().exec(sql2);                
	//////////////////////////////////////////////////////// 
	            		            String sql = StringUtil.replace(getResource("delete.sql"), "{{id}}", idInstrumento);
	            		            getDb().exec(sql);
	    getDb().commit();    
	    return 0;
    }
    
    Recordset getEmpresa (String username) throws Throwable{
    	String query = "select *" +
		" from ajvieira_isurvey_app.empresa where id_empresa = (select id_empresa from ajvieira_isurvey_sec.s_user where userlogin = " + username + ")";
return this.getDb().get(query);
    }
    
    Recordset getParticipantes(String idEmpresa) throws Throwable{
    	String query = "select id_participante, nombre_participante, apellido_participante, email_participante" +
    			" from ajvieira_isurvey_app.participante where id_empresa = " + idEmpresa;
    	return this.getDb().get(query);
    }

}
