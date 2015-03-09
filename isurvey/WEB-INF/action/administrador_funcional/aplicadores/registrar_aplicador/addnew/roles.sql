select
	role_id,
	s_role.description,
	app_alias
from ${schema}s_role, ${schema}s_application
where s_role.app_id = s_application.app_id
order by s_application.description, rolename
