insert into ${schema}s_menu_role
(role_id, menu_id)

select
	r.role_id, 
	menu_id

from 
	${schema}s_role r,
	${schema}s_menu m
	where 
		r.rolename = ${fld:rolename} 
		and r.app_id = @app_id 
		and m.title = ${fld:title} 
		and m.app_id = @app_id;
		

