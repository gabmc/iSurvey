insert into ${schema}s_user 
(
	userlogin,
	lname,
	fname,
	email,
	enabled,
	locale,
	is_ldap,
	dn
)
values 
(
	${fld:userlogin},
	${fld:lname},
	${fld:fname},
	${fld:email},
	1,
	${fld:locale},
	1,
	${fld:dn}
)
