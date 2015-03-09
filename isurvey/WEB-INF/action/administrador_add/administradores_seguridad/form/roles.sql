select
	role_id,
	${schema}s_role.description,
	app_alias
from ${schema}s_role, ${schema}s_application
where ${schema}s_role.app_id = ${schema}s_application.app_id
and ${schema}s_role.rolename like 'Administrador de Seguridad'
order by ${schema}s_application.description, rolename
