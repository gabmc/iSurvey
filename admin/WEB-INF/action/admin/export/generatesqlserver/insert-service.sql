insert into ${schema}s_service
(
	path,
	description,
	app_id
)
values 
(
	${fld:path},
	${fld:description},
	ident_current('${schema}s_application')
);


