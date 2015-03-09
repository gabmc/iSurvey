select 
	*
from 
	${schema}s_user_role r,
	${schema}s_user u
where 
	r.role_id = ${fld:role_id}
	and r.user_id = u.user_id
order by 
	u.user_id
