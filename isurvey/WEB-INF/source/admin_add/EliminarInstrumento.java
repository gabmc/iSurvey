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
	        
	        	////////////////////////////////////////////////////////
	            		            String sql2 = StringUtil.replace(getResource("delete-token.sql"), "{{id}}", idInstrumento);
	            		            getDb().exec(sql2);                
	//////////////////////////////////////////////////////// 
	            		            String sql = StringUtil.replace(getResource("delete.sql"), "{{id}}", idInstrumento);
	            		            getDb().exec(sql);
	    getDb().commit();    
	    return 0;
    }

}
