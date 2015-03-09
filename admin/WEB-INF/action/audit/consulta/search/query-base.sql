select 
	*
from
	${schema}s_auditlog a,
	${schema}s_user s
where
	s.userlogin = a.userlogin
	
${filter}

