select	
	service_id,
	path,
	description
from ${schema}s_service
where service_id = ${fld:service_id}
order by path
