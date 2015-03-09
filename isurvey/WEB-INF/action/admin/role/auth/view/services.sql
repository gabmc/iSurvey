select 
	s.service_id, 
	s.path, 
	s.description
from
	${schema}s_service s
where 
	s.app_id = ${fld:app_id}
order by 
	s.path
