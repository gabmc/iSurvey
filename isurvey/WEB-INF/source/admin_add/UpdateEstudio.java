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
        String tituloEmail = ((String[]) parametros.get("titulo_email"))[0];
        String cuerpoEmail = ((String[]) parametros.get("cuerpo_email"))[0];
       
            String sql1 = StringUtil.replace(getResource("update.sql"), "{{nombre_estudio}}", nombre);
        	sql1 = StringUtil.replace(sql1, "{{tipo}}", tipo);
        	sql1 = StringUtil.replace(sql1, "{{id_empresa}}", idEmpresa);
        	sql1 = StringUtil.replace(sql1, "{{titulo_email}}", tituloEmail);
        	sql1 = StringUtil.replace(sql1, "{{cuerpo_email}}", cuerpoEmail);
        	sql1 = StringUtil.replace(sql1, "{{id_estudio}}", idEstudio);
        	getDb().exec(sql1);
        	
        //getDb().commit();
        return 0;
    }
}
