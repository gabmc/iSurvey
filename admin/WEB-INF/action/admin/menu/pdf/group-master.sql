select 
	m.menu_id,
	m.title,
	a.description as aplication
from 
	${schema}s_menu m,
	${schema}s_application a
where
	m.app_id=a.app_id
	and
	a.app_id = ${fld:app_id}
	and
	a.app_id=m.app_id
