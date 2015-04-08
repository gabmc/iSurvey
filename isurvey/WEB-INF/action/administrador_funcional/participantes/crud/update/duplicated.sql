select
	id_participante	
from 
	ajvieira_isurvey_app.participante
where
	id_participante = ${fld:id_participante} and
	id_participante <> ${fld:id}
	