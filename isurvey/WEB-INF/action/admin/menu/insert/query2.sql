select
	*
from
	${schema}s_menu
where
	app_id = ${ses:app_id} and
	title = ${fld:title} and
	master_menu_id = ${fld:master_menu_id}