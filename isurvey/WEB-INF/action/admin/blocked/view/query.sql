select 	*
from 
	${schema}s_user
where
	enabled = 0
order by 
	userlogin
