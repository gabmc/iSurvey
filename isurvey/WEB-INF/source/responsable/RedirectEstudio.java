/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package responsable;


import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.StringUtil;
import java.util.Map;
import java.util.Enumeration;

import tokens_participantes.TokenGenerator;

/**
 *
 * @author addSolutions
 */
public class RedirectEstudio extends GenericTransaction {
	
	   @Override
	   public int service(Recordset inputParams) throws Throwable {

	       super.service(inputParams);
	       this.getDb().beginTrans();
	       Enumeration names = this.getRequest().getParameterNames();
	       Map parametros = this.getRequest().getParameterMap();
	       String id = ((String[]) parametros.get("id"))[0];
	       if (getTipoEstudio(id).equals("Cerrado"))
	    	   getRequest().setAttribute("ruta", "action/responsable/monitoreo_general/estudio/cerrado/instrumentos/form?id=" + id);
	       if (getTipoEstudio(id).equals("Abierto-Identificado"))
	    	   getRequest().setAttribute("ruta", "action/responsable/monitoreo_general/estudio/abierto_identificado/form?id=" + id);
	       if (getTipoEstudio(id).equals("Abierto-Anonimo"))
	    	   getRequest().setAttribute("ruta", "action/responsable/monitoreo_general/estudio/abierto_anonimo/form?id=" + id);
	       return 0;
	   }
	   
	   String getTipoEstudio (String idEstudio) throws Throwable{
		   String sql = "select tipo from ajvieira_isurvey_app.estudio where id_estudio = " + idEstudio;
		   Recordset rs = this.getDb().get(sql);
		   rs.top();
		   String tipo = "";
		   while (rs.next())
			   tipo = rs.getString("tipo");
		   return tipo;
	   }
}