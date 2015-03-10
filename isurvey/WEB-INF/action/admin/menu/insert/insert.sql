insert into ${schema}s_menu  
(
	app_id,
	title,
	position,
	master_menu_id
)
values 
(
	${ses:app_id},
	${fld:title},
	${fld:position},
	${fld:master_menu_id}
)
