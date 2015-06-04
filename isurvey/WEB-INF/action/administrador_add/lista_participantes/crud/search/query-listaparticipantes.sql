select
	lista_participantes.*
from 
	ajvieira_isurvey_app.lista_participantes
where
	id_empresa = ${fld:id_empresa}
order by 
	id_lista_participantes desc
