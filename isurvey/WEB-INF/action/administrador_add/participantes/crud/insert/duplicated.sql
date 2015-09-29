select
	id_participante, id_empresa	
from 
	ajvieira_isurvey_app.participante
where
	id_participante = ${fld:id_participante}
	and id_empresa = ${fld:id_empresa}