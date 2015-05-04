/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package estudio.cerrado;


import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.StringUtil;
import java.util.Map;
import java.util.Enumeration;

/**
 *
 * @author addSolutions
 */
public class InsertarOpinion extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idInstrumento = ((String[]) parametros.get("id"))[0];
        String token = ((String[]) parametros.get("token"))[0];
        String tipo = ((String[]) parametros.get("tipo"))[0];
        String texto = ((String[]) parametros.get("texto"))[0];
        Recordset interseccion = new Recordset();
        interseccion = getIntParticipanteInstrumento(token);
        interseccion.top();
        String idParticipante = "0";
        while (interseccion.next()){
        	idParticipante = interseccion.getString("id_participante");
        }
        String query = StringUtil.replace(getResource("insert.sql"), "{{id_instrumento}}", idInstrumento);
        query = StringUtil.replace(query, "{{id_participante}}", idParticipante);
        query = StringUtil.replace(query, "{{tipo}}", tipo);
        query = StringUtil.replace(query, "{{texto}}", texto);
        getDb().exec(query);
        getDb().commit();
        return 0;
    }

    Recordset getIntParticipanteInstrumento (String token) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where token_participante = '" + token + "'";
    	return this.getDb().get(query);
    }
}
