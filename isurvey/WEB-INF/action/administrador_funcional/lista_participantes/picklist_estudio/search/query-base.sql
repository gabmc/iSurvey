select	
	estudio.*
from
	ajvieira_isurvey_app.estudio 
where
	id_estudio is not null
	${filter}
        and estudio.id_empresa in (select ss.id_empresa  from ajvieira_isurvey_security.s_user as ss where ss.userlogin='${def:user}')
        and estudio.tipo not like 'Abierto-Anonimo'
        and estudio.tipo not like 'Abierto-Identificado'
order by
	id_estudio desc