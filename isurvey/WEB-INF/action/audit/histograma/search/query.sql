select 
	a.userlogin,
	s.fname + ' ' + s.lname as usuario,
	count(operation) as total
from
	${schema}s_auditlog a,
	${schema}s_user s
where
	s.userlogin = a.userlogin and
	a.op_date between ${fld:fdesde} and ${fld:fhasta} and
	a.operation = ${fld:operation} and
	a.context_alias = ${fld:contexto}
group by
	a.userlogin,
	s.fname,
	s.lname
