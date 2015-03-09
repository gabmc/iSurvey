select
	app_id,
	app_alias,
	description
from ${schema}s_application
order by app_alias
