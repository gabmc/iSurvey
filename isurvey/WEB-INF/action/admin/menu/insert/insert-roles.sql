insert into ${schema}s_menu_role  
(
	menu_id,
	role_id
)
values 
(
	${fld:insert.sql.lastid},
	${fld:role_id}
)
