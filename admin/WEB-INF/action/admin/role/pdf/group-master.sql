select 
	r.rolename,
	r.description,
	r.role_id,
	a.description as aplication
from 
	${schema}s_role r,
	${schema}s_application a
where
	r.app_id=a.app_id
