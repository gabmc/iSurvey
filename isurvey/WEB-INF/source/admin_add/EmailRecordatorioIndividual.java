/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin_add;


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
public class EmailRecordatorioIndividual extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
    	Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
    	String idEstudio = ((String[]) parametros.get("id_estudio"))[0];
    	String idParticipante = ((String[]) parametros.get("id_participante"))[0];
    	Recordset output = getOutput(getParticipantes(idParticipante, idEstudio), idEstudio);
    	this.publish("output", output);
        return 0;
    }
    
    Recordset getOutput(Recordset participantes, String idEstudio) throws Throwable{
    	Recordset tituloYcuerpo = getTituloYcuerpo(idEstudio);
    	tituloYcuerpo.first();
    	String subject = tituloYcuerpo.getString("titulo_mail_recordatorio");
    	participantes.top();
    	Recordset output = new Recordset();
    	output.append("subject", java.sql.Types.VARCHAR);
    	output.append("body", java.sql.Types.VARCHAR);
    	output.append("email_participante", java.sql.Types.VARCHAR);
    	while (participantes.next()){
    		String body = tituloYcuerpo.getString("cuerpo_mail_recordatorio");
    		body = StringUtil.replace(body, "{{banner}}", "ggg");
        	body = StringUtil.replace(body, "{{nombre_estudio}}", getNombreEstudio(idEstudio));
    		body = StringUtil.replace(body, "{{enlace}}", participantes.getString("token_participante"));
    		body = StringUtil.replace(body, "{{nombre_participante}}", participantes.getString("nombre_participante"));
    		body = StringUtil.replace(body, "{{apellido_participante}}", participantes.getString("apellido_participante"));
    		output.addNew();
    		output.setValue("subject", subject);
    		output.setValue("body", body);
    		output.setValue("email_participante", participantes.getString("email_participante"));
    	}
    	return output;
    }
    
    Recordset getParticipantes(String idParticipante, String idEstudio) throws Throwable{
	    	String query = StringUtil.replace(getResource("participante.sql"), "${fld:id_estudio}", idEstudio);
	    	query = StringUtil.replace(query, "${fld:id_empresa}", getIdEmpresa(idEstudio));
	    	query = StringUtil.replace(query, "${fld:id_participante}", idParticipante);
	    	return this.getDb().get(query);
    }
    
    Recordset getTituloYcuerpo(String idEstudio) throws Throwable{
    	String query = StringUtil.replace(getResource("estudio.sql"), "${fld:id_estudio}", idEstudio);
    	return this.getDb().get(query);
    }
    
    String getDireccionBanner(String idEstudio) throws Throwable{
    	String query = "select banner from ajvieira_isurvey_app.estudio where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(query);
    	rs.first();
    	return "<img src=\"${def:context}/images/banners_estudios/" + rs.getString("banner") + "\" width=\"700\" " +
    			"height=\"188\" alt=\"logo\" style=\"float:center; width=30%; margin-right: 1% margin-bottom: 0.5em;\">";
    }
    
    String getNombreEstudio(String idEstudio) throws Throwable{
    	String sql = "select nombre_estudio from ajvieira_isurvey_app.estudio where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("nombre_estudio");
    }

    String getIdEmpresa (String idEstudio) throws Throwable{
    	String sql = "select id_empresa from ajvieira_isurvey_app.estudio where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("id_empresa");
    }
}