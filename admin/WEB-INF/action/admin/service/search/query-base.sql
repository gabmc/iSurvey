select 
	service_id,
	path,
	description,
	app_id
from
	${schema}s_service
where
	app_id=${fld:app_id}

${filter}

