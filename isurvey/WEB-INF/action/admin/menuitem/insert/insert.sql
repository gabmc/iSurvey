insert into ${schema}s_menu_item  
(
	service_id,
	menu_id,
	position
)
values 
(
	${fld:service_id},
	${ses:menu_id},
	${fld:position}
)
