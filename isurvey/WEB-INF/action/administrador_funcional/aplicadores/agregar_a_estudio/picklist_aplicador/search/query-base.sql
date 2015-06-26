select
        ajvieira_isurvey_security.s_user.*
from
        ${schema}s_user, ${schema}s_role, ${schema}s_user_role
where
        s_user.id_empresa in (select ss.id_empresa  from ajvieira_isurvey_security.s_user as ss where ss.userlogin='${def:user}')
        and s_user.user_id = s_user_role.user_id
        and s_user_role.role_id = s_role.role_id
        and s_role.role_id = 503
order by
		s_user.user_id desc