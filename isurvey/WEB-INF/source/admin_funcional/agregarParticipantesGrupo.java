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
public class agregarParticipantesGrupo extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();

        String id_lista = ((String[]) parametros.get("id_lista_participantes"))[0];
        System.out.println(id_lista);
        while(names.hasMoreElements()){
            String nombreCampo = (String) names.nextElement();
            if (nombreCampo.contains("cod_")){
                String nombreCampoParticipante[] = nombreCampo.split("_");
                String idParticipante = nombreCampoParticipante[1];
                String sql = StringUtil.replace(getResource("insert.sql"), "{{id_participante}}", idParticipante);
                sql = StringUtil.replace(sql, "{{id_lista_participantes}}", id_lista);
                getDb().exec(sql);
                getDb().commit();
            }
        }
        return 0;
}
}
