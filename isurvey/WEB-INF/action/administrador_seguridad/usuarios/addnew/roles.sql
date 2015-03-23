select
	role_id,
	s_role.description,
	app_alias
from ${schema}s_role, ${schema}s_application
where s_role.app_id = s_application.app_id
and s_role.role_id not in (select role_id from ajvieira_isurvey_security.s_role where role_id = 1 or role_id = 506)
order by s_application.description, rolename
