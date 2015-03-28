package domain.utils;

import dinamica.*;

/**
 * Registra en la session los valores de los parametros
 * de un validator.<br><br>
 * Fecha de creacion: 2011-07-18<br>
 * Fecha de actualizacion: 2011-07-18<br> 
 * @author Martin Cordova y Asociados C.A
 */
public class SessionAttribute extends GenericTransaction {

	@Override
	public int service(Recordset inputParams) throws Throwable {
		
		Recordset rsMD = inputParams.getMetaData();
		rsMD.top();
		while(rsMD.next()) {
			if(!inputParams.isNull(rsMD.getString("name")))
				getSession().setAttribute(rsMD.getString("name"), inputParams.getValue(rsMD.getString("name")));
		}
		
		return super.service(inputParams);
	}

}
