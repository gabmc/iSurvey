package domain;

import dinamica.*;

public class DeleteRecord extends dinamica.audit.AuditableTransaction 
{

	public int service(Recordset inputParams) throws Throwable 
	{
		Recordset rsMD = inputParams.getMetaData();
		rsMD.top();
		while(rsMD.next()) {
			if(!inputParams.isNull(rsMD.getString("name")))
				getSession().setAttribute(rsMD.getString("name"), inputParams.getValue(rsMD.getString("name")));
		}
		
		String rsID  = getConfig().getConfigValue("recordset-id");
		String pkey  = getConfig().getConfigValue("pkey");
		Recordset rs = (Recordset)getSession().getAttribute(rsID);
		int id = inputParams.getInt("id");
		int pos = rs.findRecord(pkey, id);
		if (pos < 0)
			throw new Throwable("Can't find record with column " + pkey + "=" + id);
		else
			rs.delete(pos);

		super.service(inputParams);
		
		return 0;
	}
	
}
