select
	role_id,
	description
from ${schema}s_role
where app_id = ${ses:app_id}
order by rolename
