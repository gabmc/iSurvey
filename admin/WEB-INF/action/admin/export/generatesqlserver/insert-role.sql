insert into ${schema}s_role  
(
	app_id,
	rolename,
	description
)
values 
(
	ident_current('${schema}s_application'),
	${fld:rolename},
	${fld:description}
);

