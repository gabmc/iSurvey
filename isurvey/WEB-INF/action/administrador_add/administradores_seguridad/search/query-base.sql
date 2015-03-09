select 
	case
		when is_ldap = 1 then 'LDAP' else 'DB'
	end as tipo,
	s.*
from 
	${schema}s_user s
	
where 
	enabled = 1
	${filter}

order by 
	${orderby} ${sortmode}

