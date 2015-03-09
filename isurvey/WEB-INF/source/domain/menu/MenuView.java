package domain.menu;

import dinamica.*;

/**
 * Ejecuta un query o otro para mostrar los item del menu,
 * dependiendo de si el recordset del request contiene el ID del menu item
 * o no.<br>
 * Fecha de creacion: 2011-08-20<br>
 * Fecha de actualizacion: 2011-08-20<br>
 * @author Martin Cordova y Asociados C.A
 */
public class MenuView extends GenericTransaction {

	@Override
	public int service(Recordset inputParams) throws Throwable {
		
		Recordset rsMD = inputParams.getMetaData();
		rsMD.top();
		while(rsMD.next()) {
			if(!inputParams.isNull(rsMD.getString("name")))
				getSession().setAttribute(rsMD.getString("name"), inputParams.getValue(rsMD.getString("name")));
		}
		
		int rc = super.service(inputParams);
		
		Recordset rs1 = new Recordset();
		
		if(inputParams.isNull("menu_id")) {
			rs1 = getDb().get(getSQL(getResource("query1.sql"),inputParams));
		} else {
			rs1 = getDb().get(getSQL(getResource("query2.sql"),inputParams));
		}
		
		publish("query.sql", rs1);
		
		return rc;
	}
	
}
