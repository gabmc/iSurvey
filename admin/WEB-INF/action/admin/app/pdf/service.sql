select
	s.*,
	a.description as aplication
from
	${schema}s_service s,
	${schema}s_application a
where
	a.app_id = ${fld:app_id} 
	and a.app_id = s.app_id