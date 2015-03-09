select 
	case
		when master_menu_id is null then 0 else master_menu_id
	end as master_id,
	title
from 
	${schema}s_menu
where
	menu_id = ${fld:menu_id}