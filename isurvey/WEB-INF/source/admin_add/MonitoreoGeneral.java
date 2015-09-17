/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin_add;


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
public class MonitoreoGeneral extends GenericTransaction {
    public int service(Recordset inputParams) throws Throwable{
    	String nombre_estudio = "--";
    	String numero_encuestas = "--";
    	String numero_participantes = "--";
    	String completado = "--";
    	String porcentaje_completado = "--";
    	String incompleto = "--";
    	String porcentaje_incompleto = "--";
    	String sin_iniciar = "--";
    	String porcentaje_sin_iniciar = "--";
    	String participantes = "--";
    	String linkInstrumentos = "--";
    	Map parametros = this.getRequest().getParameterMap();
    	String idEmpresa = ((String[]) parametros.get("id_empresa"))[0];
        super.service(inputParams);
        this.getDb().beginTrans();
        Recordset estudios = getEstudios(idEmpresa);
        estudios.top();
        while (estudios.next()){
        	Recordset instrumentos = getInstrumentos(estudios.getString("id_estudio"));
        	instrumentos.top();
        	while (instrumentos.next()){
        		Recordset tokens = getTokens(instrumentos.getString("id_instrumento"));
        		tokens.top();
            	while(tokens.next()){
            		updateStatus(tokens.getString("token_participante"));
            	}
        	}
        }
        //recordset para ser mostrado en la tabla
        Recordset output = new Recordset();
        output.append("nombre_estudio", java.sql.Types.VARCHAR);
        output.append("numero_encuestas", java.sql.Types.VARCHAR);
        output.append("numero_participantes", java.sql.Types.VARCHAR);
        output.append("completado", java.sql.Types.VARCHAR);
        output.append("porcentaje_completado", java.sql.Types.VARCHAR);
        output.append("incompleto", java.sql.Types.VARCHAR);
        output.append("porcentaje_incompleto", java.sql.Types.VARCHAR);
        output.append("sin_iniciar", java.sql.Types.VARCHAR);
        output.append("porcentaje_sin_iniciar", java.sql.Types.VARCHAR);
        output.append("participantes", java.sql.Types.VARCHAR);
        output.append("link_instrumentos", java.sql.Types.VARCHAR);
        
        //recordset para ser mostrado en el pdf que se genera
        Recordset output2 = new Recordset();
        output2.append("nombre_estudio", java.sql.Types.VARCHAR);
        output2.append("numero_encuestas", java.sql.Types.VARCHAR);
        output2.append("numero_participantes", java.sql.Types.VARCHAR);
        output2.append("completado", java.sql.Types.VARCHAR);
        output2.append("porcentaje_completado", java.sql.Types.VARCHAR);
        output2.append("incompleto", java.sql.Types.VARCHAR);
        output2.append("porcentaje_incompleto", java.sql.Types.VARCHAR);
        output2.append("sin_iniciar", java.sql.Types.VARCHAR);
        output2.append("porcentaje_sin_iniciar", java.sql.Types.VARCHAR);
        
        estudios.top();
        while(estudios.next()){
        	output.addNew();
        	output2.addNew();
        	numero_encuestas = getNumeroEncuestas(estudios.getString("id_estudio"));
        	output.setValue("numero_encuestas", numero_encuestas);
        	output2.setValue("numero_encuestas", numero_encuestas);
        	nombre_estudio = estudios.getString("nombre_estudio");
        	if (getNumeroInstrumentosInt(estudios.getString("id_estudio")) > 0){
	        	if (estudios.getString("tipo").equals("Cerrado")){
		        	//nombre_estudio = "<a href=\"${def:context}${def:actionroot}/estudio/form?id=" 
		        	//	+ estudios.getString("id_estudio") + "\">" + estudios.getString("nombre_estudio") + "</a>";
		        	numero_participantes = getNumeroParticipantesCerrado(estudios.getString("id_estudio"));
		        	completado = getCompletadosCerrado(estudios.getString("id_estudio"));
		        	porcentaje_completado = getPorcentajeCompletadosCerrado(estudios.getString("id_estudio"));
		        	incompleto = getIncompletoCerrado(estudios.getString("id_estudio"));
		        	porcentaje_incompleto = getPorcentajeIncompletoCerrado(estudios.getString("id_estudio"));
		        	sin_iniciar = getSinIniciarCerrado(estudios.getString("id_estudio"));
		        	porcentaje_sin_iniciar = getPorcentajeSinIniciarCerrado(estudios.getString("id_estudio"));
		        	linkInstrumentos = "<a href=\"${def:context}${def:actionroot}/estudio/form?id=" 
		        		+ estudios.getString("id_estudio") + 
		        		"\"><img title=\"Ver detalle de Instrumentos\" src=\"${def:context}/images/clipboard2.png\" width=\"18\" height=\"18\"/></a>";
		        	participantes = "<a href=\"${def:context}${def:actionroot}/participantes/form?id=" 
		        		+ estudios.getString("id_estudio") + "\"><img title=\"Ver detalle de Participantes\" src=\"${def:context}/images/multiple_users.png\" " +
		        				"alt=\"Click para ver detalle de Instrumentos del Estudio\" width=\"18\" height=\"18\"></a>";
		        	
	        	}
	        	if (estudios.getString("tipo").equals("Abierto-Identificado")){
	        	//	nombre_estudio = "<a href=\"${def:context}${def:actionroot}/estudio/form?id=" 
		        //		+ estudios.getString("id_estudio") + "\">" + estudios.getString("nombre_estudio") + "</a>";
	        		numero_participantes = getNumeroParticipantesIdentificado(estudios.getString("id_estudio"));
	        		completado = getCompletadosIdentificado(estudios.getString("id_estudio"));
	        		porcentaje_completado = getPorcentajeCompletadoIdentificado(estudios.getString("id_estudio"));
	        		incompleto = getIncompletoIdentificado(estudios.getString("id_estudio"));
	        		porcentaje_incompleto = getPorcentajeIncompletoIdentificado(estudios.getString("id_estudio"));
	        		sin_iniciar = getSinIniciarIdentificado(estudios.getString("id_estudio"));
	        		porcentaje_sin_iniciar = getPorcentajeSinIniciarIdentificado(estudios.getString("id_estudio"));
		        	participantes = "<a href=\"${def:context}${def:actionroot}/estudio/form?id=" 
		        		+ estudios.getString("id_estudio") 
		        		+ "\"><img title=\"Ver detalle de Participantes\" src=\"${def:context}/images/multiple_users.png\" width=\"18\" height=\"18\"/></a>";
		        	linkInstrumentos = "";
	        		
	        	}
	        	if (estudios.getString("tipo").equals("Abierto-Anonimo")){
	        	//	nombre_estudio = estudios.getString("nombre_estudio");
	        		numero_participantes = getNumeroParticipantesAnonimo(estudios.getString("id_estudio"));
	        		completado = getCompletadosAnonimo(estudios.getString("id_estudio"));
	        		porcentaje_completado = getPorcentajeCompletadoAnonimo(estudios.getString("id_estudio"));
	        		incompleto = getIncompletoAnonimo(estudios.getString("id_estudio"));
	        		porcentaje_incompleto = getPorcentajeIncompletoAnonimo(estudios.getString("id_estudio"));
	        		sin_iniciar = "--";
	        		porcentaje_sin_iniciar = "--";
		        	participantes = "";
		        	linkInstrumentos = "--";	
	        	}
        	}
        	output.setValue("nombre_estudio", nombre_estudio);
        	output.setValue("numero_participantes", numero_participantes);
        	output.setValue("completado", completado);
        	output.setValue("porcentaje_completado", porcentaje_completado);
        	output.setValue("incompleto", incompleto);
        	output.setValue("porcentaje_incompleto", porcentaje_incompleto);
        	output.setValue("sin_iniciar", sin_iniciar);
        	output.setValue("porcentaje_sin_iniciar", porcentaje_sin_iniciar);
        	output.setValue("participantes", participantes);
        	output.setValue("link_instrumentos", linkInstrumentos);
        	
        	output2.setValue("nombre_estudio", estudios.getString("nombre_estudio"));
        	output2.setValue("numero_participantes", numero_participantes);
        	output2.setValue("completado", completado);
        	output2.setValue("porcentaje_completado", porcentaje_completado);
        	output2.setValue("incompleto", incompleto);
        	output2.setValue("porcentaje_incompleto", porcentaje_incompleto);
        	output2.setValue("sin_iniciar", sin_iniciar);
        	output2.setValue("porcentaje_sin_iniciar", porcentaje_sin_iniciar);
        }
        if (output.getRecordCount() > 0){
        	getRequest().setAttribute("flag", "0");
	        this.getSession().setAttribute("estudios.sql", output);
	        this.getSession().setAttribute("query2.sql", output2);
        }
        else{
        	getRequest().setAttribute("flag", "1");
        }
        //publish("estudios", output);
        this.getDb().commit();
        return 0;
    }
    
