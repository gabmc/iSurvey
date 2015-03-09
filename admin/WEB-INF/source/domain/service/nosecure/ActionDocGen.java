package domain.service.nosecure;
import dinamica.*;
import java.io.*;

/**
 * ActionDocGen<br>
 * Generates documentation for every Action
 * under /web-inf/action for a given application.
 * <br><br>
 * Creation date: 05/08/2004<br>
 * (c) 2004 Martin Cordova y Asociados<br>
 * http://www.martincordova.com<br>
 * @author Martin Cordova dinamica@martincordova.com
 */
public class ActionDocGen
{

	String rootPath = null;
	Recordset action = new Recordset();

	public ActionDocGen(String path) throws Throwable
	{
		rootPath = path;
		action.append("path", java.sql.Types.VARCHAR);
		action.append("dataclass", java.sql.Types.VARCHAR);
		action.append("outputclass", java.sql.Types.VARCHAR);
		action.append("log", java.sql.Types.VARCHAR);
		action.append("jdbclog", java.sql.Types.VARCHAR);
		action.append("tx-enabled", java.sql.Types.VARCHAR);
		action.append("summary", java.sql.Types.VARCHAR);
		
		File f = new File(rootPath);
		if (!f.exists())
			throw new Throwable("Path not found - Invalid Action path: " + rootPath);
		getData(f);
		
	}

	void getData(File f) throws Throwable
	{
		String items[] = f.list();
		
		//System.out.println(f.getPath() + " -> " + items.length + " items.");
		
		for (int i=0;i<items.length;i++)
		{
			
			String fname = f.getPath() + File.separator + items[i];
			
			File item = new File(fname);
			if (item.isDirectory())
			{
				getData(item);
			}
			else
			{
				if (item.getName().equals("config.xml"))
				{
					
					Config c = null;
					
					try {
						c = new Config(getFile(item), item.getParent());
					} catch (Throwable e) {
						System.out.println("El config.xml del siguiente action: [" + item + "] esta corrupto.");
						e.printStackTrace();
					}
					String path = c.path.substring(c.path.lastIndexOf("WEB-INF") + "WEB-INF".length());
					path = StringUtil.replace(path, "\\", "/");
					action.addNew();
					action.setValue("path", path);
					action.setValue("summary", c.summary);
					action.setValue("tx-enabled", c.transTransactions);
					action.setValue("dataclass", c.transClassName);
					action.setValue("outputclass", c.outClassName);
					action.setValue("log", c.mvcLog);
					action.setValue("jdbclog", c.jdbcLog);
				}
			}
			
		}
		
	}

	String getFile(File f) throws Throwable
	{
		byte buffer[] = new byte[(int)f.length()];
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(f);
			fis.read(buffer);
			String data = new String(buffer);
			return data;
		}
		catch (FileNotFoundException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			if (fis!=null)
				fis.close();
		}
	}

	/**
	 * Returns the internal recordset that contains a record
	 * for every Action of the given application. Each record contains
	 * the folllowing columns:<br>
	 * <ul>
	 * <li> path
	 * <li> summary
	 * <li> tx-enabled
	 * <li> dataclass
	 * <li> outputclass
	 * <li> log
	 * <li> jdbclog
	 * </ul>
 	 *
	 * @return Recordset containing list of Actions and related properties
	 */
	public Recordset getActions()
	{
		return action;
	}

}
