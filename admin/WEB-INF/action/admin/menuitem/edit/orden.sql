select a.menu_item_id, a.position, b.description, b.service_id
from 
	${schema}s_menu_item a,
	${schema}s_service b
where
	a.menu_id = ${ses:menu_id}
	and a.service_id = b.service_id 
	and a.menu_item_id = ${fld:menu_item_id}
order by 
	position
