package domain.service.nosecure;

import java.io.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import dinamica.*;
import dinamica.xml.Document;
import dinamica.xml.Element;

/**
 * Recorre el directorio configurado en el web.xml, para obtener la lista
 * de los contexto asociados al contenedor, a su vez obtiene los web.xml
 * de cada contexto los parsea para obtener los valores del datasource y alias, 
 * donde el datasource tiene que ser igual al datasource de la aplicacion de seguridad
 * (Solo se obtendra aquellas aplicaciones que posean el mismo datasource).
 * Fecha de creacion: 2008-08-02<br>
 * Fecha de actualizacion: 2010-06-28<br>
 * (c) 2008 Martin Cordova y Asociados C.A<br>
 * http://www.martincordova.com<br>
 * @author Francisco Galizia (galiziafrancisco@gmail.com)
 */
public class GetAplication extends GenericTransaction implements EntityResolver
{

	 @Override
     public InputSource resolveEntity(String arg0, String arg1)
                     throws SAXException, IOException {
             InputSource s =  new InputSource(getClass().getResourceAsStream("/javax/servlet/resources/web-app_2_3.dtd"));
             s.setPublicId(arg0);
             s.setSystemId("javax.servlet.resources.web-app_2_3.dtd");
             return s;
     }
	 
	/* (non-Javadoc)
	 * @see dinamica.GenericTransaction#service(dinamica.Recordset)
	 */
	@Override
	public int service(Recordset inputParams) throws Throwable
	{
		//reutilizar la logica del padre
		super.service(inputParams);
		
		if(getContext().getInitParameter("base-dir") == null || getContext().getInitParameter("base-dir").equals(""))
			throw new Throwable("Parámetro [base-dir] no encontrado en el web.xml");
		
		//datasource de la aplicacion de seguridad
		String dataSourceAdmin = getContext().getInitParameter("def-datasource");
		
		//recordset que contendra la ruta, nombre y alias
		//las aplicaciones del contenedor
		Recordset rs = new Recordset();
		rs.append("path-app", java.sql.Types.VARCHAR);
		rs.append("name-app", java.sql.Types.VARCHAR);
		rs.append("alias-app", java.sql.Types.VARCHAR);
		
		//obtener el archivo con la ruta configura en el web.xml
		File fsource = new File(getContext().getInitParameter("base-dir"));
		
		//si no existe arrojar una excepcion
		if (!fsource.exists())
			throw new Throwable("Directorio invalido: " + getContext().getInitParameter("base-dir"));
		
		//obtener la lista de directorios
		String items[] = fsource.list();
		
		//recorrer cada archivo y obtener la ruta del contexto
		for (int i=0;i<items.length;i++)
		{
			//contiene la ruta completa de la aplicacion o contexto
			String pathApp = fsource.getPath() + File.separator + items[i] + File.separator + "WEB-INF" + File.separator + "action";
			
			//contiene la ruta completa del web.xml de la aplicacion o contexto
			String pathAppWebXML = fsource.getPath() + File.separator + items[i] + File.separator + "WEB-INF" + File.separator + "web.xml";
			
			//nombre de la aplicacion
			String nameApp = items[i];
			
			File f = new File(pathAppWebXML);
			//verificar que el archivo web.xml existe
			if (f.isFile()) {
				//obtener archivo web.xml de la aplicacion
				FileInputStream  webXML = new FileInputStream(f);
				
				//obtener el documento
				Document doc = new Document(webXML,this);
				
				//obtener el array de elementos param-value del web.xml
				Element initParams[] = doc.getElements("filter[filter-name = 'SecurityFilter']/init-param/param-value");
							
				//si el array no tiene elemento significa que el
				//el filtro de seguridad esta comentado y por lo 
				//tanto es descartado
				if(initParams.length > 0){
					
					//obtener la posicion numero 1 del array ya que esta
					//es la que contiene el nombre del datasource
					Element paramValueDS = initParams[1];
		
					//el valor del elemento param-value corresponde con el datasource
					//de la aplicacion de seguridad?
					if(paramValueDS.getValue().equals(dataSourceAdmin)){
						
						//obtener el valor de la posicion numero 0 del
						//array la cual corresponde con el alias de la
						//aplicacion
						Element paramValueAlias = initParams[0];
						
						//añadir un nuevo record al recordset
						rs.addNew();
						rs.setValue("path-app", pathApp);
						rs.setValue("name-app", nameApp);
						rs.setValue("alias-app", paramValueAlias.getString());
					}		
				}
			}
		}
		
		//publicar el recordset
		publish("aplicaciones", rs);
		
		return 0;
	}
}
