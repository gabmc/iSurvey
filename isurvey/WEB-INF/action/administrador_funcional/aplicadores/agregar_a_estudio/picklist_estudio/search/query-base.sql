select	
	ajvieira_isurvey_app.estudio.*
from
	ajvieira_isurvey_app.estudio 
where
	id_estudio is not null
	${filter}
        and estudio.id_empresa in (select ss.id_empresa  from security.s_user as ss where ss.userlogin='${def:user}')