select 
	s.*,
	empresa.nombre_empresa
from 
	${schema}s_user s, ${schema}s_role, ${schema}s_user_role, ajvieira_isurvey_app.empresa
	
where 
	enabled = 1
	and s_user_role.role_id = s_role.role_id
	and s_role.role_id not in (select role_id from ajvieira_isurvey_security.s_role where role_id = 1)
	and s.user_id = s_user_role.user_id
	and s.id_empresa = empresa.id_empresa
	and concat(s.id_empresa) like ${fld:id_empresa}
	${filter}


UNION

select 
	s.*,
	'' as nombre_empresa
from 
	${schema}s_user s, ${schema}s_role, ${schema}s_user_role, ajvieira_isurvey_app.empresa
	
where 
	enabled = 1
	and s_user_role.role_id = s_role.role_id
	and s_role.role_id not in (select role_id from ajvieira_isurvey_security.s_role where role_id = 1)
	and s.user_id = s_user_role.user_id
	and s.id_empresa not in (select id_empresa from ajvieira_isurvey_app.empresa)
	${filter}

order by 
	${orderby} ${sortmode}

