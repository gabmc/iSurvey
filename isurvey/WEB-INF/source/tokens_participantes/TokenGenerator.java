/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tokens_participantes;

public class TokenGenerator{

    public String generarToken(String idParticipante, String idInstrumento) throws Throwable {

        try {

            String llave = "iSurvey";

            java.util.Date date = new java.util.Date();
            String fecha = Long.toString(date.getTime());
            String mensaje = idParticipante + idInstrumento;

            String token = new encriptador().encrypt(llave, mensaje).replace('+', '1');
            token = token.replace('=', '2');
            token = token.replace('/', '3');
           //System.out.println("LLAVE " + llave + " TEXTO: " + mensaje + " CODIGO: " + token);

           	return token;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    return null;

    }
}
