/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package responsable;

import tokens_participantes.*;
import dinamica.GenericTableManager;
import dinamica.GenericTransaction;
import dinamica.Recordset;
import dinamica.StringUtil;
import java.util.Map;
import java.util.Enumeration;

/**
 *
 * @author addSolutions
 */
public class BuscarParticipantesEstudioFiltro extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
    	String identificador = null;
    	String nombre = null;
    	String apellido = null;
    	String email = null;
    	String estatus = null;
        super.service(inputParams);
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idEstudio = ((String[]) parametros.get("id"))[0];
        try{
        	identificador = ((String[]) parametros.get("identificador"))[0];
        }catch(Exception e){
        	System.out.println("identificador");
        }
        try{
        	nombre = ((String[]) parametros.get("nombre"))[0];
        }catch(Exception e){
        	System.out.println("nombre");
        }
        try{
        	apellido = ((String[]) parametros.get("apellido"))[0];
        }catch(Exception e){
        	System.out.println("apellido");
        }
        try{
        	email = ((String[]) parametros.get("email"))[0];
        }catch(Exception e){
        	System.out.println("email");
        }
        try{
        	estatus = ((String[]) parametros.get("estatus"))[0];
        }catch(Exception e){
        	System.out.println("estatus");
        }
        int numeroInstrumentos = Integer.parseInt(getNumeroInstrumentos(idEstudio));
        Recordset participantes = getParticipantesDeEstudio(idEstudio, identificador);
        if (participantes.getRecordCount() > 0 && numeroInstrumentos > 0){
            Recordset output = new Recordset();
            output.append("identificador", java.sql.Types.INTEGER);
            output.append("nombre", java.sql.Types.VARCHAR);
            output.append("apellido", java.sql.Types.VARCHAR);
            output.append("email", java.sql.Types.VARCHAR);
            output.append("completado", java.sql.Types.VARCHAR);
            output.append("token", java.sql.Types.VARCHAR);
            output.append("completado2", java.sql.Types.INTEGER);
            participantes.top();
            while (participantes.next()){
            	String instrumentosCompletados = getInstrumentosCompletados(idEstudio, participantes.getString("id_participante"));
            	output.addNew();
            	output.setValue("identificador", Integer.parseInt(participantes.getString("id_participante")));
            	output.setValue("nombre", participantes.getString("nombre_participante"));
            	output.setValue("apellido", participantes.getString("apellido_participante"));
            	output.setValue("email", participantes.getString("email_participante"));
            	output.setValue("completado", instrumentosCompletados + "/" + numeroInstrumentos);
            	output.setValue("token", getToken(participantes.getString("id_participante"), getIdPrimerInstrumento(idEstudio)));
            	output.setValue("completado2", Integer.valueOf(instrumentosCompletados));
            }
            
            this.getSession().setAttribute("query-participantes.sql", output);
            System.out.println("return 0");
            return 0;
        }
        else{
        	System.out.println("return 0");
        	return 1;
        }
    }
    
    String getIdPrimerInstrumento(String idEstudio) throws Throwable{
    	String query = "select * from ajvieira_isurvey_app.instrumento where id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(query);
    	rs.first();
    	return rs.getString("id_instrumento");
    }
    
    String getToken(String idParticipante, String idInstrumento) throws Throwable{
    	TokenGenerator tg = new TokenGenerator();
    	return tg.generarToken(idParticipante, idInstrumento);
    }
    
    String getInstrumentosCompletados(String idEstudio, String idParticipante) throws Throwable{
    	String query = "select case (select count(estatus) " +
    			"from ajvieira_isurvey_app.int_participante_instrumento where id_participante " +
    			"= ipi.id_participante and id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = " + idEstudio +
    			" and estatus = 'Completa') " +
    			"group by id_participante) " +
    			"when 0 then " +
    			"0 " +
    			"when null then " +
    			"0 "+
    			"else " +
    			"(select count(estatus) from ajvieira_isurvey_app.int_participante_instrumento " +
    			"where estatus = 'Completa' and id_participante = ipi.id_participante and id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = " + idEstudio +
    			" and estatus = 'Completa')) " +
    			"end as completado " +
    			"from ajvieira_isurvey_app.int_participante_instrumento as ipi " +
    			"where id_instrumento in " +
    			"(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = "+idEstudio+") " +
    			"and id_participante = " + idParticipante +
    			" group by id_participante";
    	Recordset rs = this.getDb().get(query);
    	rs.first();
    	return rs.getString("completado");
    }
    
    Recordset getParticipantesDeEstudio (String idEstudio, String identificador) throws Throwable{
    	String query = "select participante.id_participante, participante.nombre_participante, participante.apellido_participante, " +
    			"participante.email_participante " +
    			"from ajvieira_isurvey_app.participante, " +
    			"ajvieira_isurvey_app.int_participante_lista_participantes, " +
    			"ajvieira_isurvey_app.lista_participantes, " +
    			"ajvieira_isurvey_app.int_lista_participantes_estudio, " +
    			"ajvieira_isurvey_app.estudio " +
    			"where " +
    			"participante.id_participante = int_participante_lista_participantes.id_participante " +
    			"and participante.id_empresa = int_participante_lista_participantes.id_empresa_participante " +
    			"and int_participante_lista_participantes.id_lista_participantes = lista_participantes.id_lista_participantes " +
    			"and lista_participantes.id_empresa = participante.id_empresa " +
    			"and lista_participantes.id_lista_participantes = int_lista_participantes_estudio.id_lista_participantes " +
    			"and int_lista_participantes_estudio.id_estudio = estudio.id_estudio " +
    			"and estudio.id_empresa = participante.id_empresa " +
    			"and estudio.id_estudio = " + idEstudio + " " +
    			"and concat(participante.id_participante) like " + identificador;
    	Recordset participantes = this.getDb().get(query);
    	return participantes;
    }
    
    String getNumeroInstrumentos (String idEstudio) throws Throwable{
    	String sql = "select count(instrumento.id_instrumento) as numero_encuestas " +
    			"from  ajvieira_isurvey_app.instrumento " +
    			"where instrumento.id_estudio = " + idEstudio;
    	Recordset rs = this.getDb().get(sql);
    	rs.first();
    	return rs.getString("numero_encuestas");
    }
}