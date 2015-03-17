/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpc;

import dinamica.GenericTransaction;
import dinamica.Recordset;

/**
 *
 * @author addSolutions
 */
public class Conexion extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable{
        super.service(inputParams);
        RpcConn rpc = new RpcConn();
        String llave;
        Recordset sessionKey = new Recordset();
        sessionKey.append("html", java.sql.Types.VARCHAR);
        sessionKey.addNew();
        llave = "<tr><td>" + rpc.getSessionKeyRPC("admin", "1q2w3e", 1) + "</td></tr>";
        //llave = llave + "<tr><td>" + rpc.addSurvey(key, 555, "prueba survey", "es", "S", 1);
        llave = llave + "<tr><td>" + rpc.listSurveys(rpc.getSessionKey(), "admin", 5);
        //llave = llave + "<tr><td>" + rpc.listUsers(rpc.getSessionKey(), 1);
        sessionKey.setValue("html", llave);
        this.publish("sessionKey", sessionKey);
        rpc.releaseSessionKey(rpc.getSessionKey(), 1);
        return 0;
    }
}
