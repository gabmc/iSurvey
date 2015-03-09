package domain.menu;

import dinamica.Recordset;
import dinamica.xml.Document;
import dinamica.xml.Element;
import domain.admin.InsertDetail;

/**
 * Clase que ejecuta un recordset despues que ha
 * se ha realizado la transaccion este recordset
 * es publicado<br><br>
 * Fecha de creacion: 2011-08-21<br>
 * Fecha de actualizacion: 2011-08-21<br>
 * @author Martin Cordova y Asociados C.A
 */
public class MenuTransaction extends dinamica.audit.AuditableTransactionIdentity {

	@Override
	public int service(Recordset inputParams) throws Throwable {
		
		int rc = super.service(inputParams);
		
		//read config parameters
		String colName = getConfig().getConfigValue("colname");
		String sqlFile = getConfig().getConfigValue("sql-template");
		
		Document doc = getConfig().getDocument();
		Element e = doc.getElement("sql-template");
		String params = e.getAttribute("params");
		
		//pre-fill sql template with master record values
		String sql = getSQL(getResource(sqlFile), inputParams);
		
		if (params!=null){
			Recordset rs = getRecordset(params);
			if (rs.getRecordCount() > 0) {
				rs.first();
				sql = getSQL(sql, rs);
			}
		}

		//insert detail records using reusable object
		InsertDetail r = (InsertDetail)getObject("domain.admin.InsertDetail");
		r.save(sql, colName);
		
		String sqlAudit = "query1.sql";
		if(!inputParams.isNull("master_menu_id"))
			sqlAudit = "query2.sql";
		
		if (sqlAudit!=null) {
			if (!sqlAudit.equals("")) {
				Recordset rs = getDb().get(getSQL(getResource(sqlAudit), inputParams));
				publish("query.sql", rs);
			}
		}
				
		return rc;
	}

}
