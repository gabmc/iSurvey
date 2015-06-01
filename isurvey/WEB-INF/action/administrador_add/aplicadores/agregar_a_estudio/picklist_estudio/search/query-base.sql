select	
	ajvieira_isurvey_app.estudio.*
from
	ajvieira_isurvey_app.estudio 
where
	id_estudio is not null
	${filter}
        and estudio.id_empresa = ${fld:id_empresa}