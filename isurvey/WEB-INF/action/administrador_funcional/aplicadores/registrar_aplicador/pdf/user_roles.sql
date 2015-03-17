select 
	*
from 
	${schema}s_user_role ur,
	${schema}s_role r,
	${schema}s_application a
where 
	r.role_id = ur.role_id 
	and ur.user_id =${fld:user_id} 
	and r.app_id = a.app_id
	
