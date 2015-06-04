select	
	lista_participantes.*
from
	ajvieira_isurvey_app.lista_participantes 
where
	id_lista_participantes is not null
	and lista_participantes.id_empresa = ${fld:id_empresa}
	${filter}