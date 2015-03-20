/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin_funcional;

import dinamica.GenericTransaction;
import dinamica.Recordset;
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
        while(names.hasMoreElements()){
            String nombreCampo = (String) names.nextElement();
            if (nombreCampo.contains("cod_")){
                String nombreCampoParticipante[] = nombreCampo.split("_");
                String idParticipante = nombreCampoParticipante[1];
                if (finder(idParticipante, idLista) == false){
                    String sql = StringUtil.replace(getResource("insert.sql"), "{{id_participante}}", idParticipante);
                    sql = StringUtil.replace(sql, "{{id_lista_participantes}}", idLista);
                    getDb().exec(sql);
                }
            }
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
}
