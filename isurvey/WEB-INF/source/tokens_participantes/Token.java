/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tokens_participantes;

import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.StringUtil;

public class Token extends GenericTransaction{

    public int service(Recordset inputParams, String idParticipante, String idLista) throws Throwable{
    	super.service(inputParams);
    	this.getDb().beginTrans();
    	Recordset instrumentos = getInstrumentos(idLista);
    	instrumentos.top();
    	while (instrumentos.next()){
	        String token = crearToken(idParticipante, instrumentos.getString("id_instrumento"));
	        if (finder(token) == false){
		            String sql = StringUtil.replace(getResource("insert-token.sql"), "{{id_participante}}", idParticipante);
		            sql = StringUtil.replace(sql, "{{id_instrumento}}", instrumentos.getString("id_instrumento"));
		            sql = StringUtil.replace(sql, "{{token}}", token);
		            getDb().exec(sql);
	        }
        }
        getDb().commit();
        return 0;
    }
    
    private String crearToken(String idParticipante, String idInstrumento) throws Throwable{
    	TokenGenerator tg = new TokenGenerator();
    	return tg.generarToken(idParticipante, idInstrumento);
    }

    Boolean finder (String token) throws Throwable{
        String query = "select * from ajvieira_isurvey_app.int_participante_instrumento";
        Recordset rs = this.getDb().get(query);
        rs.top();
        while (rs.next()){
            if (rs.getString("token_participante").equals(token)){
                return true;
            }
        }
        return false;
    }
    
    Recordset getInstrumentos (String idLista) throws Throwable{
    	String query = "SELECT instrumento . * " +
    			"FROM ajvieira_isurvey_app.instrumento, ajvieira_isurvey_app.estudio, " +
    			"ajvieira_isurvey_app.int_lista_participantes_estudio, ajvieira_isurvey_app.lista_participantes " +
    			"WHERE lista_participantes.id_lista_participantes = " + idLista + " " +
    			"AND lista_participantes.id_lista_participantes = int_lista_participantes_estudio.id_lista_participantes " +
    			"AND int_lista_participantes_estudio.id_estudio = estudio.id_estudio " +
    			"AN D estudio.id_estudio = instrumento.id_estudio";
    	Recordset instrumentos = this.getDb().get(query);
    	return instrumentos;
    }
}
