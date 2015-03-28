package domain.loginconsulta;

import dinamica.*;

/**
 * Motor de busqueda, construye un SQL condicionalmente
 * de acuerdo a los parametros recibidos, ejecuta el query
 * y retorna 0 o 1 dependiendo de si el recordset tiene o no
 * registros. Esta clase dejara el recordset en sesion para que
 * pueda mostrarse en una vista paginada.
 * <br><br>
 * Actualizado: 2007-06-27<br>
 * Framework Dinamica - Distribuido bajo licencia LGPL<br>
 * @author mcordova (martin.cordova@gmail.com)
 * */
public class Search extends GenericTransaction
{

	//define el ID del recordset a publicar
	String _rsName = "query.sql";
	
	@Override
	public int service(Recordset inputs) throws Throwable
	{

		//reutiliza la logica de la clase padre
		int rc = super.service(inputs);
		
		/* ensamblar query */
		
		//carga el template base del query
		String qBase = getResource("query-base.sql");

		//aqui se almacenaran las condiciones del WHERE
		StringBuffer qFilter = new StringBuffer();
		
		//logica generica para generar las clausulas 
		//all the applicable clauses
		String[] params = {
							"user_id",
							"fdesde",
							"fhasta"
						  };
		
		for (int i=0;i<params.length;i++)
		{
			if (inputs.getValue(params[i])!=null)
				qFilter.append(getResource("clause-" + params[i]+ ".sql"));
		}

		//ya tenemos la lista de condiciones
		String where = qFilter.toString();
		
		//ahora reemplaza las condiciones en el query base
		qBase = StringUtil.replace(qBase,"${filter}", where);

		/* listo el query - quedo ensamblado */
		
		//ahora reemplaza los valores de los parametros en el query
		String sql = getSQL(qBase, inputs);
		
		//ejecutar query y crear recordset
		Recordset rs = getDb().get(sql);
		
		//retorno data?
		if (rs.getRecordCount()>0)
		{
			//publicar recordset
			getSession().setAttribute(_rsName, rs);
			rc = 0;
		}
		else
		{
			rc = 1;
		}
		
		return rc;
		
	}

}
