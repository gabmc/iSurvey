insert into ${schema}s_application  
(
	app_alias,
	description,
	pwd_policy
)
values 
(
	${fld:app_alias},
	${fld:description},
	${fld:pwd_policy}
)