    String getPorcentajeIncompletoAnonimo (String idEstudio) throws Throwable{
    	if (getNumeroInstrumentosInt(idEstudio) != 0){
	    	String idInstrumento = getIdInstrumento(idEstudio);
	    	String sql = "select concat(round((select ((select (select count(*) from ajvieira_isurvey_lime.survey_"+idInstrumento+")" +
	    			" - (select count(*) from ajvieira_isurvey_lime.survey_"+idInstrumento+" " +
	    			"where submitdate)) * 100) / " +
	    			"((select count(*) from ajvieira_isurvey_lime.survey_"+idInstrumento+")))),'%') as porcentaje_incompleto";
	    	Recordset rs = this.getDb().get(sql);
	    	rs.first();
	    	return rs.getString("porcentaje_incompleto");
    	}
    	else
    		return "0";
    }
    
    String getIncompletoAnonimo (String idEstudio) throws Throwable{
    	if (getNumeroInstrumentosInt(idEstudio) != 0){
	    	String idInstrumento = getIdInstrumento(idEstudio);
	    	String sql = "select (select count(*) from ajvieira_isurvey_lime.survey_"+idInstrumento+") - " +
	    			"(select count(*) from ajvieira_isurvey_lime.survey_"+idInstrumento+" " +
	    			"where submitdate) as incompleto";
	    	Recordset rs = this.getDb().get(sql);
	    	rs.first();
	    	return rs.getString("incompleto");
    	}
    	else
    		return "0";
    }
    
