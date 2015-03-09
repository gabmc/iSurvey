select
	security.s_user.*
from
	security.s_user, security.s_user_role, security.s_role
where
	security.s_role.rolename = 'Aplicador'
	and security.s_user.user_id = security.s_user_role.user_id
	and s_user_role.role_id = security.s_role.role_id
order by
	s_user.user_id