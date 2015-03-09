select 
	*
from 
	${schema}s_service_role sr,
	${schema}s_role r
where 
	r.role_id = sr.role_id 
	and sr.service_id =${fld:service_id} 
	
