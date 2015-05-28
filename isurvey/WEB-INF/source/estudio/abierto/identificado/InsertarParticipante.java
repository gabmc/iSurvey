/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package estudio.abierto.identificado;

import dinamica.GenericTableManager;
import dinamica.Recordset;
import dinamica.StringUtil;
import java.util.Enumeration;
import java.util.Map;


import tokens_participantes.TokenGenerator;

public class InsertarParticipante extends GenericTableManager {

    @Override
    public int service(Recordset inputParams) throws Throwable {
        this.getDb().beginTrans();
        Enumeration names = this.getRequest().getParameterNames();
        Map parametros = this.getRequest().getParameterMap();
        String idParticipante = ((String[]) parametros.get("id_participante"))[0];
        String idInstrumento = ((String[]) parametros.get("id_instrumento"))[0];
        
        TokenGenerator tg = new TokenGenerator();
        String token = tg.generarToken(idParticipante, idInstrumento);
        
        String sql = StringUtil.replace(getResource("insert.sql"), "{{id_participante}}", idParticipante);
        sql = StringUtil.replace(sql, "{{nombre_participante}}", ((String[]) parametros.get("nombre_participante"))[0]);
        sql = StringUtil.replace(sql, "{{apellido_participante}}", ((String[]) parametros.get("apellido_participante"))[0]);
        sql = StringUtil.replace(sql, "{{email_participante}}", ((String[]) parametros.get("email_participante"))[0]);
        sql = StringUtil.replace(sql, "{{empresa}}", ((String[]) parametros.get("empresa"))[0]);
        sql = StringUtil.replace(sql, "{{cargo}}", ((String[]) parametros.get("cargo"))[0]);
        sql = StringUtil.replace(sql, "{{id_empresa}}", ((String[]) parametros.get("id_empresa"))[0]);
        this.getDb().exec(sql);
        
        sql = StringUtil.replace(getResource("insert-lime.sql"), "{{firstname}}", ((String[]) parametros.get("nombre_participante"))[0]);
        sql = StringUtil.replace(sql, "{{lastname}}", ((String[]) parametros.get("apellido_participante"))[0]);
        sql = StringUtil.replace(sql, "{{email}}", ((String[]) parametros.get("email_participante"))[0]);
        sql = StringUtil.replace(sql, "{{token}}", token);
        sql = StringUtil.replace(sql, "{{id_encuesta}}", idInstrumento);
        this.getDb().exec(sql);
        
        sql = StringUtil.replace(getResource("insert-lime-respuestas.sql"), "{{token}}", token);
        sql = StringUtil.replace(sql, "{{id_encuesta}}", idInstrumento);
        this.getDb().exec(sql);
        
        sql = StringUtil.replace(getResource("insert-int-participante-instrumento.sql"), "{{token_participante}}", token);
        sql = StringUtil.replace(sql, "{{id_participante}}", idParticipante);
        sql = StringUtil.replace(sql, "{{id_instrumento}}", idInstrumento);
        
        this.getDb().exec(sql);

        return super.service(inputParams);
    }
    
    

}
