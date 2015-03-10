select 
	*
from 
	${schema}s_user
where
	upper (fname) like upper (${fld:name})
order by 
	user_id
