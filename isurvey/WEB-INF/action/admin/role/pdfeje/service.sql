select
	r.rolename,
	s.*,
	a.description as app_description
from
	${schema}s_service s,
	${schema}s_role r,
	${schema}s_service_role sr,
	${schema}s_application a
where
	r.role_id = ${fld:role_id} 
	and r.role_id = sr.role_id
	and sr.service_id = s.service_id
	and s.app_id =  a.app_id