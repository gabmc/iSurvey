select 
	case
		when is_ldap = 1 then 'LDAP' else 'DB'
	end as tipo,
	s.*
from 
	${schema}s_user s
	
where 
	enabled = 1
	and s.id_empresa in (select ss.id_empresa  from ajvieira_isurvey_security.s_user as ss where ss.userlogin='${def:user}')
	${filter}

order by 
	${orderby} ${sortmode}

