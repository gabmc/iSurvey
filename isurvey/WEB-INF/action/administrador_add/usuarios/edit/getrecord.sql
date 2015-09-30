select	
	case
		when is_ldap = 0 then '/WEB-INF${def:actionroot}/template.htm' else '/WEB-INF${def:actionroot}/template2.htm'
	end as url,
	s.*, empresa.nombre_empresa as id_empresa_nombre
from ${schema}s_user s, ajvieira_isurvey_app.empresa
where user_id = ${fld:id}
and s.id_empresa = empresa.id_empresa
