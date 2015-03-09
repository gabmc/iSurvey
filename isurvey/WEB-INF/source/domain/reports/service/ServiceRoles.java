package domain.reports.service;

import dinamica.*;

public class ServiceRoles extends GenericTransaction 
{

	public Recordset getRoles (Recordset rs) throws Throwable
	{
		String dsName = getContext().getInitParameter("def-datasource");
		String sql = getSQL(getResource("service_roles.sql"), rs); 
		return dbGet(dsName, sql);
	}
	
}
