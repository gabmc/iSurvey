/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_add;

import dinamica.GenericTableManager;
import dinamica.Recordset;
import dinamica.StringUtil;
import java.util.Enumeration;
import java.util.Map;

/**
 *
 * @author Randy
 */
public class InsertarEstudio2 extends GenericTableManager {

    @Override
    public int service(Recordset inputParams) throws Throwable {

        ///////////////////////////////////////////////

        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String nombre = ((String[]) parametros.get("nombre_estudio2"))[0];
        String tipo = ((String[]) parametros.get("tipo2"))[0];
        String idEmpresa = ((String[]) parametros.get("id_empresa2"))[0];
        
        if (tipo.equals("Abierto-Identificado")){
            String sql1 = StringUtil.replace(getResource("insert-lista.sql"), "{{nombre_lista}}", nombre);
            sql1 = StringUtil.replace(sql1, "{{id_empresa}}", idEmpresa);
            getDb().exec(sql1);
            
            String idLista = getIdListaParticipantes(nombre);

            Recordset participantes = getParticipantes(idEmpresa);
            participantes.top();
            while (participantes.next()){
                String sql2 = StringUtil.replace(getResource("insert-int-lista-participante.sql"), "{{id_participante}}", participantes.getString("id_participante"));
                sql2 = StringUtil.replace(sql2, "{{id_empresa_participante}}", idEmpresa);
                sql2 = StringUtil.replace(sql2, "{{id_lista_participantes}}", idLista);
                getDb().exec(sql2);
            }
                  
            String sql4 = StringUtil.replace(getResource("insert-int-lista-estudio.sql"), "{{id_lista}}", idLista);
            getDb().exec(sql4);
        }

        ///////////////////////////////////////////////
        return super.service(inputParams);
    }


    ///////////////////////////////////////////

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
        String query = "select id_participante from ajvieira_isurvey_app.participante where id_empresa = " + idEmpresa;
        return this.getDb().get(query);
    }

    ///////////////////////////////////////////
}
