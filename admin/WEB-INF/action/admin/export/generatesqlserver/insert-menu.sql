insert into ${schema}s_menu  
(
	app_id,
	title,
	position
)
values 
(
	ident_current('${schema}s_application'),
	${fld:title},
	${fld:position}
);

