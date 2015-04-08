/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crypt;

import dinamica.*;

/**
 *
 * @author Kate
 */
public class encuesta_instancia_cliente extends GenericTransaction {

    @Override
    public int service(Recordset inputParams) throws Throwable {

        super.service(inputParams);
        this.getDb().beginTrans();

        try {


            encuesta_crypto c = new encuesta_crypto();


            String id_encuesta = inputParams.getString("modeloid2");
            String nombre = inputParams.getString("nombreentrevistado2");
            String apellido = inputParams.getString("apellidoentrevistado2");
            String cedula = inputParams.getString("cedula2");
            String correo = inputParams.getString("correo2"); //correo entrevistado
            String nombres = inputParams.getString("nombre_supervisor2");
            String correos = inputParams.getString("correos2");//correo supervisor
            String clasificacion = inputParams.getString("clasificacion2");
            String desempeno1 = inputParams.getString("desempeno1");
            String desempeno2 = inputParams.getString("desempeno2");




            Recordset rs1 = getRecordset("empresa.sql"); // empresa de usuario logeado
            rs1.first();
            String id_empresa = rs1.getString("id_empresa");


            String llave = id_empresa + rs1.getString("nombre_empresa") + id_encuesta;




            Recordset rs = getRecordset("email.sql"); // email de usuario logeado
            rs.first();
            String emaila = rs.getString("email");

            String datos = "";

            if (correo == null) {
                correo = "";
            }
            if (nombres == null) {
                nombres = "";
            }
            if (correos == null) {
                correos = "";
            }

            if (nombre == null) {
                nombre = "An\u00f3nimo";
            } else {
                datos = datos + nombre;
            }

            if (apellido == null) {
                apellido = "An\u00f3nimo";
            } else {
                datos = datos + apellido;
            }

            if (cedula == null) {
                cedula = "";
            } else {
                datos = datos + cedula;
            }

            if (desempeno2 == null) {
                desempeno2 = "";
            } else {
                desempeno2 = desempeno2.toUpperCase();
                desempeno2 = desempeno2.trim();
            }

            java.util.Date date = new java.util.Date();
            String fecha = Long.toString(date.getTime());
            System.out.println("FECHA: " + fecha);
            String texto = "";

            if (!datos.isEmpty()) {
                texto = datos + fecha;
            } else {
                texto = "anonimo" + fecha;
            }

            texto.toUpperCase().replaceAll("Ñ", "N").
                    replaceAll("Á", "A").replaceAll("É", "E").replaceAll("Í", "I").
                    replaceAll("Ó", "O").replaceAll("Ú", "U").replaceAll(" ", "");


            String codigo = new encuesta_crypto().encrypt(llave, texto).replace('+', '1');
            codigo = codigo.replace('=', '7');
           System.out.println("LLAVE " + llave + " TEXTO: " + texto + " CODIGO: " + codigo);




            String iInstancia = getResource("insert.sql");
            iInstancia = StringUtil.replace(iInstancia, "${codigo}", codigo);
            iInstancia = StringUtil.replace(iInstancia, "${modeloid2}", id_encuesta);
            iInstancia = StringUtil.replace(iInstancia, "${empresaid2}", id_empresa);
            iInstancia = StringUtil.replace(iInstancia, "${nombreentrevistado2}", nombre);
            iInstancia = StringUtil.replace(iInstancia, "${apellidoentrevistado2}", apellido);
            iInstancia = StringUtil.replace(iInstancia, "${correo2}", correo);
            iInstancia = StringUtil.replace(iInstancia, "${correos2}", correos);
            iInstancia = StringUtil.replace(iInstancia, "${cedula2}", cedula);
            iInstancia = StringUtil.replace(iInstancia, "${nombre_supervisor2}", nombres);
            iInstancia = StringUtil.replace(iInstancia, "${correoa2}", emaila);
            iInstancia = StringUtil.replace(iInstancia, "${clasificacion2}", clasificacion);
            iInstancia = StringUtil.replace(iInstancia, "${desempeno1}", desempeno1);
            iInstancia = StringUtil.replace(iInstancia, "${desempeno2}", desempeno2);

            getDb().exec(iInstancia);
            this.getDb().commit();

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return 0;
    }
}