    String getPorcentajeCompletadoAnonimo (String idEstudio) throws Throwable{
    	if (getNumeroInstrumentosInt(idEstudio) != 0){
	    	String idInstrumento = getIdInstrumento(idEstudio);
	    	String sql = "select concat(round((select ((select count(*) from ajvieira_isurvey_lime.survey_"+idInstrumento+" " +
	    			"where submitdate) * 100) / (select count(*) from ajvieira_isurvey_lime.survey_"+idInstrumento+"))),'%') as porcentaje_completado";
	    	Recordset rs = this.getDb().get(sql);
	    	rs.first();
	    	return rs.getString("porcentaje_completado");
    	}
    	else
    		return "0";
    }
    
    String getCompletadosAnonimo (String idEstudio) throws Throwable{
    	if (getNumeroInstrumentosInt(idEstudio) != 0){
	    	String idInstrumento = getIdInstrumento(idEstudio);
	    	String sql = "select count(*) as completado from ajvieira_isurvey_lime.survey_"+idInstrumento+" " +
	    			"where submitdate";
	    	Recordset rs = this.getDb().get(sql);
	    	rs.first();
	    	return rs.getString("completado");
    	}
    	else
    		return "0";
    }
    
    String getNumeroParticipantesAnonimo (String idEstudio) throws Throwable{
    	if (getNumeroInstrumentosInt(idEstudio) != 0){
	    	String idInstrumento = getIdInstrumento(idEstudio);
	    	String sql = "select count(*) as numero_participantes from ajvieira_isurvey_lime.survey_" + idInstrumento;
	    	Recordset rs = this.getDb().get(sql);
	    	rs.first();
	    	return rs.getString("numero_participantes");
    	}
    	else
    		return "0";
    }
    
