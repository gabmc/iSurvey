select
	service_id,
	path,
	description
from ${schema}s_service
where app_id = ${ses:app_id}
order by path
