insert into ${schema}s_service_role
(role_id, service_id)

select
	r.role_id, 
	s.service_id
from 
	${schema}s_role r,
	${schema}s_service s
where 
	r.rolename = ${fld:rolename} 
	and r.app_id = ident_current('${schema}s_application') 
	and s.path = ${fld:path} 
	and s.app_id = ident_current('${schema}s_application');