    String getIdInstrumento (String idEstudio) throws Throwable{
    	String sql = "select id_instrumento from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("id_instrumento");
    }
    
    int getNumeroInstrumentosInt (String idEstudio) throws Throwable{
    	String sql = "select count(id_instrumento) as count from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getInt("count");
    }
    
    String getPorcentajeSinIniciarIdentificado (String idEstudio) throws Throwable{
    	if (getNumeroInstrumentosInt(idEstudio) != 0){
    	String sql = "select concat(round((select (("+getSinIniciarIdentificado(idEstudio)+")*100)/(select count(int_participante_instrumento.token_participante) + " +
    			"(select count(participante.id_estudio_identificado) " +
    			"from ajvieira_isurvey_app.participante " +
    			"where id_participante not in " +
    			"(select id_participante " +
    			"from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where (estatus = 'Completa' or estatus = 'Incompleta') " +
    			"and id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = "+idEstudio+"))) " +
    			"from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where (estatus = 'Completa' or estatus = 'Incompleta') " +
    			"and int_participante_instrumento.id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = "+idEstudio+")))),'%') as porcentaje_sin_iniciar";
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("porcentaje_sin_iniciar");
    	}
    	else
    		return "0%";
    }
    
    String getSinIniciarIdentificado (String idEstudio) throws Throwable{
    	if (getNumeroInstrumentosInt(idEstudio) != 0){
	    	String sql = "select count(participante.id_estudio_identificado) + 0 as sin_iniciar " +
	    			"from ajvieira_isurvey_app.participante " +
	    			"where id_participante not in " +
	    			"(select id_participante from ajvieira_isurvey_app.int_participante_instrumento " +
	    			"where (estatus = 'Completa' or estatus = 'Incompleta') " +
	    			"and id_instrumento in " +
	    			"(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = "+idEstudio+"))";
	    	Recordset rs = this.getDb().get(sql);
	    	rs.first();
	    	return rs.getString("sin_iniciar");
		}
    	else
    		return "0";
    }
    
    String getPorcentajeIncompletoIdentificado (String idEstudio) throws Throwable{
    	String sql = "select concat(round((select (((select count(int_participante_instrumento.id_participante) " +
    			"from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where int_participante_instrumento.id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = "+idEstudio+") and estatus = 'Incompleta') * 100) / (select count(int_participante_instrumento.token_participante) + " +
    			"(select count(participante.id_estudio_identificado) " +
    			"from ajvieira_isurvey_app.participante " +
    			"where id_participante not in " +
    			"(select id_participante " +
    			"from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where (estatus = 'Completa' or estatus = 'Incompleta') " +
    			"and id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = "+idEstudio+"))) " +
    			"from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where (estatus = 'Completa' or estatus = 'Incompleta') " +
    			"and int_participante_instrumento.id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = "+idEstudio+"))))),'%') as porcentaje_incompleto";
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("porcentaje_incompleto");
    }
    
    String getIncompletoIdentificado (String idEstudio) throws Throwable{
    	String sql = "select count(int_participante_instrumento.id_participante) as incompleto " +
    			"from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where int_participante_instrumento.id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = "+idEstudio+") and estatus = 'Incompleta'";
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("incompleto");
    }
    
