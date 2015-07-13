/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import dinamica.GenericTransaction;
import dinamica.Recordset;

/**
 *
 * @author Gary
 */
public class RedirectWelcome  extends GenericTransaction {

   @Override
   public int service(Recordset inputParams) throws Throwable {

       super.service(inputParams);
       if (this.isUserInRole("Administrador add")) 
           getRequest().setAttribute("ruta", "action/security/home/welcome/admin_add");
       
       if (this.isUserInRole("Administrador de Seguridad")) 
           getRequest().setAttribute("ruta", "action/security/home/welcome/admin_seguridad");

       if (this.isUserInRole("Administrador Funcional")) 
           getRequest().setAttribute("ruta", "action/security/home/welcome/admin_funcional");
   
       if (this.isUserInRole("Analista")) 
           getRequest().setAttribute("ruta", "action/security/home/welcome/analista");

       if (this.isUserInRole("Lider")) 
           getRequest().setAttribute("ruta", "action/security/home/welcome/lider");

       if (this.isUserInRole("Responsable Proceso Medicion")) 
           getRequest().setAttribute("ruta", "action/security/home/welcome/responsable");     
       
       if (this.isUserInRole("Aplicador")) 
           getRequest().setAttribute("ruta", "action/security/home/welcome/aplicador");  
       
       return 0;
   }
}

