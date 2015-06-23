select	
	lista_participantes.*
from
	ajvieira_isurvey_app.lista_participantes 
where
	id_lista_participantes is not null
	${filter}
order by
	id_lista_participantes desc