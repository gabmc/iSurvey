select 
	case
		when is_ldap = 1 then 'LDAP' else 'DB'
	end as tipo,
	s.*
from 
	${schema}s_user s, ${schema}s_role, ${schema}s_user_role
	
where 
	enabled = 1
	and s_user_role.role_id = s_role.role_id
	and s_role.role_id = 507
	and s.user_id = s_user_role.user_id
	${filter}

order by 
	${orderby} ${sortmode}

