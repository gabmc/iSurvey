select	
	ajvieira_isurvey_app.lista_participantes.id_lista_participantes, 
	ajvieira_isurvey_app.lista_participantes.nombre_lista_participantes as nombre_lista_participantes_pl0,
	ajvieira_isurvey_app.participante.*
from
	ajvieira_isurvey_app.lista_participantes,
	ajvieira_isurvey_app.participante 
where
	ajvieira_isurvey_app.lista_participantes.id_lista_participantes=ajvieira_isurvey_app.participante.id_lista_participantes and
	id_participante = ${fld:id} 
	


