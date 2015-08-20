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
public class VentanaRecordatorio extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idEstudio = ((String[]) parametros.get("id_estudio"))[0];
        String sql1 = StringUtil.replace(getResource("sql.sql"), "${fld:id_estudio}", idEstudio);
        Recordset rs = this.getDb().get(sql1);
        rs.first();
        String subject = rs.getString("titulo_mail_recordatorio");
        String body = rs.getString("cuerpo_mail_recordatorio");
        body = StringUtil.replace(body, "{{banner}}", getDireccionBanner(idEstudio));
        body = StringUtil.replace(body, "{{nombre_estudio}}", getNombreEstudio(idEstudio));
        this.publish("sql.sql", getOutput(subject, body));	
        //getDb().commit();
        return 0;
    }
    
    Recordset getOutput(String subject, String body) throws RecordsetException{
    	Recordset output = new Recordset();
    	output.append("subject", java.sql.Types.VARCHAR);
    	output.append("body", java.sql.Types.VARCHAR);
    	output.addNew();
    	output.setValue("subject", subject);
    	output.setValue("body", body);
    	return output;
    }
    
    String getDireccionBanner(String idEstudio) throws Throwable{
    	String query = "select banner from ajvieira_isurvey_app.estudio where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(query);
    	rs.first();
    	return "<img src=\"${def:context}/images/banners_estudios/" + rs.getString("banner") + "\" width=\"350\" " +
    			"height=\"94\" alt=\"logo\" style=\"float:center; width=30%; margin-right: 1% margin-bottom: 0.5em;\">";
    }
    
    String getNombreEstudio(String idEstudio) throws Throwable{
    	String sql = "select nombre_estudio from ajvieira_isurvey_app.estudio where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("nombre_estudio");
    }
}
