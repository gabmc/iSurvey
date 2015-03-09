select 
	*,
	case
		when master_menu_id is null then 0 else master_menu_id
	end as master_id
from 
	${schema}s_menu
where
	app_id = ${ses:app_id} and
	master_menu_id = ${fld:menu_id}
order by 
	position
