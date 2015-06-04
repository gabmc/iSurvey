select
	lista_participantes.*
from 
	ajvieira_isurvey_app.lista_participantes
where
	id_empresa = ${fld:id_empresa}
	and oculta = 'No'