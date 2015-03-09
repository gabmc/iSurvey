select	
	case
		when is_ldap = 0 then '/WEB-INF${def:actionroot}/template.htm' else '/WEB-INF${def:actionroot}/template2.htm'
	end as url,
	s.*
from ${schema}s_user s
where user_id = ${fld:id}
