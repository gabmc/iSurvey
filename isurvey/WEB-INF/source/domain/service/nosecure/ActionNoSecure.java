package domain.service.nosecure;

import dinamica.*;
import dinamica.xml.*;

/**
 * Leer la data de los servicios en la BD de seguridad 
 * y comparar con el recordset que contiene toda las ruta de los servicios
 * de una determinada aplicacion, este modulo publica un
 * recordset con aquellos action o servicios que no estan 
 * en la base de datos de seguridad tabla "s_service". A su vez
 * descarta actions, por ser necesarios a la hora de hacer login, esta
 * configuracion es leida del config.xml.
 * Fecha de creacion: 2008-08-07<br>
 * (c) 2008 Martin Cordova y Asociados C.A<br>
 * http://www.martincordova.com<br>
 * @author Francisco Galizia (galiziafrancisco@gmail.com)
 */
public class ActionNoSecure extends GenericTransaction
{

	/* (non-Javadoc)
	 * @see dinamica.GenericTransaction#service(dinamica.Recordset)
	 */
	@Override
	public int service(Recordset inputParams) throws Throwable
	{
		//reutilizar la logica del padre
		super.service(inputParams);
		
		//obtener el documento (config.xml)
		Document docXML = getConfig().getDocument();
		
		//obtener los elementos
        Element actionDescart[] = docXML.getElements("//action-descartado");
		
        //obtener array de los parametros del request
        String pathAlias[] = StringUtil.split(inputParams.getString("path_alias"), ",");
        
        //template del SQL de los servicios en BD para la aplicacion
        String sql = StringUtil.replace(getResource("query.sql"), "${alias}",pathAlias[1]);
        String sqlApp = StringUtil.replace(getResource("app.sql"), "${alias}",pathAlias[1]);        
        
		//obtener recordset con todos los servicios seguros de la aplicacion    
		Recordset rs = getDb().get(getSQL(sql, inputParams));
		Recordset rsApp = getDb().get(getSQL(sqlApp, inputParams));
		
		//obtener todas las acciones de la aplicacion, que luego seran comparada
		//con la data de los servicios de la base de datos
		ActionDocGen doc = new ActionDocGen(pathAlias[0]);
		
		//recordset que contiene todos los actions de la aplicacion
		Recordset rs2 = doc.getActions();
		
		//recordset que contendra los servicios no seguros
		//este recordset sera guardado en sesion y se podra generar reportes y 
		//script de base de datos
		Recordset rs3 = new Recordset();
				
		//copiar la estructura del recordset de todos los action
		//al recordset que contendra los action no seguros
		rs3 = rs2.copyStructure();
		rs3.append("app_id", java.sql.Types.INTEGER);
		
		//recorrer recordset para hacer las comparaciones
		rs2.top();
		while(rs2.next()){
			if(rs.findRecord("path", rs2.getString("path")) == -1){
				//flag que determinara si un action esta en el
				//config.xml y por lo tanto no debe ser añadido
				//al recordset de action no seguros
				int flag = 0;
				//recorrer elementos del config.xml
				for (int i = 0; i < actionDescart.length; i++) 
		        {   	
					//obtener un elemento
		        	Element action = actionDescart[i];
		        	//comparar atributo con el campo del recorser añadir valor
		        	//al flag si consuerdan
		        	if(action.getAttribute("action").equals(rs2.getString("path"))){
		        		flag = 1;
		        		//salir del bucle
		        		break;
		        	}
		        }
				//preguntar si flag no cambio de valor, en caso
				//contrario no se añade el registro al recordset
				//ya que el action esta descartado como inseguro
				if (flag == 0){
					//añadir un nuevo record al recordset que contiene los
					//actions no seguros
					rsApp.first();
					rs3.addNew();
					rs3.setValue("path", rs2.getString("path"));
					rs3.setValue("summary", rs2.getString("summary"));
					rs3.setValue("tx-enabled", rs2.getString("tx-enabled"));
					rs3.setValue("dataclass", rs2.getString("dataclass"));
					rs3.setValue("outputclass", rs2.getString("outputclass"));
					rs3.setValue("log", rs2.getString("log"));
					rs3.setValue("jdbclog", rs2.getString("jdbclog"));
					rs3.setValue("app_id", rsApp.getInteger("app_id"));
				}
			}	
		}
		//el recordset tiene registros?
		if(rs3.getRecordCount() > 0){
			Recordset rsSQL = new Recordset();
			rsSQL.append("script", java.sql.Types.VARCHAR);
			rsSQL.addNew();
			rsSQL.setValue("script", getSQL(getResource("script.sql"), inputParams));
			//grabar en sesion el recordset
			getSession().setAttribute("query.sql", rs3);
			getSession().setAttribute("script", rsSQL);
			return 0;
		}else{
			return 1;
		}
		
	}

}
