package domain.reports.user;

import dinamica.*;

public class UserRoles extends GenericTransaction 
{

	public Recordset getRoles (Recordset rs) throws Throwable
	{
		String dsName = getContext().getInitParameter("def-datasource");
		String sql = getSQL(getResource("user_roles.sql"), rs); 
		return dbGet(dsName, sql);
	}
	
}
