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
		and r.app_id = ident_current('${schema}s_application') 
		and m.title = ${fld:title} 
		and m.app_id = ident_current('${schema}s_application');
		

