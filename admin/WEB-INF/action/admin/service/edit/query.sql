select 
	service_id,
	path,
	description,
	app_id
from
	${schema}s_service
where
	service_id = ${fld:id}


