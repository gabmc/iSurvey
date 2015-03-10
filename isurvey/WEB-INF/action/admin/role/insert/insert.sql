insert into ${schema}s_role  
(
	app_id,
	rolename,
	description
)
values 
(
	${fld:app_id},
	${fld:rolename},
	${fld:description}
)