    String getPorcentajeCompletadoIdentificado (String idEstudio) throws Throwable{
    	String sql = "select concat(round((select (((select count(int_participante_instrumento.id_participante) " +
    			"from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where int_participante_instrumento.id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = "+idEstudio+") " +
    			"and estatus = 'Completa') * 100) / (select count(int_participante_instrumento.token_participante) + (select count(participante.id_estudio_identificado) " +
    			"from ajvieira_isurvey_app.participante " +
    			"where id_participante not in (select id_participante from ajvieira_isurvey_app.int_participante_instrumento where estatus = 'Completa' " +
    			"and id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = "+idEstudio+"))) " +
    			"from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where (estatus = 'Completa' or estatus = 'Incompleta') " +
    			"and int_participante_instrumento.id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = "+idEstudio+"))))),'%') as porcentaje_completado";
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("porcentaje_completado");
    }
    
    String getCompletadosIdentificado (String idEstudio) throws Throwable{
    	String sql = "select count(int_participante_instrumento.id_participante) as completado " +
    			"from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where int_participante_instrumento.id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = "+idEstudio+") and estatus = 'Completa'";
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("completado");
    }
    
    String getNumeroParticipantesIdentificado (String idEstudio) throws Throwable{
    	if (getNumeroInstrumentosInt(idEstudio) != 0){
	    	String sql = "select count(int_participante_instrumento.token_participante) + " +
	    			"(select count(participante.id_estudio_identificado) " +
	    			"from ajvieira_isurvey_app.participante " +
	    			"where id_participante not in " +
	    			"(select id_participante " +
	    			"from ajvieira_isurvey_app.int_participante_instrumento " +
	    			"where (estatus = 'Completa' or estatus = 'Incompleta') " +
	    			"and id_instrumento in " +
	    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
	    			"where id_estudio = "+idEstudio+"))) as numero_participantes " +
	    			"from ajvieira_isurvey_app.int_participante_instrumento " +
	    			"where (estatus = 'Completa' or estatus = 'Incompleta') " +
	    			"and int_participante_instrumento.id_instrumento in " +
	    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
	    			"where id_estudio = "+idEstudio+")";
	    	Recordset rs = this.getDb().get(sql);
	    	rs.first();
	    	return rs.getString("numero_participantes");
    	}
    	else
    		return "0";
    }
    
    String getNumeroEncuestas (String idEstudio) throws Throwable{
    	String sql = "select count(instrumento.id_instrumento) as numero_encuestas " +
    			"from  ajvieira_isurvey_app.instrumento " +
    			"where instrumento.id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("numero_encuestas");
    }
    
    String getPorcentajeSinIniciarCerrado (String idEstudio) throws Throwable{
    	String sql = "select concat(round((select (("+getSinIniciarCerrado(idEstudio)+")*100)/("+getNumeroParticipantesCerrado(idEstudio)+"))),'%') as porcentaje_sin_iniciar";
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("porcentaje_sin_iniciar");
    }
    
    String getSinIniciarCerrado (String idEstudio) throws Throwable{
    	String sql = StringUtil.replace(getResource("query-incompletos-y-sin-iniciar.sql"), "{{id_estudio}}", idEstudio);
    	sql = StringUtil.replace(sql, "{{estatus}}", "Sin Iniciar");
    	Recordset rs = this.getDb().get(sql);
    	rs.top();
    	return String.valueOf(rs.getRecordCount());
    }
    
    String getPorcentajeIncompletoCerrado (String idEstudio) throws Throwable{
    	String sql = "select concat(round((select (("+getIncompletoCerrado(idEstudio)+")*100)/("+getNumeroParticipantesCerrado(idEstudio)+"))),'%') as porcentaje_incompleto";
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("porcentaje_incompleto");
    }
    
    String getIncompletoCerrado (String idEstudio) throws Throwable{
    	String sql = StringUtil.replace(getResource("query-incompletos-y-sin-iniciar.sql"), "{{id_estudio}}", idEstudio);
    	sql = StringUtil.replace(sql, "{{estatus}}", "Incompleta");
    	Recordset rs = this.getDb().get(sql);
    	rs.top();
    	return String.valueOf(rs.getRecordCount());
    }
    
