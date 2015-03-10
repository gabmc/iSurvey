insert into ${schema}s_user_role
(user_id, role_id)

select
	${fld:user_id},
	r.role_id 
from 
	${schema}s_role r
	where 
		r.rolename = ${fld:rolename} 
		and r.app_id = ident_current('${schema}s_application');

		
