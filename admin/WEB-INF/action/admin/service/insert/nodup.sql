select
	*
from
	${schema}s_service
where
	path = ${fld:path} and
	app_id = ${fld:app_id}
