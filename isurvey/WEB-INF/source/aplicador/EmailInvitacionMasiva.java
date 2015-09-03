/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aplicador;


import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.RecordsetException;
import dinamica.StringUtil;
import java.util.Map;
import java.util.Enumeration;

/**
 *
 * @author addSolutions
 */
public class EmailInvitacionMasiva extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
    	Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
    	String idEstudio = ((String[]) parametros.get("id_estudio"))[0];
    	String flag = ((String[]) parametros.get("flag"))[0];
    	Recordset output = getOutput(getParticipantes(idEstudio, flag), idEstudio);
    	this.publish("output", output);
        return 0;
    }
    
    Recordset getOutput(Recordset participantes, String idEstudio) throws Throwable{
    	Recordset tituloYcuerpo = getTituloYcuerpo(idEstudio);
    	tituloYcuerpo.first();
    	String subject = tituloYcuerpo.getString("titulo_email");
    	participantes.top();
    	Recordset output = new Recordset();
    	output.append("subject", java.sql.Types.VARCHAR);
    	output.append("body", java.sql.Types.VARCHAR);
    	output.append("email_participante", java.sql.Types.VARCHAR);
    	while (participantes.next()){
    		String body = tituloYcuerpo.getString("cuerpo_email");
    		body = StringUtil.replace(body, "{{banner}}", getDireccionBanner(idEstudio));
        	body = StringUtil.replace(body, "{{nombre_estudio}}", getNombreEstudio(idEstudio));
    		body = StringUtil.replace(body, "{{enlace}}", getEnlace(participantes.getString("token_participante"),idEstudio));
    		body = StringUtil.replace(body, "{{nombre_participante}}", participantes.getString("nombre_participante"));
    		body = StringUtil.replace(body, "{{apellido_participante}}", participantes.getString("apellido_participante"));
    		output.addNew();
    		output.setValue("subject", subject);
    		output.setValue("body", body);
    		output.setValue("email_participante", participantes.getString("email_participante"));
    	}
    	return output;
    }
    
    Recordset getParticipantes(String idEstudio, String flag) throws Throwable{
    	if (flag.equals("0")){
	    	String query = StringUtil.replace(getResource("participantes_sin_iniciar_incompleta.sql"), "${fld:id_estudio}", idEstudio);
	    	query = StringUtil.replace(query, "${def:user}", this.getUserName());
	    	return this.getDb().get(query);
    	}
    	else if (flag.equals("1")){
    		String query = StringUtil.replace(getResource("participantes_incompleta.sql"), "${fld:id_estudio}", idEstudio);
    		query = StringUtil.replace(query, "${def:user}", this.getUserName());
    		return this.getDb().get(query);
    	}
    	else if (flag.equals("2")){
    		String query = StringUtil.replace(getResource("participantes_sin_iniciar.sql"), "${fld:id_estudio}", idEstudio);
    		query = StringUtil.replace(query, "${def:user}", this.getUserName());
    		return this.getDb().get(query);
    	}
    	else{
    		String query = StringUtil.replace(getResource("participantes_todos.sql"), "${fld:id_estudio}", idEstudio);
    		query = StringUtil.replace(query, "${def:user}", this.getUserName());
    		return this.getDb().get(query);
    	}
    }
    
    Recordset getTituloYcuerpo(String idEstudio) throws Throwable{
    	String query = StringUtil.replace(getResource("estudio.sql"), "${fld:id_estudio}", idEstudio);
    	return this.getDb().get(query);
    }
    
    String getDireccionBanner(String idEstudio) throws Throwable{
    	String query = "select banner from ajvieira_isurvey_app.estudio where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(query);
    	rs.first();
    	return "<img src=\"http://www.compensa.com.ve/isurvey/images/banners_estudios/" + rs.getString("banner") + "\" width=\"700\" " +
    			"height=\"188\" alt=\"logo\" style=\"float:center; width=30%; margin-right: 1% margin-bottom: 0.5em;\">";
    }
    
    String getNombreEstudio(String idEstudio) throws Throwable{
    	String sql = "select nombre_estudio from ajvieira_isurvey_app.estudio where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("nombre_estudio");
    }

    String getEnlace(String token, String idEstudio){
    	return "<a href=\"http://www.compensa.com.ve/isurvey/action/estudio/cerrado2/form?id=" + idEstudio + "" +
    			"&token=" + token + "\">aqu&iacute;</a>";
    }
  
}