    String getPorcentajeCompletadosCerrado (String idEstudio) throws Throwable{
    	String sql = "select concat(round((select ("+getCompletadosCerrado(idEstudio)+"*100)/"+getNumeroParticipantesCerrado(idEstudio)+")),'%') as porcentaje_completados";
    	Recordset porcentajeCompletados = this.getDb().get(sql);
    	porcentajeCompletados.first();
    	return porcentajeCompletados.getString("porcentaje_completados");
    }
    
    String getCompletadosCerrado (String idEstudio) throws Throwable{
    	String sql = "select floor(count(int_participante_instrumento.id_participante)/"+getNumeroInstrumentos(idEstudio)+") as completados " +
    			"from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where int_participante_instrumento.id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = "+idEstudio+") and estatus = 'Completa'";
    	Recordset completados = this.getDb().get(sql);
    	completados.first();
    	return completados.getString("completados");
    }
    
    String getNumeroParticipantesCerrado(String idEstudio) throws Throwable{
    	String sql = "select round((count(int_participante_instrumento.token_participante) + 0)/"+getNumeroInstrumentos(idEstudio)+") as numero_participantes " +
    			"from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where int_participante_instrumento.id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento " +
    			"where id_estudio = "+idEstudio+")";
    	Recordset numeroParticipantes =  this.getDb().get(sql);
    	numeroParticipantes.first();
    	return numeroParticipantes.getString("numero_participantes");
    }
    
    String getNumeroInstrumentos (String idEstudio) throws Throwable{
    	String sql = "select count(id_instrumento) as count from ajvieira_isurvey_app.instrumento where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("count");
    }
    
    Recordset getTokens(String idInstrumento) throws Throwable{
    	String sql = "select token_participante from ajvieira_isurvey_app.int_participante_instrumento where id_instrumento = " + idInstrumento;
    	return this.getDb().get(sql);
    }
       
