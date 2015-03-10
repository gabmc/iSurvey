select 
	*
from 
	${schema}s_menu_item mt,
	${schema}s_service s
where 
	mt.menu_id = ${fld:menu_id}
	and mt.service_id = s.service_id
order by 
	mt.position
