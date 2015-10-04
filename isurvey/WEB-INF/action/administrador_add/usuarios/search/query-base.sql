select 
	s.*, empresa.nombre_empresa
from 
	${schema}s_user s, ajvieira_isurvey_app.empresa
	
where
	s.id_empresa = empresa.id_empresa
	and s.id_empresa = ${fld:id_empresa} 
	and enabled = 1
	${filter}

order by 
	${orderby} ${sortmode}