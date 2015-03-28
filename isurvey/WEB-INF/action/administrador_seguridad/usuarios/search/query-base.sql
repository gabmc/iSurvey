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
	and s_role.role_id not in (select role_id from ajvieira_isurvey_security.s_role where role_id = 506 or role_id = 1)
	and s.user_id = s_user_role.user_id
	and s.id_empresa in (select id_empresa from s_user where userlogin = '${def:user}')
	${filter}

order by 
	${orderby} ${sortmode}

