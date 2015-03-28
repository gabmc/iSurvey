package domain.security.ldap;

import dinamica.*;
import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * Modulo que verifica si dado un userlogin, este existe en el directorio LDAP.
 * Para ello necesita conectarse al LDAP mendiantes unos parametros que se
 * encuentran configurados en el web.xml del contexto. Si encuentra el userlogin
 * añade a un recordset los datos basicos de la persona, en caso contrario cambia el 
 * valor de una variable de control que indica si se encontro el userlogin o no.
 * El recordset es publicado de para que sea consumido por la salida.<br>
 * Fecha de creacion: 2008-09-01<br>
 * Fecha de actualizacion: 2008-09-04<br>
 * @author Martin Cordova y Asociados C.A (martin.cordova@gmail.com)
 */
public class GetUser extends GenericTransaction 
{
	@Override
	public int service(Recordset inputParams) throws Throwable 
	{
		//ejecutar la logica del padre
		super.service(inputParams);

		//obtener parametros del web.xml con la configuracion para la conexion con LDAP
		String url = getContext().getInitParameter("ldap-url");
		String authentication = getContext().getInitParameter("ldap-authentication");
		String login = getContext().getInitParameter("ldap-login");
		String password = getContext().getInitParameter("ldap-pass");
		
		DirContext ctx = null;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("com.sun.jndi.ldap.connect.pool", "true"); 
	    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	    env.put(Context.PROVIDER_URL, url);
	    env.put(Context.SECURITY_AUTHENTICATION, authentication);
	    env.put(Context.SECURITY_PRINCIPAL, login);
	    env.put(Context.SECURITY_CREDENTIALS, password);

	    try {
			
	    	ctx = new InitialDirContext(env);
	    	
	    	//obtener parametros del web.xml para poder encontrar un usuario en el directorio LDAP
	    	String searchURL = getContext().getInitParameter("ldap-search-url");
	    	String searchFilter = getContext().getInitParameter("ldap-search-filter");
	    	searchFilter = StringUtil.replace(searchFilter, "${userlogin}", inputParams.getString("userlogin"));
	    	
	    	//Recordset que contendra los datos del usuario
	    	Recordset rs = new Recordset();
	    	rs.append("lname", java.sql.Types.VARCHAR);
	    	rs.append("fname", java.sql.Types.VARCHAR);
	    	rs.append("email", java.sql.Types.VARCHAR);
	    	rs.append("found", java.sql.Types.VARCHAR);
	    	rs.append("dn", java.sql.Types.VARCHAR);
	    	rs.addNew();
	    	//valor que control que permite saber si un usuario existe en el directorio LDAP
	    	rs.setValue("found", "N");
	    	
            SearchControls sc = new SearchControls();
	    	sc.setSearchScope(SearchControls.SUBTREE_SCOPE);	    	
	    	
			NamingEnumeration<?> answer = ctx.search(searchURL, searchFilter, sc);
			while (answer.hasMoreElements()) {
				rs.setValue("found", "Y");
				SearchResult searchResult = (SearchResult) answer.next();
	            Attributes attributes = searchResult.getAttributes();
	            
	            //Armar un array con los nombres y apellidos del usuario
	            String attrName = getConfig().getConfigValue("ldap-attribute");
	            String name[] = StringUtil.split(String.valueOf(attributes.get(attrName).get()), " ");
	            
	            //variables que contendran los nombres y apellidos
	            String lname = null;
	            String fname = null;
	            
	            if(name.length == 1){
	            	fname = name[0].trim();
	            }
	            
	            if(name.length == 2){
	            	lname = name[1].trim();
	            	fname = name[0].trim();
	            }
	            
	            if(name.length == 3){
	            	lname = name[1].trim() + " " + name[2].trim();
	            	fname = name[0].trim();
	            }
	            
	            if(name.length == 4){
	            	lname = name[2].trim() + " " + name[3].trim();
	            	fname = name[0].trim() + " " + name[1].trim();
	            }
	            
	            //eliminar puntos y coma del nombre
	            lname = StringUtil.replace(lname, ",", "");
	            lname = StringUtil.replace(lname, ".", "");
	            lname = lname.trim();
	            
	            //eliminar puntos y coma del nombre
	            fname = StringUtil.replace(fname, ",", "");
	            fname = StringUtil.replace(fname, ".", "");
	            fname = fname.trim();
	            
	            //añadir valores al recordset
	            rs.setValue("lname", lname);
	            rs.setValue("fname", fname);
	            rs.setValue("dn", searchResult.getNameInNamespace());
	            
	            Attribute a = attributes.get("mail");
	            if (a!=null)
	            	rs.setValue("email", a.get());
			}	    	
	    	//publicar el recordset
			publish("ldap", rs);
			return 0;
				
		} catch (NamingException e) {
			throw (e);
		} finally {
			if (ctx!=null)
				try {
					ctx.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
		}
	
	}
	
}


