select 
	ap.app_alias as alias
from 
	${schema}s_application ap
where
	ap.app_id = ${fld:app_id} 
