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
public class InsertarParticipante extends GenericTableManager {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idParticipante = ((String[]) parametros.get("id_participante"))[0];
        Recordset listas = getListas();
        
        while (listas.next()){
        	String sql = StringUtil.replace(getResource("insert-int.sql"), "{{id_participante}}", idParticipante);
        	sql = StringUtil.replace(sql, "{{userlogin}}", this.getUserName());
        	sql = StringUtil.replace(sql, "{{id_lista_participantes}}", listas.getString("id_lista_participantes"));
        	this.getDb().exec(sql);
        }
  
        getDb().commit();
        return 0;
    }
    
    Recordset getListas() throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.lista_participantes where oculta = 'Si'";
    	return this.getDb().get(query);
    }
}