    Recordset getEstudios(String idEmpresa) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.estudio where id_empresa = " + idEmpresa + " order by id_estudio desc";
    	return this.getDb().get(query);
    }
    
    Recordset getInstrumentos(String idEstudio) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.instrumento where id_estudio = " + idEstudio;
    	return this.getDb().get(query);
    }
    
    Recordset getParticipante (String token) throws Throwable{
    	String query = "select participante.id_participante, participante.nombre_participante, " +
    			"participante.apellido_participante, participante.email_participante, participante.empresa, " +
    			"participante.cargo, participante.supervisor, participante.fecha_nacimiento, " +
    			"participante.fecha_ingreso, participante.sexo, participante.tipo_nomina, participante.funcion " +
    			"from ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_instrumento, " +
    			"ajvieira_isurvey_app.instrumento, ajvieira_isurvey_app.estudio, " +
    			"ajvieira_isurvey_app.empresa " +
    			"where participante.id_participante = int_participante_instrumento.id_participante " +
    			"and int_participante_instrumento.id_instrumento = instrumento.id_instrumento " +
    			"and instrumento.id_estudio = estudio.id_estudio " +
    			"and estudio.id_empresa = empresa.id_empresa " +
    			"and empresa.id_empresa = participante.id_empresa " +
    			"and int_participante_instrumento.token_participante = '" + token + "'";
    	Recordset participante = this.getDb().get(query);
    	return participante;
    }
    
    void setEstatus (String token, String estatus, String porcentaje) throws Throwable{
    	String sql = "update ajvieira_isurvey_app.int_participante_instrumento set estatus = '" + estatus + "', " +
    			" porcentaje_completado = " + porcentaje + " where token_participante = '" + token + "'";
    	this.getDb().exec(sql);
    	
    }
    
    Recordset questionsOrdenadas (String idEncuesta) throws Throwable{
    	String sql = "select * from ajvieira_isurvey_lime.questions where parent_qid = 0 and sid = " + idEncuesta;
    	Recordset questions = this.getDb().get(sql);
    	questions.top();
    	questions.sort("question_order");
    	return questions;
    }
    
    Recordset getNombresColumnas (String nombreTabla) throws Throwable{
    	String sql = "SELECT `COLUMN_NAME` " +
    			"FROM `INFORMATION_SCHEMA`.`COLUMNS` " +
    			"WHERE `TABLE_SCHEMA`='ajvieira_isurvey_lime' " +
    			"AND `TABLE_NAME`='"+nombreTabla+"';";
    	return this.getDb().get(sql);
    }
    
    void updateStatus (String token) throws Throwable{
    	String estatus = "";
    	float porcentaje = 1;
    	int preguntasObligatorias = 0;
    	int preguntasRespondidas = 0;
    	String query = "select instrumento.id_instrumento " +
		"from " +
		"ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.instrumento, " +
		"ajvieira_isurvey_app.int_participante_instrumento " +
		"where " +
		"estudio.id_estudio = instrumento.id_estudio " +
		"and estudio.tipo = 'Cerrado' " +
		"and int_participante_instrumento.id_instrumento = instrumento.id_instrumento " +
		"and int_participante_instrumento.id_participante = (select id_participante from  " +
		"ajvieira_isurvey_app.int_participante_instrumento " +
		"where token_participante = '"+token+"') ";
    	Recordset instrumentos = this.getDb().get(query);
    	instrumentos.top();
    	Recordset participante = getParticipante(token);
    	participante.top();
    	while (participante.next()){
	    	while (instrumentos.next()){
	    		TokenGenerator tg = new TokenGenerator();
	    		String sql = "select * from ajvieira_isurvey_lime.survey_" + instrumentos.getString("id_instrumento") + " " +
	    				"where token = '" + tg.generarToken(participante.getString("id_participante"), instrumentos.getString("id_instrumento")) + "'";
	    		Recordset respuestas = this.getDb().get(sql);
	    		respuestas.top();
	    		Recordset columnas = getNombresColumnas("survey_" + instrumentos.getString("id_instrumento"));
	    		columnas.top();
	    		int numeroColumnas = columnas.getRecordCount() - 5;
	    		Recordset preguntas = questionsOrdenadas(instrumentos.getString("id_instrumento"));
	    		preguntas.top();
	    		while (preguntas.next()){
	    			if (preguntas.getString("mandatory").equals("Y"))
	    				preguntasObligatorias++;
	    		}

	    		while (respuestas.next()){
		    		if (respuestas.getString("submitdate") != null){
		    			estatus = "Completa";
		    			porcentaje = 100;
		    		}
		    		else{
		    			preguntas.first();
			    		for (int i = 1; i <= 5; i++){
			    			columnas.next();
			    		}
		    			int numeroColumnas2 = numeroColumnas;
			    		while (columnas.next()){
			    			String column = columnas.getString("column_name");
			    			column = column.toLowerCase();
			    			if (respuestas.getString(column) == null && (preguntas.getString("mandatory").equals("Y")) && (!column.equals("submitdate") && !column.equals("lastpage"))){
			    				estatus = "Incompleta";
			    			}
			    			if (respuestas.getString(column) != null && respuestas.getString(column) != "" && (preguntas.getString("mandatory").equals("Y")) && (!column.equals("submitdate") && !column.equals("lastpage"))){
			    				preguntasRespondidas++;
			    			}
			    			if (respuestas.getString(column) == null)
			    				numeroColumnas2--;
			    			preguntas.next();
			    		}
			    		if (numeroColumnas2 <= 0){
			    			estatus = "Sin Iniciar";
			    			porcentaje = 0;
			    		}
			    		else{ 
			    			estatus = "Incompleta";
			    			porcentaje = 1;
			    		}
		    		}
	    		}
	    		
	    		if (porcentaje != 0 && porcentaje != 100 && preguntasObligatorias != 0)
	    			porcentaje = (preguntasRespondidas*100)/preguntasObligatorias;

	    		if (preguntasObligatorias == 0)
	    			porcentaje = 100;
	    		setEstatus(tg.generarToken(participante.getString("id_participante"), instrumentos.getString("id_instrumento")), estatus, String.valueOf(porcentaje));
	    	}
    	}
    }
}