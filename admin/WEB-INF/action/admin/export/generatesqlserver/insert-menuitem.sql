insert into ${schema}s_menu_item
(service_id, menu_id, position)

select
	s.service_id, 
	m.menu_id,
	${fld:position}
from 
	${schema}s_service s,
	${schema}s_menu m
	where 
		s.path = ${fld:path} 
		and s.app_id = ident_current('${schema}s_application') 
		and m.title = ${fld:title} 
		and m.app_id = ident_current('${schema}s_application');

