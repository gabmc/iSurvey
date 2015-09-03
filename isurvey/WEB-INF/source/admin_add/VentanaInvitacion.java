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
public class VentanaInvitacion extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idEstudio = ((String[]) parametros.get("id_estudio"))[0];
        String idParticipante = ((String[]) parametros.get("id_participante"))[0];
        String sql1 = StringUtil.replace(getResource("query-participante-aplicador.sql"), "${fld:id_estudio}", idEstudio);
        sql1 = StringUtil.replace(sql1, "${fld:id_empresa}", getIdEmpresa(idEstudio));
        sql1 = StringUtil.replace(sql1, "${fld:id_participante}", idParticipante);
        System.out.println(sql1);
        Recordset rs = this.getDb().get(sql1);
        rs.first();
        Recordset participante = getParticipante(idParticipante, getIdEmpresa(idEstudio));
        participante.first();
        String sql2 = StringUtil.replace(getResource("sql.sql"), "${fld:id_estudio}", idEstudio);
        Recordset rs2 = this.getDb().get(sql2);
        rs2.first();
        String subject = rs2.getString("titulo_email");
        String body = rs2.getString("cuerpo_email");
        body = StringUtil.replace(body, "{{banner}}", getDireccionBanner(idEstudio));
        body = StringUtil.replace(body, "{{nombre_estudio}}", getNombreEstudio(idEstudio));
        body = StringUtil.replace(body, "{{nombre_participante}}", participante.getString("nombre_participante"));
        body = StringUtil.replace(body, "{{apellido_participante}}", participante.getString("apellido_participante"));
        body = StringUtil.replace(body, "{{enlace}}", getEnlace(idEstudio, getToken(idParticipante, idEstudio)));
        this.publish("sql.sql", getOutput(subject, body));
        this.publish("query-participante-aplicador.sql", rs);
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
    
    Recordset getParticipante(String idParticipante, String idEmpresa) throws Throwable{
    	String sql = "select nombre_participante, apellido_participante from ajvieira_isurvey_app.participante " +
    			"where id_participante = " + idParticipante + " and id_empresa = " + idEmpresa;
    	return this.getDb().get(sql);
    }
    
    String getIdEmpresa (String idEstudio) throws Throwable{
    	String sql = "select id_empresa from ajvieira_isurvey_app.estudio where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("id_empresa");
    }
    
    String getToken (String idParticipante, String idEstudio) throws Throwable{
    	String sql = "select token_participante from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where id_participante = " + idParticipante + " and id_instrumento = (select id_instrumento " +
    			"from ajvieira_isurvey_app.instrumento where id_estudio = " + idEstudio + " limit 1)";
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("token_participante");
    }
    
    String getEnlace (String idEstudio, String token){
    	String output = "<a href=\"http://www.compensa.com.ve/isurvey/action/estudio/cerrado2/form?id=" + idEstudio;
    	output = output + "&token=" + token + "\">aqu&iacute;</a>";
    	return output;
    }
}
