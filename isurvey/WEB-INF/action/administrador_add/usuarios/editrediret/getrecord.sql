select
	case
		when is_ldap = 1 then '${def:context}${def:actionroot}/edit2?id=' else '${def:context}${def:actionroot}/edit?id='
	end as url,	
	user_id as id
from ${schema}s_user s
where user_id = ${fld:id}
