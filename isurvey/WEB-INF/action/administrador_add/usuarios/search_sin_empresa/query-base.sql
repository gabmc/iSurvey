select 
	s.*, empresa.nombre_empresa
from 
	${schema}s_user s, ajvieira_isurvey_app.empresa
	
where
	s.id_empresa = empresa.id_empresa
	 and enabled = 1
	${filter}

UNION

select 
	s.*,
	'' as nombre_empresa
from
	${schema}s_user s,${schema}s_role, ${schema}s_user_role

where
	enabled = 1
	and s_user_role.role_id = s_role.role_id
	and s.user_id = s_user_role.user_id	
	and s.id_empresa = 0
	${filter}

order by 
	${orderby} ${sortmode}