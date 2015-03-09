select
	*
from
	${schema}s_menu_item  
where
	service_id = ${fld:service_id} and
	menu_id = ${ses:menu_id}