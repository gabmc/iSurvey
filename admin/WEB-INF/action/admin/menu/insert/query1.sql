select
	*
from
	${schema}s_menu
where
	app_id = ${ses:app_id} and
	title = ${fld:title}