select 	l.*, u.userlogin, u.fname, u.lname
from 
	${schema}s_user u, ${schema}s_loginlog l
where
	u.user_id = l.user_id

${filter}

