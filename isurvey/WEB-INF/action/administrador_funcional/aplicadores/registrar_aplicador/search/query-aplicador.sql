select
	ajvieira_isurvey_security.s_user.*
from
	ajvieira_isurvey_security.s_user, ajvieira_isurvey_security.s_user_role, ajvieira_isurvey_security.s_role
where
	ajvieira_isurvey_security.s_role.rolename = 'Aplicador'
	and ajvieira_isurvey_security.s_user.user_id = ajvieira_isurvey_security.s_user_role.user_id
	and s_user_role.role_id = ajvieira_isurvey_security.s_role.role_id
order by
	s_user.user_id